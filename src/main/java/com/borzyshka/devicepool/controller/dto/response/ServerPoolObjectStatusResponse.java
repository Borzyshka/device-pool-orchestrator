package com.borzyshka.devicepool.controller.dto.response;

import com.borzyshka.devicepool.service.dto.ServerStatus;
import lombok.NonNull;

import java.net.URL;

public record ServerPoolObjectStatusResponse(@NonNull String reservationId, @NonNull URL host, @NonNull ServerStatus status) {
}
