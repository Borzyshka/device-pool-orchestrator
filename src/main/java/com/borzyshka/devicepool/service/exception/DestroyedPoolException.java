package com.borzyshka.devicepool.service.exception;

import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
@Getter
public class DestroyedPoolException extends DeveloperException {

    private final String internalCode = "DESTROYED_POOL";
    private final HttpStatus status = HttpStatus.GONE;

    public DestroyedPoolException(@NonNull String poolId) {
        super(String.format("Pool '%s' is already destroyed", poolId));
    }

}
