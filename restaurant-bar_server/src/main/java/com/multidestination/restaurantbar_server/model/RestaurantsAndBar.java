package com.multidestination.restaurantbar_server.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "restaurants_and_bar")
@Data
public class RestaurantsAndBar {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Double price;

    @Column(name = "operation_hour")
    private String operationHour;

    @Column(name = "address")
    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Lob
    @Column(name = "photo")
    private byte[] photo;

    @Column(name = "photo_content_type")
    private String photoContentType;

    @Lob
    @Column(name = "menu")
    private byte[] menu;

    @Column(name = "type_of_food")
    private String typeOfFood;

    @ManyToOne
    private Location location;

    private Boolean isActive;

    private Boolean isApproved;

    private String createdBy;
}
