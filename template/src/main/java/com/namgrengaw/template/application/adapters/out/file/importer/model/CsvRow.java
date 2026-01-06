package com.namgrengaw.template.application.adapters.out.file.importer.model;

import org.apache.commons.csv.CSVRecord;

import java.util.Arrays;
import java.util.List;

public class CsvRow implements TabularRow {

    private final CSVRecord record;
    private final List<String> headers;

    public CsvRow(CSVRecord record, String[] headers) {
        this.record = record;
        this.headers = List.copyOf(Arrays.asList(headers));
    }

    @Override
    public String get(String column) {
        return record.isMapped(column) ? record.get(column) : null;
    }

    @Override
    public String get(int index) {
        return record.get(index);
    }

    @Override
    public boolean has(String column) {
        return record.isMapped(column);
    }

    @Override
    public List<String> headers() {
        return headers;
    }
}
