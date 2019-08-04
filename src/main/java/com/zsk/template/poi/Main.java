package com.zsk.template.poi;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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
public class Main
{
    public static void main(String[] args) throws IOException
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
}


class Excel
{
    private static final String EMPTY = "";
    private static final Map<String, String> TITLE_TRANSLARION = new HashMap<>();
    static
    {
        TITLE_TRANSLARION.put("key1", "title1");
        TITLE_TRANSLARION.put("key2", "title2");
        TITLE_TRANSLARION.put("key3", "title3");
        TITLE_TRANSLARION.put("key4", "title4");
    }

    private Workbook workbook;

    public Excel(Workbook workbook)
    {
        this.workbook = workbook;
    }

    public static ExcelBuilder builder()
    {
        return new ExcelBuilder();
    }

    public Workbook getWorkbook()
    {
        return workbook;
    }

    public static class WorkbookFactory
    {
        public static final String SXSSF = "SXSSF";
        public static final String XSSF = "XSSF";
        public static final String HSSF = "HSSF";

        public static Workbook getWorkbook(String type)
        {
            if (XSSF.equals(type))
            {
                return new XSSFWorkbook();
            }
            else if (SXSSF.equals(type))
            {
                return new SXSSFWorkbook();
            }
            else if (HSSF.equals(type))
            {
                return new HSSFWorkbook();
            }
            else
            {
                throw new RuntimeException("invalid type");
            }
        }
    }

    public static class ExcelBuilder {
        private Workbook workbook;
        private Sheet sheet;
        private Integer rowNum;

        ExcelBuilder() {
        }

        public Excel build()
        {
            return new Excel(workbook);
        }

        public ExcelBuilder workbook(String type)
        {
            workbook = WorkbookFactory.getWorkbook(type);
            return this;
        }

        public ExcelBuilder sheet(String sheetName)
        {
            sheet = workbook.createSheet(sheetName);
            rowNum = 0;
            return this;
        }

        public ExcelBuilder title(String[] keys)
        {
            Row row = sheet.createRow(rowNum);
            for (int i = 0; i < keys.length; i++)
            {
                Cell cell = row.createCell(i);
                cell.setCellValue(TITLE_TRANSLARION.getOrDefault(keys[i], keys[i]));
            }
            rowNum++;
            return this;
        }

        public ExcelBuilder data(String[] keys, List<Map<String, Object>> list)
        {
            for (Map<String, Object> data : list)
            {
                Row row = sheet.createRow(rowNum);
                for (int i = 0; i < keys.length; i++)
                {
                    Cell cell = row.createCell(i);
                    cell.setCellValue(String.valueOf(data.getOrDefault(keys[i], EMPTY)));
                }
                rowNum++;
            }
            return this;
        }

    }
    
}

