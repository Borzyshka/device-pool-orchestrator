package com.borzyshka.devicepool.controller.dto.response;

import lombok.NonNull;

import java.util.Map;

public record BundlePoolObjectStatusResponse(@NonNull String reservationId,
                                             @NonNull DevicePoolObjectStatusResponse device,
                                             @NonNull Map<String, Object> deviceCapabilities,
                                             @NonNull ServerPoolObjectStatusResponse server) {
}
