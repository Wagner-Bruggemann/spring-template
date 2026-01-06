package com.namgrengaw.template.application.adapters.out.file.exporter.impl;

import com.namgrengaw.template.application.adapters.out.file.exporter.contracts.FileExporter;
import com.namgrengaw.template.application.exceptions.FileExportException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

@Component
public class XlsxExporter implements FileExporter {

    @Override
    public Resource exportFile(String title, String[] headers, List<Map<String, String>> content) throws FileExportException {

        try (Workbook workBook = new XSSFWorkbook()) {
            final Sheet sheet = workBook.createSheet(title);
            final Row headerRow = sheet.createRow(0);

            buildHeaders(headers, workBook, headerRow);
            buildRows(headers, content, sheet);
            formatCellSizes(headers, sheet);

            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workBook.write(outputStream);
            return new ByteArrayResource(outputStream.toByteArray());
        } catch (IOException e) {
            throw new FileExportException("Error on trying to export a new XLSX file.", e);
        }
    }

    private void formatCellSizes(String[] headers, Sheet sheet) {
        forEachColumn(headers, (index, header) -> sheet.autoSizeColumn(index));
    }

    private void buildRows(String[] headers, List<Map<String, String>> content, Sheet sheet) {
        int rowIndex = 1;
        for (Map<String, String> item : content) {
            Row row = sheet.createRow(rowIndex++);
            forEachColumn(
                    headers,
                    (index, header) -> {
                        row.createCell(index).setCellValue(item.getOrDefault(header, ""));
                    });
        }
    }

    private void buildHeaders(String[] headers, Workbook workBook, Row headerRow) {
        final CellStyle headerCellStyle = createHeaderCellStyle(workBook);
        forEachColumn(
                headers,
            (i, header) -> {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(header);
                cell.setCellStyle(headerCellStyle);
            });
    }

    private CellStyle createHeaderCellStyle(Workbook workBook) {
        CellStyle style = workBook.createCellStyle();
        Font font = workBook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    private void forEachColumn(String[] headers, BiConsumer<Integer, String> callback) {
        for (int index = 0; index < headers.length; index++) {
            callback.accept(index, headers[index]);
        }
    }

}
