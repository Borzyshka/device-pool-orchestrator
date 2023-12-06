package com.borzyshka.devicepool.controller.exceptionhandler;

import com.borzyshka.devicepool.service.exception.DeveloperException;
import com.borzyshka.devicepool.controller.dto.response.ErrorResponse;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class DeveloperExceptionHandler {

    @ExceptionHandler(DeveloperException.class)
    public ResponseEntity<ErrorResponse> handleException(@NonNull DeveloperException exception) {
        final ErrorResponse body = new ErrorResponse(exception.getStatus().value(), exception.getInternalCode(), exception.getMessage());
        return new ResponseEntity<>(body, exception.getStatus());
    }
}
