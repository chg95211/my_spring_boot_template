import com.xxxx.core.logger.Logger;

import com.xxxx.core.logger.LoggerFactory;

import com.google.common.collect.Lists;

import org.apache.commons.codec.digest.HmacUtils;

import org.apache.commons.lang3.RandomStringUtils;

import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.BeanUtils;

import org.springframework.util.ClassUtils;

import org.springframework.util.CollectionUtils;

import java.lang.annotation.Annotation;

import java.lang.reflect.AnnotatedElement;

import java.lang.reflect.Array;

import java.lang.reflect.Field;

import java.util.*;

public class SignatureUtils {

private static final Logger LOGGER = LoggerFactory.getLogger(SignatureUtils.class);
    private static final byte XOR_TOKEN = (byte) 0xFF;
    private static final String NOT_FOUND = "$_$";
    private static final String DELIMETER = "^_^";
    /**

        * 客户端调用
      * @param  signatureHeaders header中需要的参数

      * @param  pathParams @PathVariable 需要的参数

      * @param  requestParamMap @RequestParam需要的参数

      * @param entity  @ModelAttribute 或者 @RequestBody需要的参数

    */

    public static String signature(SignatureHeaders signatureHeaders, List<String> pathParams, Map<String, Object> requestParamMap, Object entity) {
        List<String> requestParams = Collections.EMPTY_LIST;
        List<String> pathVariables = Collections.EMPTY_LIST;
        String beanParams = StringUtils.EMPTY;
        if (!CollectionUtils.isEmpty(pathParams)) {
            pathVariables = pathParams;
        }
        if (!CollectionUtils.isEmpty(requestParamMap)) {
            requestParams = new ArrayList<>();
            for (Map.Entry<String, Object> entry : requestParamMap.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                List<String> values = Lists.newArrayList();
                if (value.getClass().isArray()) {
                    //数组
                    for (int j = 0; j < Array.getLength(value); ++j) {
                        values.add(Array.get(value, j).toString());
                    }
                } else if (ClassUtils.isAssignable(Collection.class, value.getClass())) {
                    //集合
                    for (Object o : (Collection<?>) value) {
                        values.add(o.toString());
                    }
                } else {
                    //单个值
                    values.add(value.toString());
                }
                values.sort(Comparator.naturalOrder());
                requestParams.add(key + "=" + StringUtils.join(values));
            }
        }
        if (!Objects.isNull(entity)) {
            beanParams = toSplice(entity);
        }
        String headersToSplice = SignatureUtils.toSplice(signatureHeaders);

        List<String> toSplices = Lists.newArrayList();
        toSplices.add(headersToSplice);
        toSplices.addAll(pathVariables);
        requestParams.sort(Comparator.naturalOrder());
        toSplices.addAll(requestParams);
        toSplices.add(beanParams);
        return SignatureUtils.signature(toSplices.toArray(new String[]{}), signatureHeaders.getAppsecret());
    }

    public static String encode(String text, String appsecret) {
        byte token = (byte) (appsecret.hashCode() & XOR_TOKEN);
        byte[] tb = text.getBytes();
        for (int i = 0; i < tb.length; ++i) {
            tb[i] ^= token;
        }
        return Base64.getEncoder().encodeToString(tb);
    }

    public static String decode(String text, String appsecret) {
        byte token = (byte) (appsecret.hashCode() & XOR_TOKEN);
        byte[] tb = Base64.getDecoder().decode(text);
        for (int i = 0; i < tb.length; ++i) {
            tb[i] ^= token;
        }
        return new String(tb);
    }

    /**     * 生成签名1     */
    public static String signature(String[] args, String appsecret) {
        String splice = StringUtils.join(args, DELIMETER);
        System.out.println("拼接结果: " + splice);
        String signature = HmacUtils.hmacSha256Hex(appsecret, splice);
        return signature;
    }

    /**     * 生成签名2     */
    public static String signature(Object object, String appsecret) {
        if (Objects.isNull(object)) {
            return StringUtils.EMPTY;
        }
        String splice = toSplice(object);
        System.out.println("拼接结果: " + splice);
        if (StringUtils.isBlank(splice)) {
            return splice;
        }
        String signature = HmacUtils.hmacSha256Hex(appsecret, splice);
        return signature;
    }
    /**

     * 生成所有注有 SignatureField属性 key=value的 拼接         */

public static String toSplice(Object object) {
        if (Objects.isNull(object)) {
            return StringUtils.EMPTY;
        }
        if (isAnnotated(object.getClass(), Signature.class)) {
            Signature sg = findAnnotation(object.getClass(), Signature.class);
            switch (sg.sort()) {
                case Signature.ALPHA_SORT:
                    return alphaSignature(object);
                case Signature.ORDER_SORT:
                    return orderSignature(object);
                default:
                    return alphaSignature(object);
            }
        }
        return toString(object);
    }
    /**

      * 生成唯一nonce随机数

      * 仅供参考，不一定非得使用该方法

    */

public static String generateNonce() {
        return HmacUtils.hmacSha256Hex(UUID.randomUUID().toString(), RandomStringUtils.random(10, true, true));
    }

    private static String alphaSignature(Object object) {
        StringBuilder result = new StringBuilder();
        Map<String, String> map = new TreeMap<>();
        for (Field field : getAllFields(object.getClass())) {
            if (field.isAnnotationPresent(SignatureField.class)) {
                field.setAccessible(true);
                try {
                    if (isAnnotated(field.getType(), Signature.class)) {
                        if (!Objects.isNull(field.get(object))) {
                            map.put(field.getName(), toSplice(field.get(object)));
                        }
                    } else {
                        SignatureField sgf = field.getAnnotation(SignatureField.class);
                        if (StringUtils.isNotEmpty(sgf.customValue()) || !Objects.isNull(field.get(object))) {
                            map.put(StringUtils.isNotBlank(sgf.customName()) ? sgf.customName() : field.getName()
                                    , StringUtils.isNotEmpty(sgf.customValue()) ? sgf.customValue() : toString(field.get(object)));
                        }
                    }
                } catch (Exception e) {
                    LOGGER.error("签名拼接(alphaSignature)异常", e);
                }
            }
        }

        for (Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<String, String> entry = iterator.next();
            result.append(entry.getKey()).append("=").append(entry.getValue());
            if (iterator.hasNext()) {
                result.append(DELIMETER);
            }
        }
        return result.toString();
    }

    private static String orderSignature(Object object) {
        StringBuilder result = new StringBuilder();
        Set<OrderNode> set = new HashSet<>(16);
        for (Field field : getAllFields(object.getClass())) {
            if (field.isAnnotationPresent(SignatureField.class)) {
                field.setAccessible(true);
                SignatureField sgf = field.getAnnotation(SignatureField.class);
                try {
                    if (isAnnotated(field.getType(), Signature.class)) {
                        if (!Objects.isNull(field.get(object))) {
                            set.add(new OrderNode(sgf.order(), field.getName(), toSplice(field.get(object))));
                        }
                    } else {
                        if (StringUtils.isNotEmpty(sgf.customValue()) || !Objects.isNull(field.get(object))) {
                            set.add(new OrderNode(sgf.order()
                                    , StringUtils.isNotBlank(sgf.customName()) ? sgf.customName() : field.getName()
                                    , StringUtils.isNotEmpty(sgf.customValue()) ? sgf.customValue() : toString(field.get(object))));
                        }
                    }
                } catch (Exception e) {
                    LOGGER.error("签名拼接(orderSignature)异常", e);
                }
            }
        }
        for (Iterator<OrderNode> iterator = set.iterator(); iterator.hasNext(); ) {
            OrderNode on = iterator.next();
            result.append(on.getName()).append("=").append(on.getValue());
            if (iterator.hasNext()) {
                result.append(DELIMETER);
            }
        }
        return result.toString();
    }

    private static String toString(Object object) {
        Class<?> type = object.getClass();
        if (BeanUtils.isSimpleProperty(type)) {
            return object.toString();
        }
        if (type.isArray()) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < Array.getLength(object); ++i) {
                sb.append(toSplice(Array.get(object, i)));
            }
            return sb.toString();
        }
        if (ClassUtils.isAssignable(Collection.class, type)) {
            StringBuilder sb = new StringBuilder();
            for (Iterator<?> iterator = ((Collection<?>) object).iterator(); iterator.hasNext(); ) {
                sb.append(toSplice(iterator.next()));
                if (iterator.hasNext()) {
                    sb.append(DELIMETER);
                }
            }
            return sb.toString();
        }
        if (ClassUtils.isAssignable(Map.class, type)) {
            StringBuilder sb = new StringBuilder();
            for (Iterator<? extends Map.Entry<String, ?>> iterator = ((Map<String, ?>) object).entrySet().iterator(); iterator.hasNext(); ) {
                Map.Entry<String, ?> entry = iterator.next();
                if (Objects.isNull(entry.getValue())) {
                    continue;
                }
                sb.append(entry.getKey()).append("=").append(toSplice(entry.getValue()));
                if (iterator.hasNext()) {
                    sb.append(DELIMETER);
                }
            }
            return sb.toString();
        }
        return NOT_FOUND;
    }

    private static <A extends Annotation> A findAnnotation(AnnotatedElement element, Class<A> annotationType) {
        A annotation = element.getAnnotation(annotationType);
        return annotation;
    }

    private static boolean isAnnotated(AnnotatedElement element, Class<? extends Annotation> annotationType) {
        return element.isAnnotationPresent(annotationType);
    }

    public static Set<Field> getAllFields(final Class<?> type) {
        Set<Field> result = new HashSet<>(16);
        for (Class<?> t : getAllSuperTypes(type)) result.addAll(Arrays.asList(t.getDeclaredFields()));
        return result;
    }

    private static Set<Class<?>> getAllSuperTypes(final Class<?> type) {
        Set<Class<?>> result = new LinkedHashSet<>(16);
        if (type != null && !type.equals(Object.class)) {
            result.add(type);
            for (Class<?> supertype : getSuperTypes(type)) {
                result.addAll(getAllSuperTypes(supertype));
            }
        }
        return result;
    }

    private static Set<Class<?>> getSuperTypes(Class<?> type) {
        Set<Class<?>> result = new LinkedHashSet<>();
        Class<?> superclass = type.getSuperclass();
        Class<?>[] interfaces = type.getInterfaces();
        if (superclass != null && !superclass.equals(Object.class)) result.add(superclass);
        if (interfaces != null && interfaces.length > 0) result.addAll(Arrays.asList(interfaces));
        return result;
    }

    private static class OrderNode implements Comparable<OrderNode> {
        private int order;
        private String name;
        private String value;

        public OrderNode(int order, String name, String value) {
            this.order = order;
            this.name = name;
            this.value = value;
        }

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public int compareTo(OrderNode o) {
            if (this.order == o.order) {
                return this.name.compareTo(o.name);
            }
            return this.order - o.order;
        }

        @Override
        public boolean equals(Object obj) {
            return super.equals(obj);
        }

        @Override
        public int hashCode() {
            return Objects.hash(order, value);
        }
    }

}