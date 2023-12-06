package com.borzyshka.devicepool.controller.validation;

import com.borzyshka.devicepool.controller.dto.request.ServerReservationRequest;
import com.borzyshka.devicepool.controller.validation.annotation.PortRequiredWithHost;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class PortRequiredWithHostValidator implements ConstraintValidator<PortRequiredWithHost, ServerReservationRequest> {

    public void initialize(PortRequiredWithHost constraintAnnotation) {
    }

    @Override
    public boolean isValid(ServerReservationRequest value, ConstraintValidatorContext context) {
        return value.host() == null || value.port() != null;
    }
}