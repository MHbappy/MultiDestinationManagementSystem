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
    @Query(value = "select c.id from users u, user_cities uc, cities c " +
            "where u.id = uc.user_id AND uc.cities_id = c.id " +
            "AND u.username = ?1", nativeQuery = true)
    List<Long> getAllCitiesByUserName(String userName);

    @Query(value = "select * from restaurants_and_bar where cities_id in (?1)", nativeQuery = true)
    List<RestaurantsAndBar> getAllByCities(List<Long> cities);
}
