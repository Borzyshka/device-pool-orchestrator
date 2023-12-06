package com.borzyshka.devicepool.controller.dto.response;

import lombok.NonNull;

import java.util.List;

public record ServerPoolStatusResponse(int size, @NonNull List<ServerPoolObjectStatusResponse> objects) {
}
