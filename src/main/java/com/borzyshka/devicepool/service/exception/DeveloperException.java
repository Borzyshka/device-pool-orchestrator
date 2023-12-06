package com.borzyshka.devicepool.service.exception;

import lombok.NonNull;
import org.springframework.http.HttpStatus;

public abstract class DeveloperException extends RuntimeException {

    public DeveloperException(@NonNull String message) {
        super(message);
    }

    public abstract String getInternalCode();

    public abstract HttpStatus getStatus();
}
