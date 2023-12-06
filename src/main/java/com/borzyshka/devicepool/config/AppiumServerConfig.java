package com.borzyshka.devicepool.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.io.File;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "app.appium")
public class AppiumServerConfig {

    private File appiumPath;
    private File driverExecutable;
    private String defaultHost;

}
