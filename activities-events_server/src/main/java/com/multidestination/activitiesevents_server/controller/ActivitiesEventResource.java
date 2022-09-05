package com.multidestination.activitiesevents_server.controller;

import com.multidestination.activitiesevents_server.domain.Rating;
import com.multidestination.activitiesevents_server.domain.RatingRequest;
import com.multidestination.activitiesevents_server.domain.Reservation;
import com.multidestination.activitiesevents_server.model.ActivitiesEvent;
import com.multidestination.activitiesevents_server.model.BackGroundImage;
import com.multidestination.activitiesevents_server.repository.ActivitiesEventRepository;
import com.multidestination.activitiesevents_server.repository.BackGroundImageRepository;
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
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@Transactional
public class ActivitiesEventResource {

    private final Logger log = LoggerFactory.getLogger(ActivitiesEventResource.class);
    private final ActivitiesEventRepository activitiesEventRepository;
    private final BackGroundImageRepository backGroundImageRepository;

    public ActivitiesEventResource(ActivitiesEventRepository activitiesEventRepository, BackGroundImageRepository backGroundImageRepository) {
        this.activitiesEventRepository = activitiesEventRepository;
        this.backGroundImageRepository = backGroundImageRepository;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_MANAGER')")
    @PostMapping("/activities-events")
    public ResponseEntity<ActivitiesEvent> createActivitiesEvent(@RequestBody ActivitiesEvent activitiesEvent) throws URISyntaxException {
        log.debug("REST request to save ActivitiesEvent : {}", activitiesEvent);
        if (activitiesEvent.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A new activitiesEvent cannot already have an ID");
        }
        activitiesEvent.setIsActive(true);
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
        activitiesEvent.setIsActive(true);
        ActivitiesEvent result = activitiesEventRepository.save(activitiesEvent);
        return ResponseEntity
                .ok()
                .body(result);
    }

    @GetMapping("/activities-events")
    public List<ActivitiesEvent> getAllActivitiesEvents() {
        log.debug("REST request to get all ActivitiesEvents");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        Set<String> roles = auth.getAuthorities().stream().map(r -> r.getAuthority()).collect(Collectors.toSet());
        Boolean isAdminOrManager = (roles.contains("ROLE_ADMIN") || roles.contains("ROLE_MANAGER"));

        if (userName.equals("anonymousUser") || isAdminOrManager){
            return activitiesEventRepository.findAllByIsActive(true);
        }

        List<ActivitiesEvent> activitiesEvents = new ArrayList<>();
        List<Long> cityList = activitiesEventRepository.getAllCitiesByUserName(true, userName);
        if (cityList != null & cityList.size() > 0){
            activitiesEvents = activitiesEventRepository.getAllByCities(true, cityList);
        }
        return activitiesEvents;
    }


    @GetMapping("/activities-events/{id}")
    public ResponseEntity<ActivitiesEvent> getActivitiesEvent(@PathVariable Long id) {
        log.debug("REST request to get ActivitiesEvent : {}", id);
        Optional<ActivitiesEvent> activitiesEvent = activitiesEventRepository.findById(id);
        return ResponseEntity.ok(activitiesEvent.get());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/activities-events/{id}")
    public ResponseEntity<Boolean> deleteActivitiesEvent(@PathVariable Long id) {
        log.debug("REST request to delete ActivitiesEvent : {}", id);
        Optional<ActivitiesEvent> activitiesEvent = activitiesEventRepository.findById(id);
        activitiesEvent.get().setIsActive(false);
        activitiesEventRepository.save(activitiesEvent.get());
        return ResponseEntity.ok(true);
    }

    @PostMapping("/user-reservation")
    public ResponseEntity<Reservation> saveReservation(@RequestBody Reservation reservation){
        reservation.setUserId(getUserIdFromUserName());
        String randomServerPort = "8082";
        final String baseUrl = "http://localhost:"+randomServerPort+"/api/reservations";
        ResponseEntity<Reservation> result = new RestTemplate().postForEntity(baseUrl, reservation, Reservation.class);
        return result;
    }

    @GetMapping("/all-reservation-by-user-id")
    public List<Reservation> getAllReservationByUserId(@RequestParam(required = false) Long userId){
//        reservation.setUserId(getUserIdFromUserName());
        Long finalUserId = 0l;
        if (userId == null || userId == 0){
            finalUserId = getUserIdFromUserName();
        }else {
            finalUserId = userId;
        }

        String randomServerPort = "8082";
        final String baseUrl = "http://localhost:"+randomServerPort+"/api/reservations-by-user-id/" + finalUserId;
        Reservation[] result = new RestTemplate().getForObject(baseUrl, Reservation[].class);
        List<Reservation> reservations= Arrays.asList(result);
        return reservations;
    }

//    List<Reservation>

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


    @GetMapping("/ratings-by-reservation-id/{reservationId}")
    public List<Rating> getAllRatingsByReservation(@PathVariable Long reservationId){
//        reservation.setUserId(getUserIdFromUserName());
        String randomServerPort = "8083";
        final String baseUrl = "http://localhost:"+randomServerPort+"/api/ratings-by-reservation-id/" + reservationId;
        Rating[] result = new RestTemplate().getForObject(baseUrl, Rating[].class);
        List<Rating> reservations= Arrays.asList(result);
        return reservations;
    }

    @GetMapping("/activities-events-with-cities")
    public List<ActivitiesEvent> getAllHotelAndAccomodationsByCities() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();

        List<ActivitiesEvent> activitiesEvents = new ArrayList<>();
        List<Long> cityList = activitiesEventRepository.getAllCitiesByUserName(true, userName);
        if (cityList != null & cityList.size() > 0){
            activitiesEvents = activitiesEventRepository.getAllByCities(true, cityList);
        }
        return activitiesEvents;
    }

    @PostMapping("/save-image")
    public BackGroundImage saveBackGroundImage(BackGroundImage backGroundImage){
        backGroundImage.setId(1l);
        return backGroundImageRepository.save(backGroundImage);
    }

    @GetMapping("/get-image")
    public Optional<BackGroundImage> getImages(){
        return backGroundImageRepository.findById(1l);
    }

    public Long getUserIdFromUserName(){
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String username = loggedInUser.getName();
        Long userId = activitiesEventRepository.getUserIdByUserName(username);
        return userId;
    }



}
