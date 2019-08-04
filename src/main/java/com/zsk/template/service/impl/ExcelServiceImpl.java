package com.zsk.template.service.impl;

import com.github.pagehelper.PageHelper;
import com.zsk.template.dao.TbItemDao;
import com.zsk.template.model.TbItem;
import com.zsk.template.service.ExcelService;
import com.zsk.template.service.excelBuilder.Excel;
import com.zsk.template.util.JsonUtil;
import groovy.util.logging.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: zsk
 * @create: 2019-08-04 10:29
 **/
@Slf4j
@Service
public class ExcelServiceImpl implements ExcelService
{
    @Autowired
    private TbItemDao itemDao;

    private int pageSize = 100;

    public static void main(String[] args)
    {
        Excel excel = new ExcelServiceImpl().getExcel();
        try (OutputStream fileOut = new FileOutputStream("workbook.xlsx"))
        {
            excel.getWorkbook().write(fileOut);
            excel.getWorkbook().close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public Excel getExcel()
    {
        String[] keys = {"id",
                        "title",
                        "sell_point",
                        "price",
                        "num",
                        "barcode",
                        "image",
                        "cid",
                        "status",
                        "created",
                        "updated"
                    };
        int count = itemDao.selectCount(null);
        int pages = count / pageSize + 1;
        Excel.ExcelBuilder excelBuilder = Excel
                .builder()
                .workbook(Excel.WorkbookFactory.SXSSF)
                .sheet("商品数据")
                .title(keys);
        for (int i = 0; i < pages; i++)
        {
            PageHelper.startPage(i, pageSize);
            List<TbItem> tbItems = itemDao.selectAll();
            List<Map<String,Object>> list = JsonUtil.objectListToMapList(tbItems);
            excelBuilder.data(keys, list);
            tbItems.clear();
        }

       return excelBuilder.build();
    }
}


