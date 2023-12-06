package com.borzyshka.devicepool.service.exception;

import lombok.Getter;
import lombok.NonNull;
import org.springframework.http.HttpStatus;

@Getter
public class RuntimeCommandExecutionException extends DeveloperException {

    private final String internalCode = "RUNTIME_COMMAND_EXECUTION_FAILED";
    private final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

    public RuntimeCommandExecutionException(@NonNull String commandName) {
        super(String.format("Error occurred during  '%s' runtime command execution", commandName));
    }
}
