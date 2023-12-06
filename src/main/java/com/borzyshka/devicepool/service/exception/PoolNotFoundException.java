package com.borzyshka.devicepool.service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class PoolNotFoundException extends DeveloperException {

    private final HttpStatus status = HttpStatus.NOT_FOUND;
    private final String internalCode = "POOL_NOT_FOUND";

    public  PoolNotFoundException(){
        super("Pool is not found");
    }
}
