package com.zsk.template.service.impl;

import com.zsk.template.service.EsAdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: zsk
 * @create: 2019-05-25 17:02
 **/
@Slf4j
@Service
public class EsAdminServiceImpl implements EsAdminService
{

    @Override
    public void syncData(String type)
    {
        log.info("[syncData] 同步 type:{} start",type);

        try
        {
            Thread.sleep(10000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        log.info("[syncData] 同步 type:{} end",type);

    }
}
