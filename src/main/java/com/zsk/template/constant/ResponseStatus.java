package com.zsk.template.constant;


/**
 * @description:
 * @author: ZHANGSHENGKUN938
 * @create: 2018-09-09 11:30
 **/
public enum ResponseStatus
{
   Success(20000, "成功处理"),
   Error(50000,"系统错误");

   private Integer status;
   private String message;

   ResponseStatus(Integer status, String message)
   {
      this.status = status;
      this.message = message;
   }

   public Integer status()
   {
      return status;
   }

   public String message()
   {
      return message;
   }
}
