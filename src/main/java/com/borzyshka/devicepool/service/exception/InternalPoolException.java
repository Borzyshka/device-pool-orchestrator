package com.borzyshka.devicepool.service.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
@Getter
public class InternalPoolException extends DeveloperException {
    private final String internalCode = "INTERNAL_POOL_EXCEPTION";
    private final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;


    public InternalPoolException(Throwable cause) {
        super(cause.getMessage());
    }

}
