package com.multidestination.activitiesevents_server.domain;

import com.multidestination.activitiesevents_server.enumuration.ReservationType;
import lombok.Data;
import java.time.LocalDate;

@Data
public class Reservation {
    private Long id;
    private Long userId;
    private LocalDate startDate;
    private LocalDate endDate;
    private ReservationType reservationType;
    private Long reservationId;
    private Boolean isActive;
}
