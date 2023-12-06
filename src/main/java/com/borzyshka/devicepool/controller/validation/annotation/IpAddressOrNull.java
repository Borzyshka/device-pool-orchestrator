package com.borzyshka.devicepool.controller.validation.annotation;

import com.borzyshka.devicepool.controller.validation.IpAddressOrNullValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IpAddressOrNullValidator.class)
public @interface IpAddressOrNull {
    String message() default "{Field should be a valid IP address}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
