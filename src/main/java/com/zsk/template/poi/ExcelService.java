package com.zsk.template.poi;

import groovy.util.logging.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: zsk
 * @create: 2019-08-04 10:29
 **/
@Slf4j
@Service
public class ExcelService
{
    public static void main(String[] args)
    {
        Excel excel = new ExcelService().getExcel();
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

    public Excel getExcel()
    {
        String[] keys = {"key1", "key2", "key3", "key4"};
        List<Map<String, Object>> list  = new ArrayList<>();
        for (int i = 0; i < 5; i++)
        {
            Map<String, Object> map = new HashMap<>();
            map.put("key1", 111);
            map.put("key2", 222);
            map.put("key3", 333);
            map.put("key4", 444);
            list.add(map);
        }
        Excel excel = Excel.builder()
                .workbook(Excel.WorkbookFactory.XSSF)
                .sheet("testsheet")
                .title(keys)
                .data(keys, list)
                .sheet("testsheet2")
                .title(keys)
                .data(keys, list)
                .build();

       return excel;
    }
}


