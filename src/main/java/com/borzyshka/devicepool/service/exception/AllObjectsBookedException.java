package com.borzyshka.devicepool.service.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
@Getter
public class AllObjectsBookedException extends DeveloperException {

    private final String internalCode = "ALL_OBJECTS_BOOKED";
    private final HttpStatus status = HttpStatus.CONFLICT;

    public AllObjectsBookedException() {
        super("Timeout Reached waiting for a free object. All objects are booked");
    }

}
