package com.namgrengaw.template.application.adapters.out.file.importer;

import com.namgrengaw.template.application.adapters.out.file.importer.contracts.FileImporter;
import com.namgrengaw.template.application.adapters.out.file.importer.impl.CsvImporter;
import com.namgrengaw.template.application.adapters.out.file.importer.impl.XlsxImporter;
import com.namgrengaw.template.application.exceptions.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class FileImporterFactory {

    private Logger logger = LoggerFactory.getLogger(FileImporterFactory.class);

    @Autowired
    private ApplicationContext context;

    public FileImporter getImporter(String fileName) throws Exception {
        if (fileName.endsWith(".xlsx")) {
            return context.getBean(XlsxImporter.class);
        } else if (fileName.endsWith(".csv")) {
            return context.getBean(CsvImporter.class);
        } else {
            throw new BadRequestException("File exporter type not implemented yet...");
        }
    }

}
