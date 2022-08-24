package com.multidestination.restaurantbar_server.repository;

import com.multidestination.restaurantbar_server.model.Cities;
import com.multidestination.restaurantbar_server.model.RestaurantsAndBar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<Cities, Long> {
}
