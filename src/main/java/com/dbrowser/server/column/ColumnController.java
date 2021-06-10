package com.dbrowser.server.column;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/connections/{connectionId}/tables/{tableName}/columns")
public class ColumnController {

    private final ColumnService columnService;

    @Autowired
    public ColumnController(ColumnService columnService) {
        this.columnService = columnService;
    }

    @Operation(summary = "Get table columns using stored connection details")
    @GetMapping
    public ResponseEntity<Collection<Column>> getColumns(@PathVariable(name = "connectionId") long connectionId,
            @PathVariable(name = "tableName") String tableName) {

        Collection<Column> columns = columnService.getColumns(connectionId, tableName);

        return ResponseEntity.ok(columns);
    }

    @Operation(summary = "Get a table column by name using stored connection details")
    @GetMapping("/{columnName}")
    public ResponseEntity<Column> getOneColumn(@PathVariable(name = "connectionId") long connectionId,
            @PathVariable(name = "tableName") String tableName, @PathVariable(name = "columnName") String columnName) {

        Column column = columnService.getOneColumn(connectionId, tableName, columnName);

        return ResponseEntity.ok(column);
    }

}
