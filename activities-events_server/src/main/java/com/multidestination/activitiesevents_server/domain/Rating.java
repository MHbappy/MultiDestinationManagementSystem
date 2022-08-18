package com.multidestination.activitiesevents_server.domain;

import lombok.Data;

import javax.persistence.*;

@Data
public class Rating {
    private Long id;
    private Long userId;
    private String comment;
    private Integer score;
}
