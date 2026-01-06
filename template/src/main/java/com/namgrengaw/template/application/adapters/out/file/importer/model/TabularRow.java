package com.namgrengaw.template.application.adapters.out.file.importer.model;

import java.util.List;

public interface TabularRow {

    String get(String column);

    String get(int index);

    boolean has(String column);

    List<String> headers();

}
