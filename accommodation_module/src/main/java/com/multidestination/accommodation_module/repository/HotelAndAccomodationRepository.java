package com.multidestination.accommodation_module.repository;

import com.multidestination.accommodation_module.model.HotelAndAccomodation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@SuppressWarnings("unused")
@Repository
public interface HotelAndAccomodationRepository extends JpaRepository<HotelAndAccomodation, Long> {

    @Query(value = "select c.id from users u, user_cities uc, cities c " +
            "where u.id = uc.user_id AND uc.cities_id = c.id " +
            "AND u.username = ?1 AND c.is_active = ?2", nativeQuery = true)
    List<Long> getAllCitiesByUserName(String userName, Boolean isActive);

    @Query(value = "select * from hotel_and_accomodation where is_active = ?1 AND cities_id in (?2)", nativeQuery = true)
    List<HotelAndAccomodation> getAllByCities(Boolean isActive, List<Long> cities);

    List<HotelAndAccomodation> findAllByIsActive(Boolean isActive);

}

