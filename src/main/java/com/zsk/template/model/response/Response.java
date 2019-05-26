package com.zsk.template.model.response;


import com.zsk.template.constant.ResponseStatus;

public class Response<T> implements java.io.Serializable
{
    private Integer status;//状态码
    private String message;//错误时的提示信息
    private T data;//成功时的数据

    private Response()
    {
    }

    private Response(Integer status, String message, T data)
    {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> Response success(T data)
    {
        return new Response<>(ResponseStatus.Success.status(),null, data);
    }

    public static  Response fail(String message)
    {
        return new Response<>(ResponseStatus.Error.status(), message,null);
    }

    public static <T> Response custom(Integer status, String message, T data)
    {
        return new Response<>(status,message,data);
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public T getData()
    {
        return data;
    }

    public void setData(T data)
    {
        this.data = data;
    }

    @Override
    public String toString()
    {
        return "Response{" + "status=" + status + ", message='" + message + '\'' + ", data=" + data + '}';
    }
}