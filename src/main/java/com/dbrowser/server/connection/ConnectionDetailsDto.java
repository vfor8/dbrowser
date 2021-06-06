package com.dbrowser.server.connection;

import javax.validation.constraints.Size;

public class ConnectionDetailsDto {

    private Long id;

    @Size(min = 1, max = 150)
    private String name;

    @Size(min = 1, max = 300)
    private String hostname;

    private int port;

    @Size(min = 1, max = 250)
    private String databaseName;

    @Size(min = 1, max = 30)
    private String username;

    @Size(max = 40)
    private String password;

    public ConnectionDetailsDto() {
    }

    public ConnectionDetailsDto(Long id, String name, String hostname, int port, String databaseName, String username, String password) {
        this.id = id;
        this.name = name;
        this.hostname = hostname;
        this.port = port;
        this.databaseName = databaseName;
        this.username = username;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
