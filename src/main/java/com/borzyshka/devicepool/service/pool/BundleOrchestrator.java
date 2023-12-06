package com.borzyshka.devicepool.service.pool;

import com.borzyshka.devicepool.service.dto.DeviceStatus;
import com.borzyshka.devicepool.service.dto.ServerStatus;
import com.borzyshka.devicepool.service.exception.ReservationNotFoundException;
import com.borzyshka.devicepool.service.util.DeviceCapbilitiesSupplier;
import com.borzyshka.devicepool.service.util.RandomValuesGenerator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class BundleOrchestrator implements ObjectPool<BundleOrchestrator.BundleObject>, HasObjectsStatus<BundleOrchestrator.BundleObjectStatus> {

    @NonNull
    private final ServerPool serverPool;
    @NonNull
    private final DevicePool devicePool;
    @NonNull
    private final DeviceCapbilitiesSupplier capabilitiesSupplier;
    // --- Pool
    @NonNull
    private final Map<String, BundleObject> reservations = new ConcurrentHashMap<>();
    @NonNull
    private final ReentrantLock lock = new ReentrantLock();

    @Override
    public @NonNull BundleOrchestrator.BundleObject acquire(int duration, @NonNull TimeUnit timeUnit) {

        final DevicePool.DevicePoolObject device = devicePool.acquire(duration, timeUnit);
        final ServerPool.ServerPoolObject server = serverPool.acquire();
        final Map<String, Object> capabilities = capabilitiesSupplier.get();
        final String reservationId = RandomValuesGenerator.id();
        final BundleObject bundleObject = new BundleObject(reservationId, device, capabilities, server);
        reservations.put(reservationId, bundleObject);

        return bundleObject;
    }

    @Override
    public void recycle(@NonNull String reservationId) {
        log.info("Releasing reservation {}", reservationId);
        if (!reservations.containsKey(reservationId)) {
            throw new ReservationNotFoundException(reservationId);
        }
        final BundleObject bundleObject = reservations.get(reservationId);
        devicePool.recycle(bundleObject.device().reservationId());
        serverPool.recycle(bundleObject.server().reservationId());
        reservations.remove(reservationId);
    }

    @Override
    public void destroy() {
        lock.lock();
        try {
            reservations.keySet().forEach(this::recycle);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public List<BundleObjectStatus> getObjectsStatus() {

        return reservations.values().stream().map(reservation -> {
            final DevicePool.DevicePoolObjectStatus deviceStatus = new DevicePool.DevicePoolObjectStatus(reservation.device().device(),
                    DeviceStatus.BUSY);
            final ServerPool.ServerStatusObject serverSatus = new ServerPool.ServerStatusObject(reservation.server.reservationId(),
                    reservation.server.server(), reservation.server.server().isRunning() ? ServerStatus.RUNNING : ServerStatus.IDLE);

            return new BundleObjectStatus(reservation.reservationId(), deviceStatus, reservation.deviceCapabilities(),
                    serverSatus);
        }).collect(Collectors.toList());

    }


    // --- Records
    public record BundleObject(@NonNull String reservationId, @NonNull DevicePool.DevicePoolObject device,
                               @NonNull Map<String, Object> deviceCapabilities,
                               @NonNull ServerPool.ServerPoolObject server) {
    }

    public record BundleObjectStatus(@NonNull String reservationId,
                                     @NonNull DevicePool.DevicePoolObjectStatus device,
                                     @NonNull Map<String, Object> deviceCapabilities,
                                     @NonNull ServerPool.ServerStatusObject server) {
    }


}
