package com.multidestination.nontourist_server.repository;

import com.multidestination.nontourist_server.model.NonTourist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@SuppressWarnings("unused")
@Repository
public interface NonTouristRepository extends JpaRepository<NonTourist, Long> {

//    @Query(value = "select c.id from users u, user_cities uc, cities c " +
//            "where u.id = uc.user_id AND uc.cities_id = c.id " +
//            "AND u.username = ?1 AND c.is_active = ?2", nativeQuery = true)
//    List<Long> getAllCitiesByUserName(String userName, Boolean isActive);

    @Query(value = "select l.id from destination d, destination_location dl, location l \n" +
            "where d.id = dl.destination_id AND dl.location_id = l.id \n" +
            "AND d.is_active = true AND l.is_active = true", nativeQuery = true)
    List<Long> getAllLocationByDestination();

    @Query(value = "select * from non_tourist where is_active = ?1 AND location_id in (?2) AND is_approved = true", nativeQuery = true)
    List<NonTourist> getAllByLocation(Boolean isActive, List<Long> location);

    List<NonTourist> findAllByIsActive(Boolean isActive);

}
