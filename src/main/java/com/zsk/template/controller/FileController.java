package com.zsk.template.controller;

import com.zsk.template.model.response.Response;
import com.zsk.template.poi.Excel;
import com.zsk.template.poi.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @description:
 * @author: zsk
 * @create: 2019-08-04 14:17
 **/
@RestController
public class FileController
{
    @Autowired
    private ExcelService excelService;

    @GetMapping("/downloadFile/{fileName:.+}")
    public void downloadFile(@PathVariable String fileName, HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        Excel excel = excelService.getExcel();
        Response.excel(request, response, excel.getWorkbook(), fileName);
    }

}
