package com.dbrowser.server.tablecontent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/connections/{connectionId}/tables/{tableName}/data-preview")
public class TableContentController {

    private final TableContentService tableContentService;

    @Autowired
    public TableContentController(TableContentService tableContentService) {
        this.tableContentService = tableContentService;
    }

    @GetMapping
    public ResponseEntity<TableContent> getTableData(@PathVariable(name = "connectionId") long connectionId,
            @PathVariable(name = "tableName") String tableName) {

        TableContent tableContent = tableContentService.getTableContent(connectionId, tableName);

        return ResponseEntity.ok(tableContent);
    }

}
