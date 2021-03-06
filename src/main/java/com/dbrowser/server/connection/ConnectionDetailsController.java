package com.dbrowser.server.connection;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;

import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/connections")
public class ConnectionDetailsController {

    private final ConnectionDetailsService service;

    private final ConnectionDetailsMapper mapper = new ConnectionDetailsMapper();

    @Autowired
    public ConnectionDetailsController(ConnectionDetailsService service) {
        this.service = service;
    }

    @Operation(summary = "Get all connections")
    @GetMapping
    public ResponseEntity<Collection<ConnectionDetailsDto>> getAllConnections() {
        Collection<ConnectionDetailsDto> allConnections = service.getAllConnections().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(allConnections);
    }

    @Operation(summary = "Get a connection by id")
    @GetMapping("/{id}")
    public ResponseEntity<ConnectionDetailsDto> getConnection(@PathVariable long id) {
        ConnectionDetailsDto connection = mapper.toDto(service.getConnection(id));
        return ResponseEntity.ok(connection);
    }

    @Operation(summary = "Create a new connection using the provided details")
    @PostMapping
    public ResponseEntity<ConnectionDetailsDto> createConnection(@RequestBody @Valid ConnectionDetailsDto connection) {

        ConnectionDetails created = service.createConnection(mapper.toEntity(connection));
        ConnectionDetailsDto dto = mapper.toDto(created);

        return ResponseEntity.created(
                ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(dto.getId())
                        .toUri())
                .body(dto);
    }

    @Operation(summary = "Update a connection")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateConnection(@PathVariable long id, @RequestBody @Valid ConnectionDetailsDto connection) {
        service.updateConnection(id, mapper.toEntity(connection));
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Remove a connection")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConnection(@PathVariable long id) {
        service.deleteConnection(id);
        return ResponseEntity.ok().build();
    }

}
