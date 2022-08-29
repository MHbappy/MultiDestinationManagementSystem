package com.multidestination.nontourist_server.controller;

import com.multidestination.nontourist_server.model.NonTourist;
import com.multidestination.nontourist_server.repository.NonTouristRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@Transactional
public class NonTouristResource {

    private final Logger log = LoggerFactory.getLogger(NonTouristResource.class);

    private final NonTouristRepository nonTouristRepository;

    public NonTouristResource(NonTouristRepository nonTouristRepository) {
        this.nonTouristRepository = nonTouristRepository;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_MANAGER')")
    @PostMapping("/non-tourists")
    public ResponseEntity<NonTourist> createNonTourist(@RequestBody NonTourist nonTourist) throws URISyntaxException {
        log.debug("REST request to save NonTourist : {}", nonTourist);
        if (nonTourist.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A new activitiesEvent cannot already have an ID");
        }
        NonTourist result = nonTouristRepository.save(nonTourist);
        return ResponseEntity
            .created(new URI("/api/non-tourists/" + result.getId()))
            .body(result);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_MANAGER')")
    @PutMapping("/non-tourists/{id}")
    public ResponseEntity<NonTourist> updateNonTourist(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody NonTourist nonTourist
    ) throws URISyntaxException {
        log.debug("REST request to update NonTourist : {}, {}", id, nonTourist);
        if (nonTourist.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid id");
        }
        if (!Objects.equals(id, nonTourist.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid id");
        }

        if (!nonTouristRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Entity not found");
        }

        NonTourist result = nonTouristRepository.save(nonTourist);
        return ResponseEntity
            .ok()
            .body(result);
    }

    @GetMapping("/non-tourists")
    public List<NonTourist> getAllNonTourists() {
        log.debug("REST request to get all NonTourists");
        return nonTouristRepository.findAll();
    }

    @GetMapping("/non-tourists/{id}")
    public ResponseEntity<NonTourist> getNonTourist(@PathVariable Long id) {
        log.debug("REST request to get NonTourist : {}", id);
        Optional<NonTourist> nonTourist = nonTouristRepository.findById(id);
        return ResponseEntity.ok(nonTourist.get());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/non-tourists/{id}")
    public ResponseEntity<Boolean> deleteNonTourist(@PathVariable Long id) {
        log.debug("REST request to delete NonTourist : {}", id);
        nonTouristRepository.deleteById(id);
        return ResponseEntity.ok(true);
    }


    @GetMapping("/non-tourists-with-cities")
    public List<NonTourist> getAllHotelAndAccomodationsByCities() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();

        List<NonTourist> restaurantsAndBars = new ArrayList<>();
        List<Long> cityList = nonTouristRepository.getAllCitiesByUserName(userName);
        if (cityList != null & cityList.size() > 0){
            restaurantsAndBars = nonTouristRepository.getAllByCities(cityList);
        }
        return restaurantsAndBars;
    }
}
