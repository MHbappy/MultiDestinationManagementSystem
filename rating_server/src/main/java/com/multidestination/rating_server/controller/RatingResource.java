package com.multidestination.rating_server.controller;

import com.multidestination.rating_server.model.Rating;
import com.multidestination.rating_server.repository.RatingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@Transactional
public class RatingResource {

    private final Logger log = LoggerFactory.getLogger(RatingResource.class);
    private final RatingRepository ratingRepository;

    public RatingResource(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    @PostMapping("/ratings")
    public ResponseEntity<Rating> createRating(@RequestBody Rating rating) throws URISyntaxException {
        log.debug("REST request to save Rating : {}", rating);
        if (rating.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A new activitiesEvent cannot already have an ID");
        }
        Rating result = ratingRepository.save(rating);
        return ResponseEntity
            .created(new URI("/api/ratings/" + result.getId()))
            .body(result);
    }

    @PutMapping("/ratings/{id}")
    public ResponseEntity<Rating> updateRating(@PathVariable(value = "id", required = false) final Long id, @RequestBody Rating rating)
        throws URISyntaxException {
        log.debug("REST request to update Rating : {}, {}", id, rating);
        if (rating.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid id");
        }
        if (!Objects.equals(id, rating.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid id");
        }

        if (!ratingRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Entity not found");
        }

        Rating result = ratingRepository.save(rating);
        return ResponseEntity
            .ok()
            .body(result);
    }


    @GetMapping("/ratings")
    public List<Rating> getAllRatings() {
        log.debug("REST request to get all Ratings");
        return ratingRepository.findAll();
    }


    @GetMapping("/ratings-by-reservation-id/{reservationId}")
    public List<Rating> getAllRatingsBy(@PathVariable Long reservationId) {
        return ratingRepository.findAllByReservationId(reservationId);
    }

    @GetMapping("/ratings/{id}")
    public ResponseEntity<Rating> getRating(@PathVariable Long id) {
        log.debug("REST request to get Rating : {}", id);
        Optional<Rating> rating = ratingRepository.findById(id);
        return ResponseEntity.ok(rating.get());
    }

    @DeleteMapping("/ratings/{id}")
    public ResponseEntity<Void> deleteRating(@PathVariable Long id) {
        log.debug("REST request to delete Rating : {}", id);
        ratingRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .build();
    }
}
