package com.borzyshka.devicepool.service;

import com.borzyshka.devicepool.controller.dto.response.DevicePoolObjectStatusResponse;
import com.borzyshka.devicepool.controller.dto.response.DevicePoolStatusResponse;
import com.borzyshka.devicepool.service.pool.DevicePool;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class DevicePoolService {

    @NonNull
    private final DeviceInfoService deviceService;
    @NonNull
    private final DevicePool devicePool;


    public DevicePoolStatusResponse get() {

        update();

        final List<DevicePoolObjectStatusResponse> devices = devicePool.getObjectsStatus().stream()
                .map(object -> new DevicePoolObjectStatusResponse(object.deviceInfo(), object.status()))
                .collect(Collectors.toList());

        return new DevicePoolStatusResponse(devices.size(), devices);
    }


    public void update() {
        devicePool.update(deviceService.getAll());
    }


}
