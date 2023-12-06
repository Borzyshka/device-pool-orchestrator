package com.borzyshka.devicepool.controller.dto.request;

import com.borzyshka.devicepool.controller.validation.annotation.IpAddressOrNull;
import com.borzyshka.devicepool.controller.validation.annotation.PortRequiredWithHost;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;


@PortRequiredWithHost
public record ServerReservationRequest(

        @IpAddressOrNull(message = "Host should be a valid IP address or missing")
        String host,

        @Nullable
        @Min(value = 1024, message = "Port must be greater than or equal to {value}")
        @Max(value = 65535, message = "Port must be less than or equal to {value}")
        Integer port) {
}
