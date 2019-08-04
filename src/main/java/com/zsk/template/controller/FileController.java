package com.zsk.template.controller;

import com.zsk.template.model.response.Response;
import com.zsk.template.service.ExcelService;
import com.zsk.template.service.excelBuilder.Excel;
import com.zsk.template.service.impl.ExcelServiceImpl;
import com.zsk.template.service.ExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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
@RequestMapping("/file")
public class FileController
{
    @Autowired
    private ExcelService excelService;

    @Autowired
    private ExportService exportService;

    @GetMapping("/downloadFile/{fileName:.+}")
    public void downloadFile(@PathVariable String fileName, HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        Excel excel = excelService.getExcel();
        Response.excel(request, response, excel.getWorkbook(), fileName);
    }

    @GetMapping("/asyncDownloadFile/{fileName:.+}")
    public Response asyncDownloadFile(@PathVariable String fileName)
    {
        exportService.asyncDownloadFile(fileName);
        return Response.success(null);
    }

}
