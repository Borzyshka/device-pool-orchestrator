package com.borzyshka.devicepool.controller.validation.annotation;

import com.borzyshka.devicepool.controller.validation.PortRequiredWithHostValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE_USE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PortRequiredWithHostValidator.class)
public @interface PortRequiredWithHost {
    String message() default "{Port is required if host is provided}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
