package com.dbrowser.server.connection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConnectionDetailsRepository extends JpaRepository<ConnectionDetails, Long> {

}
