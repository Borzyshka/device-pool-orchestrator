package com.borzyshka.devicepool.service.pool;

import com.borzyshka.devicepool.service.dto.DeviceInfo;
import com.borzyshka.devicepool.service.dto.DeviceStatus;
import com.borzyshka.devicepool.service.exception.AllObjectsBookedException;
import com.borzyshka.devicepool.service.exception.InternalPoolException;
import com.borzyshka.devicepool.service.exception.ReservationNotFoundException;
import com.borzyshka.devicepool.service.util.RandomValuesGenerator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import static org.awaitility.Awaitility.await;


@RequiredArgsConstructor
@Service
@Slf4j
public class DevicePool implements ObjectPool<DevicePool.DevicePoolObject>, HasObjectsStatus<DevicePool.DevicePoolObjectStatus> {

    // --- Storage
    @NonNull
    private final List<DeviceInfo> allDevices = new ArrayList<>();
    @NonNull
    private final BlockingQueue<DeviceInfo> pool = new ArrayBlockingQueue<>(20, true);
    @NonNull
    private final Map<String, DevicePoolObject> reservations = new ConcurrentHashMap<>();
    @NonNull
    ReentrantLock lock = new ReentrantLock();


    public void update(List<DeviceInfo> devices) {
        lock.lock();
        try {
            log.debug("Getting the list of the newly connected devices");
            final List<DeviceInfo> newDevices = devices.stream().filter(device -> !allDevices.stream()
                    .map(DeviceInfo::udid).toList().contains(device.udid())).toList();

            log.debug("Getting the list of the disconnected devices");
            final List<DeviceInfo> removedDevices = allDevices.stream().filter(device ->
                    !devices.stream()
                            .map(DeviceInfo::udid).toList().contains(device.udid())).toList();

            if (!removedDevices.isEmpty()) {
                log.debug("Removing disconnected devices from pool");
                allDevices.removeAll(removedDevices);
                pool.stream().filter(device -> removedDevices.stream().map(DeviceInfo::udid).toList()
                        .contains(device.udid())).forEach(pool::remove);
                log.debug("Clearing reservations for disconnected devices");
                reservations.entrySet().stream().filter(entry -> removedDevices.contains(entry.getValue().device()))
                        .forEach(entry -> reservations.remove(entry.getKey()));
            }
            if (!newDevices.isEmpty()) {
                log.debug("Adding new devices to the pool");
                allDevices.addAll(newDevices);
                pool.addAll(newDevices);
            }
        } finally {
            lock.unlock();
        }

    }

    // -- Object Pool
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
    @NonNull
    public DevicePoolObject acquire(int duration, @NonNull TimeUnit timeUnit) {

        await().atMost(20, TimeUnit.SECONDS).until(() -> !lock.isLocked());
        try {
            final DeviceInfo acquired = pool.poll(duration, timeUnit);
            if (null == acquired) {
                throw new AllObjectsBookedException();
            }
            final String reservationId = RandomValuesGenerator.id();
            final DevicePoolObject poolObject = new DevicePoolObject(reservationId, acquired);

            reservations.put(reservationId, poolObject);
            return poolObject;
        } catch (InterruptedException e) {
            throw new InternalPoolException(e);
        }

    }

    @Override
    public void recycle(@NonNull String reservationId) {

        await().atMost(20, TimeUnit.SECONDS).until(() -> !lock.isLocked());
        log.debug("Releasing reservation {}", reservationId);
        final DevicePoolObject object = reservations.get(reservationId);
        if (null == object) {
            throw new ReservationNotFoundException(reservationId);
        }
        reservations.remove(reservationId);
        pool.add(object.device());

    }

    // --- HasObjectsStatus
    @Override
    @NonNull
    public List<DevicePoolObjectStatus> getObjectsStatus() {


        return allDevices.stream().map(device -> new DevicePoolObjectStatus(device,
                reservations.values().stream().anyMatch(value -> value.device().equals(device)) ? DeviceStatus.BUSY
                        : DeviceStatus.FREE)).collect(Collectors.toList());

    }


    // --- Records
    public record DevicePoolObject(@NonNull String reservationId, @NonNull DeviceInfo device) {
    }

    public record DevicePoolObjectStatus(@NonNull DeviceInfo deviceInfo,
                                         @NonNull DeviceStatus status) {
    }
}
