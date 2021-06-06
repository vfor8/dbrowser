package com.dbrowser.server.connection;

class ConnectionDetailsMapper {

    ConnectionDetails toEntity(ConnectionDetailsDto dto) {
        var entity = new ConnectionDetails();

        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setDatabaseName(dto.getDatabaseName());
        entity.setHostname(dto.getHostname());
        entity.setPort(dto.getPort());
        entity.setUsername(dto.getUsername());
        entity.setPassword(dto.getPassword());

        return entity;
    }

    ConnectionDetailsDto toDto(ConnectionDetails entity) {
        var dto = new ConnectionDetailsDto();

        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDatabaseName(entity.getDatabaseName());
        dto.setHostname(entity.getHostname());
        dto.setPort(entity.getPort());
        dto.setUsername(entity.getUsername());
        dto.setPassword(entity.getPassword());

        return dto;
    }
}
