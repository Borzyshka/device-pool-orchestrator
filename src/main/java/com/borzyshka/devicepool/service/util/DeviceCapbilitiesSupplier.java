package com.borzyshka.devicepool.service.util;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.Supplier;

@Service
public class DeviceCapbilitiesSupplier implements Supplier<Map<String, Object>> {

    @Override
    public Map<String, Object> get() {
        return Map.of(
                "adbForwardPort", RandomValuesGenerator.capabilitiesPort(),
                "systemPort", RandomValuesGenerator.capabilitiesPort(),
                "chromeDriverPort", RandomValuesGenerator.capabilitiesPort()
        );
    }
}
