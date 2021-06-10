package com.dbrowser.server.column;

import com.dbrowser.server.connection.ConnectionDetails;
import com.dbrowser.server.connection.ConnectionDetailsService;
import com.dbrowser.server.db.JdbcTemplateProvider;
import com.dbrowser.server.error.DBrowserException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

@Service
class ColumnService {

    private static final Logger log = LoggerFactory.getLogger(ColumnService.class);

    private static final String SELECT_COLUMNS =
            "SELECT COLUMN_NAME, COLUMN_DEFAULT, IS_NULLABLE, CHARACTER_SET_NAME, COLLATION_NAME, COLUMN_TYPE, COLUMN_KEY, EXTRA, COLUMN_COMMENT"
                    + " FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = ? AND TABLE_NAME = ?";

    private static final String SELECT_ONE_COLUMN =
            SELECT_COLUMNS + " AND COLUMN_NAME = ?";

    private static final RowMapper<Column> ROW_MAPPER = new ColumnRowMapper();

    private final ConnectionDetailsService connectionService;
    private final JdbcTemplateProvider jdbcTemplateProvider;

    @Autowired
    ColumnService(ConnectionDetailsService connectionService, JdbcTemplateProvider jdbcTemplateProvider) {
        this.connectionService = connectionService;
        this.jdbcTemplateProvider = jdbcTemplateProvider;
    }

    Collection<Column> getColumns(long connectionId, String tableName) {
        ConnectionDetails connectionDetails = connectionService.getConnection(connectionId);
        JdbcTemplate jdbcTemplate = jdbcTemplateProvider.getJdbcTemplate(connectionDetails);

        return getColumns(jdbcTemplate, connectionDetails.getDatabaseName(), tableName);
    }

    private Collection<Column> getColumns(JdbcTemplate jdbcTemplate, String databaseName, String tableName) {

        return jdbcTemplate.query(SELECT_COLUMNS, ROW_MAPPER, databaseName, tableName);
    }

    Column getOneColumn(long connectionId, String tableName, String columnName) {
        ConnectionDetails connectionDetails = connectionService.getConnection(connectionId);
        JdbcTemplate jdbcTemplate = jdbcTemplateProvider.getJdbcTemplate(connectionDetails);

        return getOneColumn(jdbcTemplate, connectionDetails.getDatabaseName(), tableName, columnName).stream().findAny()
                .orElseThrow(() -> new DBrowserException("Column " + columnName + " in table " + tableName + " not found", HttpStatus.NOT_FOUND));
    }

    private Optional<Column> getOneColumn(JdbcTemplate jdbcTemplate, String databaseName, String tableName, String columnName) {

        return jdbcTemplate.query(SELECT_ONE_COLUMN, ROW_MAPPER, databaseName, tableName, columnName).stream().findAny();
    }

    private static class ColumnRowMapper implements RowMapper<Column> {

        @Override
        public Column mapRow(ResultSet rs, int rowNum) throws SQLException {
            var c = new Column();

            c.setName(rs.getString("COLUMN_NAME"));
            c.setDatatype(rs.getString("COLUMN_TYPE"));
            c.setDefaultValue(rs.getString("COLUMN_DEFAULT"));
            c.setNullable(yesNoToBoolean(rs.getString("IS_NULLABLE")));
            c.setCharset(rs.getString("CHARACTER_SET_NAME"));
            c.setCollation(rs.getString("COLLATION_NAME"));
            c.setIndexType(strToIndexType(rs.getString("COLUMN_KEY")));
            c.setComment(rs.getString("COLUMN_COMMENT"));
            c.setAdditionalInfo(rs.getString("EXTRA"));

            return c;
        }

        private Boolean yesNoToBoolean(String str) {
            if ("YES".equalsIgnoreCase(str)) {
                return Boolean.TRUE;
            } else if ("NO".equalsIgnoreCase(str)) {
                return Boolean.FALSE;
            } else {
                log.warn("Column mapping: Expected YES/NO value, got: " + str);
                return null;
            }
        }

        private Column.ColumnIndexType strToIndexType(String str) {
            return Column.ColumnIndexType.fromDbCode(str).orElse(null);
        }

    }

}
