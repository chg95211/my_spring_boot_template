//package com.zsk.template.config.filter;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.*;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletRequestWrapper;
//import java.io.IOException;
//
///*
// * @Description:
// * @Author: zsk
// * @Date: 2018-11-23 11:31
// */
//@Component
//@WebFilter(urlPatterns = "/*")
//@Slf4j
//public class CharacterFilter implements Filter
//{
//    static class FilteredRequest extends HttpServletRequestWrapper
//    {
//
//        static String disallowedChars = "~(){}[]\\/!^\"";
//
//        FilteredRequest(ServletRequest request)
//        {
//            super((HttpServletRequest) request);
//        }
//
//        String sanitize(String input)
//        {
//            //            StringBuilder result = new StringBuilder();
//            //            for (int i = 0; i < input.length(); i++)
//            //            {
//            //                if (disallowedChars.indexOf(input.charAt(i)) >= 0)
//            //                    result.append(" ");
//            //                else
//            //                    result.append(input.charAt(i));
//            //            }
//            //            return result.toString();
//
//            //把~(){}[]\/!^"这些字符替换成空格
//            String result = input.replaceAll("[~\\(\\){}\\[\\]\\\\!\\^\\/\"]", " ");
//            return result;
//        }
//
//
//        @Override
//        public String[] getParameterValues(String paramName)
//        {
//            String values[] = super.getParameterValues(paramName);
//
//            if ("queryString".equals(paramName))
//            {
//
//                for (int index = 0; index < values.length; index++)
//                {
//                    String newValue = sanitize(values[index]);
//                    log.info("Escape {} to {}", values[index], newValue);
//                    values[index] = newValue;
//                }
//            }
//
//            return values;
//        }
//    }
//
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
//    {
//        chain.doFilter(new FilteredRequest(request), response);
//    }
//
//    public void destroy()
//    {
//    }
//
//    public void init(FilterConfig filterConfig)
//    {
//    }
//}
