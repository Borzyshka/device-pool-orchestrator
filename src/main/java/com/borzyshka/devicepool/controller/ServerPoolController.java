package com.borzyshka.devicepool.controller;

import com.borzyshka.devicepool.controller.dto.request.ServerReservationRequest;
import com.borzyshka.devicepool.controller.dto.response.ServerPoolObjectStatusResponse;
import com.borzyshka.devicepool.controller.dto.response.ServerPoolStatusResponse;
import com.borzyshka.devicepool.controller.dto.response.ServerReservationResponse;
import com.borzyshka.devicepool.service.ServerPoolService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
@Validated
public class ServerPoolController {

    private final ServerPoolService serverPoolService;


    @GetMapping("/v1/servers")
    @ResponseStatus(HttpStatus.OK)
    public ServerPoolStatusResponse listAll() {
        return serverPoolService.get();
    }

    @DeleteMapping("/v1/servers")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAll() {
        serverPoolService.recycleAll();
    }

    @PostMapping("/v1/servers")
    @ResponseStatus(HttpStatus.OK)
    public ServerReservationResponse aquire(@Valid @RequestBody ServerReservationRequest request) {
        return serverPoolService.acquire(request.host(), request.port());
    }

    @PostMapping("/v1/servers/{reservationId}/start")
    @ResponseStatus(HttpStatus.OK)
    public ServerPoolObjectStatusResponse start(@PathVariable("reservationId") @NotBlank String reservationId) {
        return serverPoolService.start(reservationId);
    }

    @PostMapping("/v1/servers/{reservationId}/stop")
    @ResponseStatus(HttpStatus.OK)
    public ServerPoolObjectStatusResponse stop(@PathVariable("reservationId") @NotBlank String reservationId) {
        return serverPoolService.stop(reservationId);
    }

    @DeleteMapping("/v1/servers/{reservationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void recycle(@PathVariable("reservationId") @NotBlank String reservationId) {
        serverPoolService.recycle(reservationId);
    }


}
