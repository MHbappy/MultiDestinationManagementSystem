package com.authorize.authorization_server.repository;

import com.authorize.authorization_server.domain.Destination;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DestinationRepository extends JpaRepository<Destination, Long> {
    List<Destination> findAllByIsActive(Boolean isActive);
    Destination findByName(String name);
}
