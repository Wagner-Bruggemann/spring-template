package com.namgrengaw.template.application.adapters.out.file.exporter.impl;

import com.namgrengaw.template.application.adapters.out.file.exporter.contracts.FileExporter;
import com.namgrengaw.template.application.exceptions.FileExportException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class CsvExporter implements FileExporter {

    @Override
    public Resource exportFile(String title, String[] headers, List<Map<String, String>> content) throws FileExportException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {
            CSVFormat format = buildHeadersFormat(headers);

            try (CSVPrinter csvPrinter = new CSVPrinter(writer, format)) {
                buildTitle(title, csvPrinter);
                buildRows(headers, content, csvPrinter);
            }
        } catch (IOException e) {
            throw new FileExportException("Error on trying to export a CSV file", e);
        }

        return new ByteArrayResource(outputStream.toByteArray());
    }

    private CSVFormat buildHeadersFormat(String[] headers) {
        CSVFormat format = CSVFormat.Builder.create()
                .setHeader(headers)
                .setSkipHeaderRecord(false)
                .get();
        return format;
    }

    private void buildRows(String[] headers, List<Map<String, String>> content, CSVPrinter csvPrinter) throws IOException {
        for (Map<String, String> item : content) {
            csvPrinter.printRecord(
                    Arrays.stream(headers)
                            .map(header -> item.getOrDefault(header, ""))
                            .toArray()
            );
        }
    }

    private void buildTitle(String title, CSVPrinter csvPrinter) throws IOException {
        if (title != null && !title.isBlank()) {
            csvPrinter.printComment(title);
        }
    }

}
