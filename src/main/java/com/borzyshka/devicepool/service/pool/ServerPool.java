package com.borzyshka.devicepool.service.pool;

import com.borzyshka.devicepool.config.AppiumServerConfig;
import com.borzyshka.devicepool.service.dto.ServerStatus;
import com.borzyshka.devicepool.service.exception.ReservationNotFoundException;
import com.borzyshka.devicepool.service.util.RandomValuesGenerator;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import static org.awaitility.Awaitility.await;

@Service
@RequiredArgsConstructor
public class ServerPool {

    private final AppiumServerConfig serverConfig;

    private final ConcurrentHashMap<String, AppiumDriverLocalService> storage = new ConcurrentHashMap<>();
    ReentrantLock lock = new ReentrantLock();

    // --- Pool
    public ServerPoolObject acquire(@NonNull String host, int port) {

        await().atMost(20, TimeUnit.SECONDS).until(() -> !lock.isLocked());
        final AppiumDriverLocalService service = AppiumDriverLocalService.buildService(getBaseBuilder()
                .withIPAddress(host).usingPort(port));
        final String id = RandomValuesGenerator.id();
        storage.put(id, service);

        return new ServerPoolObject(id, service);

    }

    public ServerPoolObject acquire() {
        return acquire(serverConfig.getDefaultHost(), RandomValuesGenerator.appiumServicePort());
    }

    public ServerPoolObject acquire(int port) {
        return acquire(serverConfig.getDefaultHost(), port);
    }

    public ServerPoolObject get(String serverId) {
        if (null == storage.get(serverId)) {
            throw new ReservationNotFoundException(serverId);
        }
        return new ServerPoolObject(serverId, storage.get(serverId));
    }

    public void recycle(String id) {
        await().atMost(20, TimeUnit.SECONDS).until(() -> !lock.isLocked());
        if (null == storage.get(id)) {
            throw new ReservationNotFoundException(id);
        }
        if (storage.get(id).isRunning()) {
            storage.get(id).stop();
        }
        storage.remove(id);
    }

    public List<ServerStatusObject> list() {

        return storage.entrySet().stream().map(entry -> new ServerStatusObject(entry.getKey(), entry.getValue(),
                        entry.getValue().isRunning() ? ServerStatus.RUNNING : ServerStatus.IDLE))
                .collect(Collectors.toList());

    }

    public void recycleAll() {
        lock.lock();
        try {
            storage.values().stream().filter(AppiumDriverLocalService::isRunning)
                    .forEach(AppiumDriverLocalService::stop);
            storage.clear();

        } finally {
            lock.unlock();
        }
    }

    private AppiumServiceBuilder getBaseBuilder() {
        return new AppiumServiceBuilder()
                .withAppiumJS(serverConfig.getAppiumPath())
                .withTimeout(Duration.ofSeconds(60))
                .usingDriverExecutable(serverConfig.getDriverExecutable());
    }

    // --- Records
    public record ServerPoolObject(@NonNull String reservationId, @NonNull AppiumDriverLocalService server) {
    }

    public record ServerStatusObject(@NonNull String reservationId, @NonNull AppiumDriverLocalService server,
                                     @NonNull ServerStatus status) {
    }


}
