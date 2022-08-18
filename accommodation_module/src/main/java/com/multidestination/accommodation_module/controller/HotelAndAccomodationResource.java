package com.multidestination.accommodation_module.controller;

import com.multidestination.accommodation_module.model.HotelAndAccomodation;
import com.multidestination.accommodation_module.repository.HotelAndAccomodationRepository;
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
public class HotelAndAccomodationResource {
    private final Logger log = LoggerFactory.getLogger(HotelAndAccomodationResource.class);

    private final HotelAndAccomodationRepository hotelAndAccomodationRepository;

    public HotelAndAccomodationResource(HotelAndAccomodationRepository hotelAndAccomodationRepository) {
        this.hotelAndAccomodationRepository = hotelAndAccomodationRepository;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_MANAGER')")
    @PostMapping("/hotel-and-accomodations")
    public ResponseEntity<HotelAndAccomodation> createHotelAndAccomodation(@RequestBody HotelAndAccomodation hotelAndAccomodation)
            throws URISyntaxException {
        log.debug("REST request to save HotelAndAccomodation : {}", hotelAndAccomodation);
        if (hotelAndAccomodation.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A new activitiesEvent cannot already have an ID");
        }
        HotelAndAccomodation result = hotelAndAccomodationRepository.save(hotelAndAccomodation);
        return ResponseEntity
                .created(new URI("/api/hotel-and-accomodations/" + result.getId()))
                .body(result);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_MANAGER')")
    @PutMapping("/hotel-and-accomodations/{id}")
    public ResponseEntity<HotelAndAccomodation> updateHotelAndAccomodation(
            @PathVariable(value = "id", required = false) final Long id,
            @RequestBody HotelAndAccomodation hotelAndAccomodation
    ) throws URISyntaxException {
        log.debug("REST request to update HotelAndAccomodation : {}, {}", id, hotelAndAccomodation);
        if (hotelAndAccomodation.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid id");
        }
        if (!Objects.equals(id, hotelAndAccomodation.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid id");
        }
        if (!hotelAndAccomodationRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Entity not found");
        }

        HotelAndAccomodation result = hotelAndAccomodationRepository.save(hotelAndAccomodation);
        return ResponseEntity
                .ok()
                .body(result);
    }

    @GetMapping("/hotel-and-accomodations")
    public List<HotelAndAccomodation> getAllHotelAndAccomodations() {
        log.debug("REST request to get all Hotel And Accomodations");
        return hotelAndAccomodationRepository.findAll();
    }

    @GetMapping("/hotel-and-accomodations-with-cities")
    public List<HotelAndAccomodation> getAllHotelAndAccomodationsByCities() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        List<HotelAndAccomodation> hotelAndAccomodations = new ArrayList<>();
        List<Long> cityList = hotelAndAccomodationRepository.getAllCitiesByUserName(userName);
        if (cityList != null & cityList.size() > 0){
            hotelAndAccomodations = hotelAndAccomodationRepository.getAllByCities(cityList);
        }
        return hotelAndAccomodations;
    }

    @GetMapping("/hotel-and-accomodations/{id}")
    public ResponseEntity<HotelAndAccomodation> getHotelAndAccomodation(@PathVariable Long id) {
        log.debug("REST request to get HotelAndAccomodation : {}", id);
        Optional<HotelAndAccomodation> hotelAndAccomodation = hotelAndAccomodationRepository.findById(id);
        return ResponseEntity.ok(hotelAndAccomodation.get());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/hotel-and-accomodations/{id}")
    public ResponseEntity<Void> deleteHotelAndAccomodation(@PathVariable Long id) {
        log.debug("REST request to delete HotelAndAccomodation : {}", id);
        hotelAndAccomodationRepository.deleteById(id);
        return ResponseEntity
                .noContent()
                .build();
    }
}
