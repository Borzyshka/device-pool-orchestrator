package com.borzyshka.devicepool.controller.dto.response;

import lombok.NonNull;

import java.util.List;

public record DevicePoolStatusResponse(int size, @NonNull List<DevicePoolObjectStatusResponse> objects) {
}
