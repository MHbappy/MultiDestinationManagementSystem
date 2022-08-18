package com.multidestination.activitiesevents_server.domain;
import com.multidestination.activitiesevents_server.enumuration.ReservationType;
import lombok.Data;

@Data
public class ReservationRequest {
    private Long userId;
    private String startDate;
    private String endDate;
    private ReservationType reservationType;
    private Long reservationId;
}
