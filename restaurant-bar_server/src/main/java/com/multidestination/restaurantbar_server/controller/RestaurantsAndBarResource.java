package com.multidestination.restaurantbar_server.controller;

import com.multidestination.restaurantbar_server.model.RestaurantsAndBar;
import com.multidestination.restaurantbar_server.repository.RestaurantsAndBarRepository;
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
public class RestaurantsAndBarResource {

    private final Logger log = LoggerFactory.getLogger(RestaurantsAndBarResource.class);
    private final RestaurantsAndBarRepository restaurantsAndBarRepository;

    public RestaurantsAndBarResource(RestaurantsAndBarRepository restaurantsAndBarRepository) {
        this.restaurantsAndBarRepository = restaurantsAndBarRepository;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_MANAGER')")
    @PostMapping("/restaurants-and-bars")
    public ResponseEntity<RestaurantsAndBar> createRestaurantsAndBar(@RequestBody RestaurantsAndBar restaurantsAndBar)
        throws URISyntaxException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();

        log.debug("REST request to save RestaurantsAndBar : {}", restaurantsAndBar);
        if (restaurantsAndBar.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A new activitiesEvent cannot already have an ID");
        }

        restaurantsAndBar.setIsApproved(false);
        restaurantsAndBar.setCreatedBy(userName);
        restaurantsAndBar.setIsActive(true);
        RestaurantsAndBar result = restaurantsAndBarRepository.save(restaurantsAndBar);
        return ResponseEntity
            .created(new URI("/api/restaurants-and-bars/" + result.getId()))
            .body(result);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_MANAGER')")
    @PutMapping("/restaurants-and-bars/{id}")
    public ResponseEntity<RestaurantsAndBar> updateRestaurantsAndBar(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RestaurantsAndBar restaurantsAndBar
    ) throws URISyntaxException {

        log.debug("REST request to update RestaurantsAndBar : {}, {}", id, restaurantsAndBar);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();

        if (restaurantsAndBar.getCreatedBy() != null && !restaurantsAndBar.getCreatedBy().equals(userName)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are not permitted for edit!");
        }


        if (restaurantsAndBar.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid id");
        }
        if (!Objects.equals(id, restaurantsAndBar.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid id");
        }

        if (!restaurantsAndBarRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Entity not found");
        }

        restaurantsAndBar.setIsActive(true);
        RestaurantsAndBar result = restaurantsAndBarRepository.save(restaurantsAndBar);
        return ResponseEntity
            .ok()
            .body(result);
    }

    @GetMapping("/restaurants-and-bars")
    public List<RestaurantsAndBar> getAllRestaurantsAndBars() {
        log.debug("REST request to get all RestaurantsAndBars");

//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String userName = auth.getName();

//        Set<String> roles = auth.getAuthorities().stream().map(r -> r.getAuthority()).collect(Collectors.toSet());
//        Boolean isAdminOrManager = (roles.contains("ROLE_ADMIN") || roles.contains("ROLE_MANAGER"));

//        if (userName.equals("anonymousUser") || isAdminOrManager){
//            return restaurantsAndBarRepository.findAllByIsActive(true);
//        }

        List<RestaurantsAndBar> restaurantsAndBars = new ArrayList<>();
        List<Long> cityList = restaurantsAndBarRepository.getAllLocationByDestination();
        if (cityList != null & cityList.size() > 0){
            restaurantsAndBars = restaurantsAndBarRepository.getAllByLocation(true, cityList);
        }
        return restaurantsAndBars;
    }

    @GetMapping("/restaurants-and-bars/{id}")
    public ResponseEntity<RestaurantsAndBar> getRestaurantsAndBar(@PathVariable Long id) {
        log.debug("REST request to get RestaurantsAndBar : {}", id);
        Optional<RestaurantsAndBar> restaurantsAndBar = restaurantsAndBarRepository.findById(id);
        return ResponseEntity.ok(restaurantsAndBar.get());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/restaurants-and-bars/{id}")
    public ResponseEntity<Boolean> deleteRestaurantsAndBar(@PathVariable Long id) {
        log.debug("REST request to delete RestaurantsAndBar : {}", id);

        Optional<RestaurantsAndBar> restaurantsAndBar = restaurantsAndBarRepository.findById(id);
        restaurantsAndBar.get().setIsActive(false);
        restaurantsAndBarRepository.save(restaurantsAndBar.get());
        return ResponseEntity.ok(true);
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/restaurants-and-bars/approve/{id}")
    public ResponseEntity<Boolean> approveRestaurantsAndBar(@PathVariable Long id) {
        log.debug("REST request to delete RestaurantsAndBar : {}", id);

        Optional<RestaurantsAndBar> restaurantsAndBar = restaurantsAndBarRepository.findById(id);
        restaurantsAndBar.get().setIsApproved(false);
        restaurantsAndBarRepository.save(restaurantsAndBar.get());
        return ResponseEntity.ok(true);
    }

    @GetMapping("/restaurants-and-bars-with-location")
    public List<RestaurantsAndBar> getAllHotelAndAccomodationsByLocation() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();

        List<RestaurantsAndBar> restaurantsAndBars = new ArrayList<>();
        List<Long> locationList = restaurantsAndBarRepository.getAllLocationByDestination();
        if (locationList != null & locationList.size() > 0){
            restaurantsAndBars = restaurantsAndBarRepository.getAllByLocation(true, locationList);
        }
        return restaurantsAndBars;
    }


}
