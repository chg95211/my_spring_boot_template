import com.xxxx.cache.service.ICacheService;

import com.xxxx.common.exception.ServiceException;

import com.xxxx.core.logger.Logger;

import com.xxxx.core.logger.LoggerFactory;

import com.xxxx.wmhopenapi.util.RelaxedConfigurationBinder;

import com.xxxx.wmhopenapi.util.signature.Signature;

import com.xxxx.wmhopenapi.util.signature.SignatureContext;

import com.xxxx.wmhopenapi.util.signature.SignatureHeaders;

import com.xxxx.wmhopenapi.util.signature.SignatureUtils;import com.xxxx.wmhopenapi.web.config.LimitConstants;

import com.xxxx.wmhopenapi.web.util.ValidatorUtils;

import com.google.common.collect.Lists;

import org.apache.commons.lang3.StringUtils;

import org.aspectj.lang.ProceedingJoinPoint;

import org.aspectj.lang.annotation.Around;

import org.aspectj.lang.annotation.Aspect;

import org.aspectj.lang.reflect.MethodSignature;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.MethodParameter;

import org.springframework.core.annotation.AnnotationUtils;

import org.springframework.core.annotation.Order;

import org.springframework.core.env.MapPropertySource;

import org.springframework.core.env.PropertySource;

import org.springframework.stereotype.Component;

import org.springframework.util.ClassUtils;

import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.context.request.RequestContextHolder;

import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import java.lang.annotation.Annotation;

import java.lang.reflect.Array;import java.lang.reflect.Method;

import java.util.*;

import java.util.concurrent.TimeUnit;

import java.util.stream.Collectors;

@Order(2)

@Aspect@Component

public class ControllerValidatorAspect {

private static Logger LOGGER = LoggerFactory.getLogger(ControllerValidatorAspect.class);

    //同一个请求多长时间内有效
    private static final Long EXPIRE_TIME = 60 * 1000 * 10L;
    //同一个nonce 请求多长时间内不允许重复请求
    private static final Long RESUBMIT_DURATION = 2000L;

    @Autowired
    private LimitConstants limitConstants;

    @Autowired
    private ICacheService redisCacheService;

    @Around("execution(* com.xxxx.wmhopenapi.web.controller..*.*(..)) " +
            "&& @annotation(com.xxxx.wmhopenapi.util.signature.Signature) " +
            "&& (@annotation(org.springframework.web.bind.annotation.RequestMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.GetMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.PostMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.PatchMapping))"
    )
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {//NOSONAR
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        //如果不是开放的URL, 进行签名校验
        if (Objects.isNull(request.getAttribute(REQUEST_URL_OPEN))) {
            MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
            Method method = methodSignature.getMethod();
            Signature signature = AnnotationUtils.findAnnotation(method, Signature.class);

            //获取header中的相关参数
            SignatureHeaders signatureHeaders = generateSignatureHeaders(signature, request);

            //客户端签名
            String clientSignature = signatureHeaders.getSignature();

            //获取到header中的拼接结果
            String headersToSplice = SignatureUtils.toSplice(signatureHeaders);
            //服务端签名
            List<String> allSplice = generateAllSplice(method, pjp.getArgs(), headersToSplice);

            String serverSignature = SignatureUtils.signature(allSplice.toArray(new String[]{}), signatureHeaders.getAppsecret());

            if (!clientSignature.equals(serverSignature)) {
                LOGGER.error(String.format("签名不一致... clientSignature=%s, serverSignature=%s", clientSignature, serverSignature));
                throw new ServiceException("WMH5001");
            }
            SignatureContext.setSignatureHeaders(signatureHeaders);
            LOGGER.info("签名验证通过, 相关信息: " + signatureHeaders);
        }
        try {
            return pjp.proceed();
        } catch (Throwable e) {//NOSONAR
            throw e;
        }
    }

    /**     * 根据request 中 header值生成SignatureHeaders实体     */
    private SignatureHeaders generateSignatureHeaders(Signature signature, HttpServletRequest request) throws Exception {//NOSONAR
        Map<String, Object> headerMap = Collections.list(request.getHeaderNames())
                .stream()
                .filter(headerName -> SignatureHeaders.HEADER_NAME_SET.contains(headerName))
                .collect(Collectors.toMap(headerName -> headerName.replaceAll("-", "."), headerName -> request.getHeader(headerName)));
        PropertySource propertySource = new MapPropertySource("signatureHeaders", headerMap);
        SignatureHeaders signatureHeaders = RelaxedConfigurationBinder.with(SignatureHeaders.class)
                .setPropertySources(propertySource)
                .doBind();
        Optional<String> result = ValidatorUtils.validateResultProcess(signatureHeaders);
        if (result.isPresent()) {
            throw new ServiceException("WMH5000", result.get());
        }
        String appSecret = limitConstants.getSignatureLimit().get(signatureHeaders.getAppid());
        if (StringUtils.isBlank(appSecret)) {
            LOGGER.error("未找到appId对应的appSecret, appId=" + signatureHeaders.getAppid());
            throw new ServiceException("WMH5002");
        }

        //其他合法性校验
        Long now = System.currentTimeMillis();
        Long requestTimestamp = Long.parseLong(signatureHeaders.getTimestamp());
        if ((now - requestTimestamp) > EXPIRE_TIME) {
            String errMsg = "请求时间超过规定范围时间10分钟, signature=" + signatureHeaders.getSignature();
            LOGGER.error(errMsg);
            throw new ServiceException("WMH5000", errMsg);
        }
        String nonce = signatureHeaders.getNonce();
        if (nonce.length() < 10) {
            String errMsg = "随机串nonce长度最少为10位, nonce=" + nonce;
            LOGGER.error(errMsg);
            throw new ServiceException("WMH5000", errMsg);
        }
        if (!signature.resubmit()) {
            String existNonce = redisCacheService.getString(nonce);
            if (StringUtils.isBlank(existNonce)) {
                redisCacheService.setString(nonce, nonce);
                redisCacheService.expire(nonce, (int) TimeUnit.MILLISECONDS.toSeconds(RESUBMIT_DURATION));
            } else {
                String errMsg = "不允许重复请求, nonce=" + nonce;
                LOGGER.error(errMsg);
                throw new ServiceException("WMH5000", errMsg);
            }
        }

        signatureHeaders.setAppsecret(appSecret);
        return signatureHeaders;
    }

    /**     * 生成header中的参数，mehtod中的参数的拼接     */
    private List<String> generateAllSplice(Method method, Object[] args, String headersToSplice) {
        List<String> pathVariables = Lists.newArrayList(), requestParams = Lists.newArrayList();
        String beanParams = StringUtils.EMPTY;
        for (int i = 0; i < method.getParameterCount(); ++i) {
            MethodParameter mp = new MethodParameter(method, i);
            boolean findSignature = false;
            for (Annotation anno : mp.getParameterAnnotations()) {
                if (anno instanceof PathVariable) {
                    if (!Objects.isNull(args[i])) {
                        pathVariables.add(args[i].toString());
                    }
                    findSignature = true;
                } else if (anno instanceof RequestParam) {
                    RequestParam rp = (RequestParam) anno;
                    String name = mp.getParameterName();
                    if (StringUtils.isNotBlank(rp.name())) {
                        name = rp.name();
                    }
                    if (!Objects.isNull(args[i])) {
                        List<String> values = Lists.newArrayList();
                        if (args[i].getClass().isArray()) {
                            //数组
                            for (int j = 0; j < Array.getLength(args[i]); ++j) {
                                values.add(Array.get(args[i], j).toString());
                            }
                        } else if (ClassUtils.isAssignable(Collection.class, args[i].getClass())) {
                            //集合
                            for (Object o : (Collection<?>) args[i]) {
                                values.add(o.toString());
                            }
                        } else {
                            //单个值
                            values.add(args[i].toString());
                        }
                        values.sort(Comparator.naturalOrder());
                        requestParams.add(name + "=" + StringUtils.join(values));
                    }
                    findSignature = true;
                } else if (anno instanceof RequestBody || anno instanceof ModelAttribute) {
                    beanParams = SignatureUtils.toSplice(args[i]);
                    findSignature = true;
                }

                if (findSignature) {
                    break;
                }
            }
            if (!findSignature) {
                LOGGER.info(String.format("签名未识别的注解, method=%s, parameter=%s, annotations=%s", method.getName(), mp.getParameterName(), StringUtils.join(mp.getMethodAnnotations())));
            }
        }
        List<String> toSplices = Lists.newArrayList();
        toSplices.add(headersToSplice);
        toSplices.addAll(pathVariables);
        requestParams.sort(Comparator.naturalOrder());
        toSplices.addAll(requestParams);
        toSplices.add(beanParams);
        return toSplices;
    }

}