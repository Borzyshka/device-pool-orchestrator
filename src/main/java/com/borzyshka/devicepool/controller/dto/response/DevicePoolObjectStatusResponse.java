package com.borzyshka.devicepool.controller.dto.response;

import com.borzyshka.devicepool.service.dto.DeviceInfo;
import com.borzyshka.devicepool.service.dto.DeviceStatus;
import lombok.NonNull;

public record DevicePoolObjectStatusResponse(@NonNull DeviceInfo details, @NonNull DeviceStatus status) {
}
