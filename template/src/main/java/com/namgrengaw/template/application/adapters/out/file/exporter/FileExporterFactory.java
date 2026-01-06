package com.namgrengaw.template.application.adapters.out.file.exporter;

import com.namgrengaw.template.application.adapters.out.file.FileMediaTypes;
import com.namgrengaw.template.application.adapters.out.file.exporter.contracts.FileExporter;
import com.namgrengaw.template.application.adapters.out.file.exporter.impl.CsvExporter;
import com.namgrengaw.template.application.adapters.out.file.exporter.impl.XlsxExporter;
import com.namgrengaw.template.application.exceptions.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class FileExporterFactory {

    private Logger logger = LoggerFactory.getLogger(FileExporterFactory.class);

    @Autowired
    private ApplicationContext context;

    public FileExporter getExporter(String acceptHeader) throws Exception {
        if (acceptHeader.equalsIgnoreCase(FileMediaTypes.APPLICATION_XLSX_VALUE)) {
            return context.getBean(XlsxExporter.class);
        } else if (acceptHeader.equalsIgnoreCase(FileMediaTypes.APPLICATION_CSV_VALUE)) {
            return context.getBean(CsvExporter.class);
        } else {
            throw new BadRequestException("File exporter type not implemented yet...");
        }
    }

}
