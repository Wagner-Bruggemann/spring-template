package com.namgrengaw.template.application.adapters.out.file.importer.model;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.util.Arrays;
import java.util.List;

public class XlsxRow implements TabularRow {

    private final Row row;
    private final List<String> headers;

    public XlsxRow(Row row, String[] headers) {
        this.row = row;
        this.headers = List.copyOf(Arrays.asList(headers));
    }

    @Override
    public String get(String column) {
        int index = headers.indexOf(column);
        return index >= 0 ? get(index) : null;
    }

    @Override
    public String get(int index) {
        Cell cell = row.getCell(index);
        if (cell == null) return null;

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf(cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> null;
        };
    }

    @Override
    public boolean has(String column) {
        return headers.contains(column);
    }

    @Override
    public List<String> headers() {
        return headers;
    }

}
