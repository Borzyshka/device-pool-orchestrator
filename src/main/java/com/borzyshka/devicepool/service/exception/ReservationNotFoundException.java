package com.borzyshka.devicepool.service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ReservationNotFoundException extends DeveloperException {

    private final String internalCode = "RESERVATION_NOT_FOUND";
    private final HttpStatus status = HttpStatus.NOT_FOUND;

    public ReservationNotFoundException(String id) {
        super(String.format("Reservation with reservationId '%s' not found.", id));
    }


}
