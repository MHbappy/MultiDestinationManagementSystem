package com.multidestination.rating_server.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "rating")
@Data
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "reservation_id")
    private Long reservationId;

    @Column(name = "comment")
    private String comment;

    @Column(name = "score")
    private Integer score;
}
