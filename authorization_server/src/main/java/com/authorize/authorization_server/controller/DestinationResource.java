package com.authorize.authorization_server.controller;

import com.authorize.authorization_server.domain.Destination;
import com.authorize.authorization_server.repository.DestinationRepository;
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
public class DestinationResource {
    @Autowired
    DestinationRepository destinationRepository;

    private final Logger log = LoggerFactory.getLogger(Transactional.class);

    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_MANAGER')")
    @PostMapping("/destination")
    public ResponseEntity<Destination> createdestination(@RequestBody Destination destination) {
        log.debug("REST request to save RestaurantsAndBar : {}", destination);
        if (destination.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A city cannot already have an ID");
        }
        Destination destination1 = destinationRepository.findByName(destination.getName());
        if (destination1 != null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Duplicate destination");
        }

        destination.setActive(true);
        Destination result = destinationRepository.save(destination);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_MANAGER')")
    @PutMapping("/destination/{id}")
    public ResponseEntity<Destination> updatedestination(
            @PathVariable(value = "id", required = false) final Long id,
            @RequestBody Destination destination
    ) {
        log.debug("REST request to update RestaurantsAndBar : {}, {}", id, destination);
        if (destination.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid id");
        }

        if (!Objects.equals(id, destination.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid id");
        }

        if (!destinationRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Entity not found");
        }
        destination.setActive(true);
        Destination result = destinationRepository.save(destination);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/destination-list")
    public List<Destination> getAlldestination() {
        log.debug("REST request to get all destination");
        return destinationRepository.findAllByIsActive(true);
    }

    @GetMapping("/destination/{id}")
    public ResponseEntity<Destination> getdestination(@PathVariable Long id) {
        log.debug("REST request to get destination : {}", id);
        Optional<Destination> restaurantsAndBar = destinationRepository.findById(id);
        return ResponseEntity.ok(restaurantsAndBar.get());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/destination/{id}")
    public ResponseEntity<Void> deletedestination(@PathVariable Long id) {
        log.debug("REST request to delete destination : {}", id);
        Optional<Destination> destination = destinationRepository.findById(id);
        destination.get().setActive(false);
        return ResponseEntity
                .noContent()
                .build();
    }


}
