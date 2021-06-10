package com.dbrowser.server.table;

import com.dbrowser.server.connection.ConnectionDetails;
import com.dbrowser.server.connection.ConnectionDetailsService;
import com.dbrowser.server.db.JdbcTemplateProvider;
import com.dbrowser.server.error.DBrowserException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Optional;

@Service
public class TableService {

    private static final String SELECT_TABLES =
            "SELECT TABLE_NAME, TABLE_TYPE, ENGINE, TABLE_COLLATION, TABLE_COMMENT FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = ?";

    private static final String SELECT_ONE_TABLE = SELECT_TABLES + " AND TABLE_NAME = ?";

    private static final RowMapper<Table> ROW_MAPPER = (resultSet, rowNum) -> {
        var t = new Table();
        t.setName(resultSet.getString("TABLE_NAME"));
        t.setType(resultSet.getString("TABLE_TYPE"));
        t.setEngine(resultSet.getString("ENGINE"));
        t.setCollation(resultSet.getString("TABLE_COLLATION"));
        t.setComment(resultSet.getString("TABLE_COMMENT"));
        return t;
    };

    private final ConnectionDetailsService connectionService;

    private final JdbcTemplateProvider jdbcTemplateProvider;

    @Autowired
    public TableService(ConnectionDetailsService connectionService, JdbcTemplateProvider jdbcTemplateProvider) {
        this.connectionService = connectionService;
        this.jdbcTemplateProvider = jdbcTemplateProvider;
    }

    public Collection<Table> getTables(long connectionId) {
        ConnectionDetails connectionDetails = connectionService.getConnection(connectionId);
        JdbcTemplate jdbcTemplate = jdbcTemplateProvider.getJdbcTemplate(connectionDetails);

        return getTables(jdbcTemplate, connectionDetails.getDatabaseName());
    }

    private Collection<Table> getTables(JdbcTemplate jdbcTemplate, String databaseName) {

        return jdbcTemplate.query(SELECT_TABLES, ROW_MAPPER, databaseName);
    }

    public Table getOneTable(long connectionId, String tableName) {
        Assert.hasText(tableName, "tableName must not be empty!");

        ConnectionDetails connectionDetails = connectionService.getConnection(connectionId);
        JdbcTemplate jdbcTemplate = jdbcTemplateProvider.getJdbcTemplate(connectionDetails);

        return getOneTable(jdbcTemplate, connectionDetails.getDatabaseName(), tableName)
                .orElseThrow(() -> new DBrowserException("Table " + tableName + " not found in database " + connectionDetails.getDatabaseName(),
                        HttpStatus.NOT_FOUND));
    }

    private Optional<Table> getOneTable(JdbcTemplate jdbcTemplate, String databaseName, String tableName) {
        return jdbcTemplate.query(SELECT_ONE_TABLE, ROW_MAPPER, databaseName, tableName).stream().findAny();
    }

}
