package com.multidestination.activitiesevents_server.domain;

import lombok.Data;

@Data
public class RatingRequest {
    private String comment;
    private Integer score;
}
