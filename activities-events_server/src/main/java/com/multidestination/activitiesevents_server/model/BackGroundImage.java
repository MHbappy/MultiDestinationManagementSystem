package com.multidestination.activitiesevents_server.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "back_ground_image")
@Data
public class BackGroundImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "photo")
    private byte[] photo;
}
