package com.namgrengaw.template.application.adapters.out.file.importer.impl;

import com.namgrengaw.template.application.adapters.out.file.importer.contracts.FileImporter;
import com.namgrengaw.template.application.adapters.out.file.importer.model.TabularRow;
import com.namgrengaw.template.application.adapters.out.file.importer.model.XlsxRow;
import com.namgrengaw.template.application.exceptions.FileImportException;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

@Component
public class XlsxImporter implements FileImporter {
    @Override
    public <T> List<T> importFile(InputStream inputStream, String[] headers, Function<TabularRow, T> mapper) throws FileImportException {
        if (headers == null || headers.length == 0) {
            throw new FileImportException("XLSX headers must be provided.");
        }

        try (XSSFWorkbook workbook = new XSSFWorkbook(inputStream)) {
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            if(rowIterator.hasNext()) rowIterator.next();

            return mapRows(rowIterator, headers, mapper);
        } catch (IOException e) {
            throw new FileImportException("Error on trying to import a XLSX file.", e);
        }
    }

    private <T> List<T> mapRows(Iterator<Row> rowIterator, String[] headers, Function<TabularRow, T> mapper) {
        List<T> content = new ArrayList<>();

        while(rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if(isRowValid(row, headers)) {
                T item = mapper.apply(new XlsxRow(row, headers));
                content.add(item);
            }
        }

        return content;
    }

    private static boolean isRowValid(Row row, String[] headers) {
        for (int i = 0; i < headers.length; i++) {
            if (row.getCell(i) != null && row.getCell(i).getCellType() != CellType.BLANK)
                return true;
        }
        return false;
    }
}
