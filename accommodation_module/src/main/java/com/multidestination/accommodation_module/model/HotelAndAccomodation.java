package com.multidestination.accommodation_module.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "hotel_and_accomodation")
@Data
public class HotelAndAccomodation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "address")
    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "price_per_night")
    private Double pricePerNight;

    @Column(name = "accomodation_features")
    private String accomodationFeatures;

    @Lob
    @Column(name = "photo")
    private byte[] photo;

    @Column(name = "photo_content_type")
    private String photoContentType;

    @ManyToOne
    private Cities cities;

    private Boolean isActive;
}
