package com.multidestination.restaurantbar_server.repository;

import com.multidestination.restaurantbar_server.model.RestaurantsAndBar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Spring Data SQL repository for the RestaurantsAndBar entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RestaurantsAndBarRepository extends JpaRepository<RestaurantsAndBar, Long> {
//    @Query(value = "select c.id from users u, user_cities uc, cities c " +
//            "where u.id = uc.user_id AND uc.cities_id = c.id " +
//            "AND u.username = ?1 AND c.is_active = ?2", nativeQuery = true)
//    List<Long> getAllCitiesByUserName(String userName, Boolean isActive);

    @Query(value = "select l.id from destination d, destination_location dl, location l \n" +
            "where d.id = dl.destination_id AND dl.location_id = l.id \n" +
            "AND d.is_active = true AND l.is_active = true", nativeQuery = true)
    List<Long> getAllLocationByDestination();

    @Query(value = "select * from restaurants_and_bar where is_active = ?1 AND location_id in (?2) AND is_approved = true", nativeQuery = true)
    List<RestaurantsAndBar> getAllByLocation(Boolean isActive, List<Long> location);

    List<RestaurantsAndBar> findAllByIsActive(Boolean isActive);


}
