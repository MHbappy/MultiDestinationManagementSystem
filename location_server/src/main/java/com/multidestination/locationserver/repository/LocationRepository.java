package com.multidestination.locationserver.repository;

import com.multidestination.locationserver.domain.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findAllByIsActive(Boolean isActive);
}
