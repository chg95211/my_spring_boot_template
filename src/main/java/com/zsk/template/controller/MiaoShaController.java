package com.zsk.template.controller;

import com.zsk.template.model.TaoMiaosha;
import com.zsk.template.model.response.Response;
import com.zsk.template.service.MiaoShaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: zsk
 * @create: 2019-05-30 22:49
 **/
@RestController
@RequestMapping("/miaosha")
public class MiaoShaController
{
    @Autowired
    private MiaoShaService miaoShaService;

    @PostMapping("/do")
    public Response doMiaoSha(@RequestBody TaoMiaosha miaosha)
    {
        return Response.success(this.miaoShaService.doMiaoSha(miaosha));
    }
}
