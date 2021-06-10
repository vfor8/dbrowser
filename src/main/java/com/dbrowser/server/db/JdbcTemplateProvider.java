package com.dbrowser.server.db;

import com.dbrowser.server.connection.ConnectionDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class JdbcTemplateProvider {

    private final DatasourceProvider datasourceProvider;

    @Autowired
    public JdbcTemplateProvider(DatasourceProvider datasourceProvider) {
        this.datasourceProvider = datasourceProvider;
    }

    public JdbcTemplate getJdbcTemplate(ConnectionDetails connectionDetails) {
        DataSource datasource = datasourceProvider.getDatasource(connectionDetails);

        return new JdbcTemplate(datasource);
    }

}
