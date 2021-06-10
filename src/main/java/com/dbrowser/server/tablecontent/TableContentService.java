package com.dbrowser.server.tablecontent;

import com.dbrowser.server.connection.ConnectionDetails;
import com.dbrowser.server.connection.ConnectionDetailsService;
import com.dbrowser.server.db.JdbcTemplateProvider;
import com.dbrowser.server.table.TableService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
class TableContentService {

    private static final int DEFAULT_ROW_LIMIT = 50;

    private static final String QUERY = "SELECT * from %s LIMIT %d";

    private static final ResultSetExtractor<TableContent> EXTRACTOR = new ContentExtractor();

    private final ConnectionDetailsService connectionService;
    private final TableService tableService;
    private final JdbcTemplateProvider jdbcTemplateProvider;

    @Autowired
    TableContentService(ConnectionDetailsService connectionService, TableService tableService, JdbcTemplateProvider jdbcTemplateProvider) {
        this.connectionService = connectionService;
        this.tableService = tableService;
        this.jdbcTemplateProvider = jdbcTemplateProvider;
    }

    TableContent getTableContent(long connectionId, String tableName) {
        // tableName comes from client input - check if exists before we use it to construct the query
        checkTableExists(connectionId, tableName);

        ConnectionDetails connectionDetails = connectionService.getConnection(connectionId);
        JdbcTemplate jdbcTemplate = jdbcTemplateProvider.getJdbcTemplate(connectionDetails);

        return getTableContent(jdbcTemplate, tableName);
    }

    private void checkTableExists(long connectionId, String tableName) {
        // throws NOT_FOUND exception if table doesn't exists
        tableService.getOneTable(connectionId, tableName);
    }

    private TableContent getTableContent(JdbcTemplate jdbcTemplate, String tableName) {

        var query = String.format(QUERY, tableName, DEFAULT_ROW_LIMIT);

        return jdbcTemplate.query(query, EXTRACTOR);
    }

    private static class ContentExtractor implements ResultSetExtractor<TableContent> {

        @Override
        public TableContent extractData(ResultSet rs) throws SQLException, DataAccessException {
            int columnCount = rs.getMetaData().getColumnCount();

            var content = new TableContent();

            while (rs.next()) {
                List<String> values = new ArrayList<>(columnCount);
                for (int i = 1; i <= columnCount; i++) {
                    values.add(rs.getString(i));
                }
                content.addRow(values);
            }

            return content;
        }
    }

}
