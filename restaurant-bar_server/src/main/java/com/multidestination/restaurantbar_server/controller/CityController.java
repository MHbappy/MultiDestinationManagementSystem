package com.multidestination.restaurantbar_server.controller;

import com.multidestination.restaurantbar_server.model.Cities;
import com.multidestination.restaurantbar_server.repository.CityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@Transactional
public class CityController {

    @Autowired
    CityRepository cityRepository;

    private final Logger log = LoggerFactory.getLogger(RestaurantsAndBarResource.class);

    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_MANAGER')")
    @PostMapping("/cities")
    public ResponseEntity<Cities> createRestaurantsAndBar(@RequestBody Cities cities) {
        log.debug("REST request to save RestaurantsAndBar : {}", cities);
        if (cities.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A city cannot already have an ID");
        }
        Cities result = cityRepository.save(cities);
        return ResponseEntity.ok(result);
    }


    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_MANAGER')")
    @PutMapping("/cities/{id}")
    public ResponseEntity<Cities> updateCities(
            @PathVariable(value = "id", required = false) final Long id,
            @RequestBody Cities cities
    ) throws URISyntaxException {
        log.debug("REST request to update RestaurantsAndBar : {}, {}", id, cities);
        if (cities.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid id");
        }
        if (!Objects.equals(id, cities.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid id");
        }

        if (!cityRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Entity not found");
        }

        Cities result = cityRepository.save(cities);
        return ResponseEntity
                .ok()
                .body(result);
    }

    @GetMapping("/cities-list")
    public List<Cities> getAllCities() {
        log.debug("REST request to get all cities");
        return cityRepository.findAll();
    }

    @GetMapping("/cities/{id}")
    public ResponseEntity<Cities> getRestaurantsAndBar(@PathVariable Long id) {
        log.debug("REST request to get cities : {}", id);
        Optional<Cities> restaurantsAndBar = cityRepository.findById(id);
        return ResponseEntity.ok(restaurantsAndBar.get());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/cities/{id}")
    public ResponseEntity<Void> deleteRestaurantsAndBar(@PathVariable Long id) {
        log.debug("REST request to delete RestaurantsAndBar : {}", id);
        Optional<Cities> restaurantsAndBar = cityRepository.findById(id);
        restaurantsAndBar.get().setIsActive(false);
//        cityRepository.deleteById(id);
        return ResponseEntity
                .noContent()
                .build();
    }


}
