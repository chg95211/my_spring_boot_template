package com.zsk.template.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @description:
 * @author: zsk
 * @create: 2019-05-12 16:25
 **/
@Controller
public class PageController
{
    @GetMapping("/page/{page}")
    public String page(@PathVariable String page)
    {
        return page;
    }
}
