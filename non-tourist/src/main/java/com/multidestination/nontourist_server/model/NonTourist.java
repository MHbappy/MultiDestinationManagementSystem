package com.multidestination.nontourist_server.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "non_tourist")
@Data
public class NonTourist {
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

    @Column(name = "hours_of_operation")
    private Integer hoursOfOperation;

    private Boolean isActive;

    @ManyToOne
    private Cities cities;
}
