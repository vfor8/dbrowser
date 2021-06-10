package com.dbrowser.server.db;

import com.dbrowser.server.connection.ConnectionDetails;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class DatasourceProvider {

    public DataSource getDatasource(ConnectionDetails connectionDetails) {

        return DataSourceBuilder.create()
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .type(SimpleDriverDataSource.class)
                .url("jdbc:mysql://" + connectionDetails.getHostname() + ":" + connectionDetails.getPort())
                .username(connectionDetails.getUsername())
                .password(connectionDetails.getPassword())
                .build();
    }

}
