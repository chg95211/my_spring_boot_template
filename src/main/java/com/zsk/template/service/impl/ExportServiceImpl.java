package com.zsk.template.service.impl;

import com.zsk.template.config.interceptor.ThreadLocalUser;
import com.zsk.template.config.mq.LogSearchSender;
import com.zsk.template.dao.TbExportDao;
import com.zsk.template.model.TbExport;
import com.zsk.template.service.excelBuilder.Excel;
import com.zsk.template.service.ExportService;
import com.zsk.template.util.SnowflakeId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Random;

/**
 * @description:
 * @author: zsk
 * @create: 2019-08-04 20:22
 **/
@Slf4j
@Service
public class ExportServiceImpl implements ExportService
{
    @Autowired
    private SnowflakeId snowflakeId;

    @Autowired
    private TbExportDao exportDao;

    @Autowired
    private ExcelServiceImpl excelService;

    @Autowired
    private LogSearchSender logSearchSender;

    @Override
    public void asyncDownloadFile(String fileName)
    {
        long id = snowflakeId.nextId();
        TbExport export = TbExport.builder().fileName(fileName).id(id).result("process").userId(ThreadLocalUser.getUser().getId()).build();
        exportDao.insert(export);

        logSearchSender.sendExport(export);
    }

    @Override
    public void doExport(TbExport export)
    {
        TbExport dbExport = exportDao.selectByPrimaryKey(export.getId());
        if (!dbExport.getResult().equals("process"))
        {
            log.info("已经处理过了");
            return;
        }

        try (OutputStream fileOut = new FileOutputStream("/home/zsk/" + new Random().nextInt(11111111) + "workbook.xlsx"))
        {
            Excel excel = excelService.getExcel();
            excel.getWorkbook().write(fileOut);
            excel.getWorkbook().close();
            dbExport.setResult("success");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            dbExport.setResult("fail");
        }
        finally
        {
            exportDao.updateByPrimaryKeySelective(dbExport);
        }


    }
}
