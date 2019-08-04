package com.zsk.template.service;

import com.zsk.template.model.TbExport;

/**
 * @description:
 * @author: zsk
 * @create: 2019-08-04 20:22
 **/
public interface ExportService
{
    void asyncDownloadFile(String fileName);

    void doExport(TbExport export);
}
