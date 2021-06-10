package com.dbrowser.server.tablecontent;

import java.util.ArrayList;
import java.util.List;

public class TableContent {

    private final List<List<String>> rows = new ArrayList<>();

    public void addRow(List<String> rowValues) {
        rows.add(rowValues);
    }

    public List<List<String>> getRows() {
        return rows;
    }
}
