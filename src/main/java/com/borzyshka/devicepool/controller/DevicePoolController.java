package com.borzyshka.devicepool.controller;

import com.borzyshka.devicepool.service.DevicePoolService;
import com.borzyshka.devicepool.controller.dto.response.DevicePoolStatusResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class DevicePoolController {

    private final DevicePoolService devicePoolService;

    @GetMapping("/v1/devices")
    @ResponseStatus(HttpStatus.OK)
    public DevicePoolStatusResponse getDevices() {
        return devicePoolService.get();
    }

}
