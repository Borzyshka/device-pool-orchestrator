package com.borzyshka.devicepool.controller.dto.response;

import lombok.NonNull;

import java.util.List;

public record BundlePoolStatusResponse(int size, @NonNull List<BundlePoolObjectStatusResponse> content) {
}
