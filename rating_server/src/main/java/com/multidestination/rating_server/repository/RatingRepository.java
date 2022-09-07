package com.multidestination.rating_server.repository;

import com.multidestination.rating_server.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the Rating entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findAllByReservationId(Long id);
    List<Rating> findAllByUserId(Long userId);
}
