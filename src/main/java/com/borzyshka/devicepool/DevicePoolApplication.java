package com.borzyshka.devicepool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class DevicePoolApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevicePoolApplication.class, args);
    }

}
