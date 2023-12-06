package com.borzyshka.devicepool.controller.dto.response;

import com.borzyshka.devicepool.service.dto.DeviceInfo;
import lombok.NonNull;

import java.net.URL;
import java.util.Map;

public record BundleReservationResponse(@NonNull String reservationId, @NonNull DeviceInfo deviceInfo,
                                        @NonNull URL serverHost, @NonNull Map<String, Object> capabilities) {
}
