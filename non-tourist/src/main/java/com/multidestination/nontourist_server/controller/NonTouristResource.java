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
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@Transactional
public class NonTouristResource {

    private final Logger log = LoggerFactory.getLogger(NonTouristResource.class);

    private final NonTouristRepository nonTouristRepository;

    public NonTouristResource(NonTouristRepository nonTouristRepository) {
        this.nonTouristRepository = nonTouristRepository;
    }

//    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_MANAGER')")
    @PostMapping("/non-tourists")
    public ResponseEntity<NonTourist> createNonTourist(@RequestBody NonTourist nonTourist) throws URISyntaxException {
        log.debug("REST request to save NonTourist : {}", nonTourist);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();

        if (nonTourist.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A new activitiesEvent cannot already have an ID");
        }

        nonTourist.setCreatedBy(userName);
        nonTourist.setIsApproved(false);
        nonTourist.setIsActive(true);
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
    ) {
        log.debug("REST request to update NonTourist : {}, {}", id, nonTourist);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();

        if (nonTourist.getCreatedBy() != null && !nonTourist.getCreatedBy().equals(userName)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are not permitted for edit!");
        }

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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        Set<String> roles = auth.getAuthorities().stream().map(r -> r.getAuthority()).collect(Collectors.toSet());
        Boolean isAdminOrManager = (roles.contains("ROLE_ADMIN") || roles.contains("ROLE_MANAGER"));

        if (userName.equals("anonymousUser") || isAdminOrManager){
            return nonTouristRepository.findAllByIsActive(true);
        }

        List<NonTourist> tourists = new ArrayList<>();
        List<Long> locationList = nonTouristRepository.getAllLocationByDestination();
        if (locationList != null & locationList.size() > 0){
            tourists = nonTouristRepository.getAllByLocation(true, locationList);
        }
        return tourists;
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
//        nonTouristRepository.deleteById(id);

        Optional<NonTourist> nonTourist = nonTouristRepository.findById(id);
        nonTourist.get().setIsActive(false);
        nonTouristRepository.save(nonTourist.get());
        return ResponseEntity.ok(true);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/non-tourists/approve/{id}")
    public ResponseEntity<Boolean> approveNonTourist(@PathVariable Long id) {
        log.debug("REST request to delete NonTourist : {}", id);
        Optional<NonTourist> nonTourist = nonTouristRepository.findById(id);
        nonTourist.get().setIsApproved(true);
        nonTouristRepository.save(nonTourist.get());
        return ResponseEntity.ok(true);
    }


    @GetMapping("/non-tourists-with-destination")
    public List<NonTourist> getAllHotelAndAccomodationsByLocations() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();

        List<NonTourist> restaurantsAndBars = new ArrayList<>();
        List<Long> destinationList = nonTouristRepository.getAllLocationByDestination();
        if (destinationList != null & destinationList.size() > 0){
            restaurantsAndBars = nonTouristRepository.getAllByLocation(true, destinationList);
        }
        return restaurantsAndBars;
    }
}
