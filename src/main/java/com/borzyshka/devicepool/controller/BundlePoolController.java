package com.borzyshka.devicepool.controller;

import com.borzyshka.devicepool.controller.dto.response.BundlePoolReservationResponse;
import com.borzyshka.devicepool.controller.dto.response.BundlePoolStatusResponse;
import com.borzyshka.devicepool.service.BundlePoolService;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;


@RestController
@AllArgsConstructor
public class BundlePoolController {

    private final BundlePoolService bundlePoolService;

    @GetMapping("/v1/bundles")
    @ResponseStatus(HttpStatus.OK)
    public BundlePoolStatusResponse list() {
        return bundlePoolService.get();
    }

    @DeleteMapping("/v1/bundles")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void recycleAll() {
        bundlePoolService.recycleAll();
    }

    @PostMapping("/v1/bundles/reservations")
    @ResponseStatus(HttpStatus.OK)
    public BundlePoolReservationResponse acquireDevice(@RequestParam(name = "quantity", required = false) Integer quantity) {
        return bundlePoolService.acquireBundles(Objects.isNull(quantity) ? 1 : quantity);
    }

    @DeleteMapping("/v1/bundles/{reservationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void recycleDevice(@PathVariable("reservationId") @NotBlank String reservationId) {
        bundlePoolService.recycleDevice(reservationId);
    }
}
