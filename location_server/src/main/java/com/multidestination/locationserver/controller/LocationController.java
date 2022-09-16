package com.multidestination.locationserver.controller;

import com.multidestination.locationserver.domain.Location;
import com.multidestination.locationserver.repository.LocationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@Transactional
public class LocationController {

    @Autowired
    LocationRepository locationRepository;
    private final Logger log = LoggerFactory.getLogger(Transactional.class);

    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_MANAGER')")
    @PostMapping("/location")
    public ResponseEntity<Location> createLocation(@RequestBody Location location) {
        log.debug("REST request to save RestaurantsAndBar : {}", location);
        if (location.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A city cannot already have an ID");
        }
        location.setActive(true);
        Location result = locationRepository.save(location);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_MANAGER')")
    @PutMapping("/location/{id}")
    public ResponseEntity<Location> updateLocation(
            @PathVariable(value = "id", required = false) final Long id,
            @RequestBody Location location
    ) {
        log.debug("REST request to update RestaurantsAndBar : {}, {}", id, location);
        if (location.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid id");
        }

        if (!Objects.equals(id, location.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid id");
        }

        if (!locationRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Entity not found");
        }
        location.setActive(true);
        Location result = locationRepository.save(location);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/location-list")
    public List<Location> getAllLocation() {
        log.debug("REST request to get all location");
        return locationRepository.findAllByIsActive(true);
    }

    @GetMapping("/location/{id}")
    public ResponseEntity<Location> getLocation(@PathVariable Long id) {
        log.debug("REST request to get location : {}", id);
        Optional<Location> restaurantsAndBar = locationRepository.findById(id);
        return ResponseEntity.ok(restaurantsAndBar.get());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/location/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
        log.debug("REST request to delete location : {}", id);
        Optional<Location> location = locationRepository.findById(id);
        location.get().setActive(false);
        return ResponseEntity
                .noContent()
                .build();
    }

}
