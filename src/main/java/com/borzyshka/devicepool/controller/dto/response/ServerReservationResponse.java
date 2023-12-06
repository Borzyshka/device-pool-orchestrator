package com.borzyshka.devicepool.controller.dto.response;

import lombok.NonNull;

import java.net.URL;

public record ServerReservationResponse(@NonNull String reservationId, @NonNull URL url) {
}
