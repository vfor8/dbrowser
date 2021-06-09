package com.dbrowser.server.connection;

import com.dbrowser.server.error.DBrowserException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.Collection;
import java.util.Objects;

@Service
public class ConnectionDetailsService {

    private final ConnectionDetailsRepository repository;

    @Autowired
    ConnectionDetailsService(ConnectionDetailsRepository repository) {
        this.repository = repository;
    }

    Collection<ConnectionDetails> getAllConnections() {
        return repository.findAll();
    }

    public ConnectionDetails getConnection(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new DBrowserException("Connection with id " + id + " not found!", HttpStatus.NOT_FOUND));
    }

    @Transactional
    ConnectionDetails createConnection(ConnectionDetails connection) {
        Objects.requireNonNull(connection);
        return repository.save(connection);
    }

    @Transactional
    void updateConnection(long id, ConnectionDetails update) {
        Objects.requireNonNull(update);

        ConnectionDetails fromDb = getConnection(id);
        fromDb.copyDetailsFrom(update);

        repository.save(fromDb);
    }

    @Transactional
    void deleteConnection(long id) {
        repository.deleteById(id);
    }

}
