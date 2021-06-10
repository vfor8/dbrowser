package com.dbrowser.server.table;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/connections/{connectionId}/tables")
public class TableController {

    private final TableService tableService;

    @Autowired
    public TableController(TableService tableService) {
        this.tableService = tableService;
    }

    @Operation(summary = "Get all tables in a database using stored connection details")
    @GetMapping
    public ResponseEntity<Collection<Table>> getAllTables(@PathVariable long connectionId) {

        Collection<Table> tables = tableService.getTables(connectionId);

        return ResponseEntity.ok(tables);
    }

    @Operation(summary = "Get a table by name using stored connection details")
    @GetMapping("/{tableName}")
    public ResponseEntity<Table> getOneTable(@PathVariable(name = "connectionId") long connectionId,
            @PathVariable(name = "tableName") String tableName) {

        Table table = tableService.getOneTable(connectionId, tableName);

        return ResponseEntity.ok(table);
    }

}
