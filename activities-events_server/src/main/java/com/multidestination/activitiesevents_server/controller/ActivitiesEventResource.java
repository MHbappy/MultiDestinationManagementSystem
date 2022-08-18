package com.multidestination.activitiesevents_server.controller;

import com.multidestination.activitiesevents_server.domain.Rating;
import com.multidestination.activitiesevents_server.domain.RatingRequest;
import com.multidestination.activitiesevents_server.domain.Reservation;
import com.multidestination.activitiesevents_server.model.ActivitiesEvent;
import com.multidestination.activitiesevents_server.repository.ActivitiesEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
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
public class ActivitiesEventResource {

    private final Logger log = LoggerFactory.getLogger(ActivitiesEventResource.class);
    private final ActivitiesEventRepository activitiesEventRepository;

    public ActivitiesEventResource(ActivitiesEventRepository activitiesEventRepository) {
        this.activitiesEventRepository = activitiesEventRepository;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_MANAGER')")
    @PostMapping("/activities-events")
    public ResponseEntity<ActivitiesEvent> createActivitiesEvent(@RequestBody ActivitiesEvent activitiesEvent) throws URISyntaxException {
        log.debug("REST request to save ActivitiesEvent : {}", activitiesEvent);
        if (activitiesEvent.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A new activitiesEvent cannot already have an ID");
        }
        ActivitiesEvent result = activitiesEventRepository.save(activitiesEvent);
        return ResponseEntity
                .created(new URI("/api/activities-events/" + result.getId()))
                .body(result);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_MANAGER')")
    @PutMapping("/activities-events/{id}")
    public ResponseEntity<ActivitiesEvent> updateActivitiesEvent(
            @PathVariable(value = "id", required = false) final Long id,
            @RequestBody ActivitiesEvent activitiesEvent
    ) {
        log.debug("REST request to update ActivitiesEvent : {}, {}", id, activitiesEvent);
        if (activitiesEvent.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid id");
        }
        if (!Objects.equals(id, activitiesEvent.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid id");
        }

        if (!activitiesEventRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Entity not found");

        }

        ActivitiesEvent result = activitiesEventRepository.save(activitiesEvent);
        return ResponseEntity
                .ok()
                .body(result);
    }

    @GetMapping("/activities-events")
    public List<ActivitiesEvent> getAllActivitiesEvents() {
        log.debug("REST request to get all ActivitiesEvents");
        return activitiesEventRepository.findAll();
    }


    @GetMapping("/activities-events/{id}")
    public ResponseEntity<ActivitiesEvent> getActivitiesEvent(@PathVariable Long id) {
        log.debug("REST request to get ActivitiesEvent : {}", id);
        Optional<ActivitiesEvent> activitiesEvent = activitiesEventRepository.findById(id);
        return ResponseEntity.ok(activitiesEvent.get());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/activities-events/{id}")
    public ResponseEntity<Void> deleteActivitiesEvent(@PathVariable Long id) {
        log.debug("REST request to delete ActivitiesEvent : {}", id);
        activitiesEventRepository.deleteById(id);
        return ResponseEntity
                .noContent()
                .build();
    }

    @PostMapping("/user-reservation")
    public ResponseEntity<Reservation> saveReservation(@RequestBody Reservation reservation){
        reservation.setUserId(getUserIdFromUserName());
        String randomServerPort = "8082";
        final String baseUrl = "http://localhost:"+randomServerPort+"/api/reservations";
        ResponseEntity<Reservation> result = new RestTemplate().postForEntity(baseUrl, reservation, Reservation.class);
        return result;
    }

    @PostMapping("/user-ratings")
    public ResponseEntity<Rating> rating(@RequestBody RatingRequest ratingRequest){
        Rating rating = new Rating();
        rating.setUserId(getUserIdFromUserName());
        rating.setComment(ratingRequest.getComment() != null ? ratingRequest.getComment() : "");
        rating.setScore(ratingRequest.getScore() != null ? ratingRequest.getScore() : 0);
        String randomServerPort = "8083";
        final String baseUrl = "http://localhost:"+randomServerPort+"/api/ratings";
        ResponseEntity<Rating> result = new RestTemplate().postForEntity(baseUrl, rating, Rating.class);
        return result;
    }

    @GetMapping("/activities-events-with-cities")
    public List<ActivitiesEvent> getAllHotelAndAccomodationsByCities() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();

        List<ActivitiesEvent> activitiesEvents = new ArrayList<>();
        List<Long> cityList = activitiesEventRepository.getAllCitiesByUserName(userName);
        if (cityList != null & cityList.size() > 0){
            activitiesEvents = activitiesEventRepository.getAllByCities(cityList);
        }
        return activitiesEvents;
    }

    public Long getUserIdFromUserName(){
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String username = loggedInUser.getName();
        Long userId = activitiesEventRepository.getUserIdByUserName(username);
        return userId;
    }
}
