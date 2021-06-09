package com.dbrowser.server.table;

import com.dbrowser.server.connection.ConnectionDetails;
import com.dbrowser.server.connection.ConnectionDetailsService;
import com.dbrowser.server.error.DBrowserException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
class TableService {

    private static final Logger log = LoggerFactory.getLogger(TableService.class);

    private static final String SELECT_TABLES =
            "SELECT TABLE_NAME, TABLE_TYPE, ENGINE, TABLE_COLLATION, TABLE_COMMENT FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = ?";

    private static final String SELECT_ONE_TABLE = SELECT_TABLES + " AND TABLE_NAME = ?";

    private final ConnectionDetailsService connectionService;

    @Autowired
    TableService(ConnectionDetailsService connectionService) {
        this.connectionService = connectionService;
    }

    Collection<Table> getTables(long connectionId) {
        ConnectionDetails connectionDetails = connectionService.getConnection(connectionId);

        DataSource ds = getDatasource(connectionDetails);

        return getTablesForDatabase(ds, connectionDetails.getDatabaseName());
    }

    private Collection<Table> getTablesForDatabase(DataSource dataSource, String databaseName) {

        try (Connection connection = dataSource.getConnection();
                PreparedStatement select = connection.prepareStatement(SELECT_TABLES)) {

            select.setString(1, databaseName);

            ResultSet resultSet = select.executeQuery();

            return tablesFromResultSet(resultSet);

        } catch (SQLException e) {
            log.error("Database connection error!", e);
            throw new DBrowserException("Internal error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Table getOneTable(long connectionId, String tableName) {
        Assert.hasText(tableName, "tableName must not be empty!");

        ConnectionDetails connectionDetails = connectionService.getConnection(connectionId);

        DataSource ds = getDatasource(connectionDetails);

        return getOneTableForDatabase(ds, connectionDetails.getDatabaseName(), tableName)
                .orElseThrow(() -> new DBrowserException("Table " + tableName + " not found in database " + connectionDetails.getDatabaseName(),
                        HttpStatus.NOT_FOUND));
    }

    private Optional<Table> getOneTableForDatabase(DataSource dataSource, String databaseName, String tableName) {

        try (Connection connection = dataSource.getConnection();
                PreparedStatement select = connection.prepareStatement(SELECT_ONE_TABLE)) {

            select.setString(1, databaseName);
            select.setString(2, tableName);

            ResultSet resultSet = select.executeQuery();

            return tablesFromResultSet(resultSet).stream().findAny();

        } catch (SQLException e) {
            log.error("Database connection error!", e);
            throw new DBrowserException("Internal error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Collection<Table> tablesFromResultSet(ResultSet resultSet) throws SQLException {
        Collection<Table> tables = new ArrayList<>();

        while (resultSet.next()) {
            var t = new Table();
            t.setName(resultSet.getString("TABLE_NAME"));
            t.setType(resultSet.getString("TABLE_TYPE"));
            t.setEngine(resultSet.getString("ENGINE"));
            t.setCollation(resultSet.getString("TABLE_COLLATION"));
            t.setComment(resultSet.getString("TABLE_COMMENT"));

            tables.add(t);
        }

        return tables;
    }

    private DataSource getDatasource(ConnectionDetails connectionDetails) {

        return DataSourceBuilder.create()
                .type(SimpleDriverDataSource.class)
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .url("jdbc:mysql://" + connectionDetails.getHostname() + ":" + connectionDetails.getPort())
                .username(connectionDetails.getUsername())
                .password(connectionDetails.getPassword())
                .build();
    }

}
