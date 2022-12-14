package com.multidestination.reservation_server.repository;

import com.multidestination.reservation_server.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the Reservation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findAllByUserId(Long userId);

}
