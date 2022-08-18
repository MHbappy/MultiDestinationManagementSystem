package com.multidestination.activitiesevents_server.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "activities_event")
@Data
public class ActivitiesEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "activity_partner")
    private String activityPartner;

    @Column(name = "partners")
    private String partners;

    @Column(name = "description")
    private String description;

    @Column(name = "address")
    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "activity_duration")
    private Integer activityDuration;

    @Column(name = "activity_group")
    private Integer activityGroup;

    @Lob
    @Column(name = "photo")
    private byte[] photo;

    @Column(name = "photo_content_type")
    private String photoContentType;

    @ManyToOne
    private Cities cities;
}
