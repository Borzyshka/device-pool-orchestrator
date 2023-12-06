package com.borzyshka.devicepool.service.util;

import com.github.javafaker.Faker;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;


@Service
public class RandomValuesGenerator {

    public static String id() {
        return Faker.instance().internet().uuid();
    }

    public static int appiumServicePort() {
        return ThreadLocalRandom.current().nextInt(4724, 5000);
    }

    public static int capabilitiesPort() {
        return ThreadLocalRandom.current().nextInt(6000, 65535);
    }
}
