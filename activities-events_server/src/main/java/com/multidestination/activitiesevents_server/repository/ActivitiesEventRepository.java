package com.multidestination.activitiesevents_server.repository;

import com.multidestination.activitiesevents_server.model.ActivitiesEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the ActivitiesEvent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActivitiesEventRepository extends JpaRepository<ActivitiesEvent, Long> {
    @Query(value = "select ID from users WHERE userName = ?1", nativeQuery = true)
    Long getUserIdByUserName(String userName);

    @Query(value = "select c.id from users u, user_cities uc, cities c " +
            "where u.id = uc.user_id AND uc.cities_id = c.id AND c.is_active = ?1 " +
            "AND u.username = ?2", nativeQuery = true)
    List<Long> getAllCitiesByUserName(Boolean isActive, String userName);

    @Query(value = "select * from hotel_and_accomodation where is_active = ?1 cities_id in (?2)", nativeQuery = true)
    List<ActivitiesEvent> getAllByCities(Boolean isActive, List<Long> cities);

    List<ActivitiesEvent> findAllByIsActive(Boolean isActive);

}
