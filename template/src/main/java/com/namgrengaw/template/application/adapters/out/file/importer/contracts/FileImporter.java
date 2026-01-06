package com.namgrengaw.template.application.adapters.out.file.importer.contracts;

import com.namgrengaw.template.application.adapters.out.file.importer.model.TabularRow;
import com.namgrengaw.template.application.exceptions.FileImportException;

import java.io.InputStream;
import java.util.List;
import java.util.function.Function;

public interface FileImporter {

    <T> List<T> importFile(InputStream inputStream, String[] headers, Function<TabularRow, T> mapper) throws FileImportException;

}
