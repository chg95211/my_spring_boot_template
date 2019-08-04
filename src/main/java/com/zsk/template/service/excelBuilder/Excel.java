package com.zsk.template.service.excelBuilder;

import com.zsk.template.util.DateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: zsk
 * @create: 2019-08-04 14:22
 **/
public class Excel
{
    private static final String EMPTY = "";
    private static final Map<String, String> TITLE_TRANSLARION = new HashMap<>();
    static
    {
        TITLE_TRANSLARION.put("id", "id");
        TITLE_TRANSLARION.put("title", "标题");
        TITLE_TRANSLARION.put("sell_point", "卖点");
        TITLE_TRANSLARION.put("price", "价格");
        TITLE_TRANSLARION.put("num", "数量");
        TITLE_TRANSLARION.put("barcode", "条形码");
        TITLE_TRANSLARION.put("image", "图片");
        TITLE_TRANSLARION.put("cid", "分类id");
        TITLE_TRANSLARION.put("status", "状态");
        TITLE_TRANSLARION.put("created", "创建时间");
        TITLE_TRANSLARION.put("updated", "更新时间");
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
                    cell.setCellValue(Excel.handleData(keys[i], data.get(keys[i])));
                }
                rowNum++;
            }
            return this;
        }

    }

    private static String handleData(String key, Object value)
    {
        if (value == null)
        {
            return EMPTY;
        }

        String valueStr = value.toString();

        if ("price".equals(key))
        {
            return String.valueOf(Long.parseLong(valueStr) / 100.0);
        }
        else if ("created".equals(key))
        {
            return DateUtil.timestampToLocalDateTimeStr(new Timestamp(Long.parseLong(valueStr)));
        }
        else if ("updated".equals(key))
        {
            return DateUtil.timestampToLocalDateTimeStr(new Timestamp(Long.parseLong(valueStr)));
        }

        return valueStr;
    }

}
