package com.zsk.template.controller;

import com.zsk.template.model.response.Response;
import com.zsk.template.service.TestService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
public class TestController
{
    @Autowired
    private TestService testService;

    @ApiOperation(value = "test", notes = "test test")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mid", value = "test", required = false, dataType = "String")
    })
    @GetMapping("/test")
    public Object test()
    {
        return testService.list();
    }

    @PostMapping("/test/upload")
    public Response upload(MultipartFile file)
    {
        log.info("文件大小{}",file.getSize());
        log.info("原始文件名{}",file.getOriginalFilename());
        log.info("文件名{}",file.getName());
        log.info("文件类型{}",file.getContentType());

        return Response.success("success");
    }

    @PostMapping("/test/upload/multi")
    public Response uploadMulti(HttpServletRequest request)
    {
        if(request instanceof MultipartHttpServletRequest)
        {
            MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest)request;
            List<MultipartFile> files = mRequest.getFiles("file");
            for (MultipartFile file : files)
            {
                log.info("文件大小{}", file.getSize());
                log.info("原始文件名{}", file.getOriginalFilename());
                log.info("文件名{}", file.getName());
                log.info("文件类型{}", file.getContentType());
            }

        }

        return Response.success("success");
    }


}