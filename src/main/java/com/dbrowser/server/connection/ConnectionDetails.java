package com.dbrowser.server.connection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "connection_details")
public class ConnectionDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "hostname")
    private String hostname;

    @Column(name = "port")
    private int port;

    @Column(name = "database_name")
    private String databaseName;

    @Column(name = "username")
    private String username;

    // TODO encrypt password
    @Column(name = "password")
    private String password;

    public ConnectionDetails() {

    }

    public ConnectionDetails(Long id, String name, String hostname, int port, String databaseName, String username, String password) {
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

    public void copyDetailsFrom(ConnectionDetails other) {
        this.name = other.name;
        this.hostname = other.hostname;
        this.port = other.port;
        this.databaseName = other.databaseName;
        this.username = other.username;
        this.password = other.password;
    }

}
