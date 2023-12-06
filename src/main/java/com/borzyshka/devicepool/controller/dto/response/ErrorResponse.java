package com.borzyshka.devicepool.controller.dto.response;

import lombok.NonNull;

public record ErrorResponse(@NonNull Integer code, @NonNull String status, @NonNull String message) {

}
