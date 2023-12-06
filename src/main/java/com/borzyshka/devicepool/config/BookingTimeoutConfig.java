package com.borzyshka.devicepool.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.concurrent.TimeUnit;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "app.timeout")
public class BookingTimeoutConfig {

    private int duration;
    private TimeUnit timeUnit;
}
