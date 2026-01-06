package com.namgrengaw.template.application.adapters.out.file.importer.impl;

import com.namgrengaw.template.application.adapters.out.file.importer.contracts.FileImporter;
import com.namgrengaw.template.application.adapters.out.file.importer.model.CsvRow;
import com.namgrengaw.template.application.adapters.out.file.importer.model.TabularRow;
import com.namgrengaw.template.application.exceptions.FileImportException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Component
public class CsvImporter implements FileImporter {
    @Override
    public <T> List<T> importFile(InputStream inputStream, String[] headers, Function<TabularRow, T> mapper) throws FileImportException {
        if (headers == null || headers.length == 0) {
            throw new FileImportException("CSV headers must be provided.");
        }
        CSVFormat format = CSVFormat.Builder.create()
                .setHeader(headers)
                .setSkipHeaderRecord(true)
                .setIgnoreEmptyLines(true)
                .setTrim(true)
                .build();

        try {
            try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
                Iterable<CSVRecord> records = format.parse(inputStreamReader);
                return mapRecords(records, headers, mapper);
            }
        } catch (IOException e) {
            throw new FileImportException("Error on trying to import a CSV file.",e);
        }
    }

    private <T> List<T> mapRecords(Iterable<CSVRecord> records, String[] headers, Function<TabularRow, T> mapper) {
        List<T> content = new ArrayList<>();

        for (CSVRecord record : records) {
            T item = mapper.apply(new CsvRow(record, headers));
            content.add(item);
        }

        return content;
    }
}
