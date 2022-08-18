package com.multidestination.nontourist_server.repository;

import com.multidestination.nontourist_server.model.NonTourist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@SuppressWarnings("unused")
@Repository
public interface NonTouristRepository extends JpaRepository<NonTourist, Long> {

    @Query(value = "select c.id from users u, user_cities uc, cities c " +
            "where u.id = uc.user_id AND uc.cities_id = c.id " +
            "AND u.username = ?1", nativeQuery = true)
    List<Long> getAllCitiesByUserName(String userName);

    @Query(value = "select * from hotel_and_accomodation where cities_id in (?1)", nativeQuery = true)
    List<NonTourist> getAllByCities(List<Long> cities);

}
