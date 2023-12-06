package com.borzyshka.devicepool.service;

import com.borzyshka.devicepool.config.BookingTimeoutConfig;
import com.borzyshka.devicepool.controller.dto.response.*;
import com.borzyshka.devicepool.service.exception.AllObjectsBookedException;
import com.borzyshka.devicepool.service.pool.BundleOrchestrator;
import com.borzyshka.devicepool.service.pool.ServerPool;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
@Slf4j
public class BundlePoolService {

    @NonNull
    private final BundleOrchestrator bundleOrchestrator;
    @NonNull
    private final DevicePoolService devicePoolService;
    @NonNull
    private final ServerPoolService serverPoolService;
    @NonNull
    private final BookingTimeoutConfig bookingTimeoutConfig;


    public BundlePoolStatusResponse get() {

        final List<BundlePoolObjectStatusResponse> statuses = bundleOrchestrator.getObjectsStatus().stream()
                .map(status -> {
                    final DevicePoolObjectStatusResponse deviceStatus =
                            new DevicePoolObjectStatusResponse(status.device().deviceInfo(), status.device().status());
                    final ServerPoolObjectStatusResponse serverStatus = new ServerPoolObjectStatusResponse(status.server().reservationId(),
                            status.server().server().getUrl(), status.server().status());

                    return new BundlePoolObjectStatusResponse(status.reservationId(), deviceStatus, status.deviceCapabilities(), serverStatus);
                }).toList();

        return new BundlePoolStatusResponse(statuses.size(), statuses);
    }

    public BundlePoolReservationResponse acquireBundles(int quantity) {

        devicePoolService.update();
        final List<BundleReservationResponse> bundles = new ArrayList<>();
        final List<ServerPool.ServerPoolObject> servers = new ArrayList<>();

        while (quantity > 0) {
            try {
                BundleOrchestrator.BundleObject bundleObject = bundleOrchestrator.acquire(bookingTimeoutConfig.getDuration(),
                        bookingTimeoutConfig.getTimeUnit());
                bundles.add(new BundleReservationResponse(bundleObject.reservationId(), bundleObject.device().device(),
                        bundleObject.server().server().getUrl(), bundleObject.deviceCapabilities()));
                servers.add(bundleObject.server());
                --quantity;
            } catch (AllObjectsBookedException e) {
                if (bundles.isEmpty()) {
                    log.debug("No devices are available. Throwing an error");
                    throw e;
                }
                log.debug("Less devices are available.");
                break;
            }
        }

        log.debug("Starting {} Appium Services", servers.size());
        serverPoolService.startServersInSeparateThreads(servers);
        log.debug("Services Started");
        return new BundlePoolReservationResponse(bundles);
    }

    public void recycleDevice(@NonNull String reservationId) {
        bundleOrchestrator.recycle(reservationId);
    }

    public void recycleAll() {
        bundleOrchestrator.destroy();
    }

}
