package com.zsk.template.config.exception;

import com.zsk.template.model.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/*
 * @Description:
 * @Author: zsk
 * @Date: 2018-09-30 16:24
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler
{
    @Value("${DEBUG_MODE}")
    private boolean DEBUG_MODE;

    /**
     * @Description: 数据格式错误.无论是否开启debug mode,都返回给前端
     * @Param:
     * @return:
     */
    @ExceptionHandler(ParameterException.class)
    @ResponseBody
    public Response parameterException(ParameterException e)
    {
        log.error("参数错误{}", e);
        return Response.fail(e.getMessage());
    }


    /**
    * @Description: 其他未处理的异常.只有debug mode开启才返回给前端
    * @Param:
    * @return:
    */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Response exceptionHandler(Exception e)
    {
        log.error("系统异常{}", e);
        if (DEBUG_MODE)
        {
            return Response.fail(e.getMessage());
        }

        return Response.fail("系统异常");
    }
}
