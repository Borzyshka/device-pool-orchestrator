package com.borzyshka.devicepool.service;


import com.borzyshka.devicepool.controller.dto.response.ServerPoolObjectStatusResponse;
import com.borzyshka.devicepool.controller.dto.response.ServerPoolStatusResponse;
import com.borzyshka.devicepool.controller.dto.response.ServerReservationResponse;
import com.borzyshka.devicepool.service.dto.ServerStatus;
import com.borzyshka.devicepool.service.pool.ServerPool;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.awaitility.Awaitility.await;

@Service
@AllArgsConstructor
@Slf4j
public class ServerPoolService {

    private final ServerPool serverPool;

    public ServerPoolStatusResponse get() {

        final List<ServerPoolObjectStatusResponse> servers = serverPool.list().stream()
                .map(object -> new ServerPoolObjectStatusResponse(object.reservationId(), object.server().getUrl(), object.status()))
                .collect(Collectors.toList());

        return new ServerPoolStatusResponse(servers.size(), servers);

    }

    public ServerReservationResponse acquire(@Nullable String host, @Nullable Integer port) {

        final ServerPool.ServerPoolObject server = Objects.nonNull(host) ? serverPool.acquire(host, Objects.requireNonNull(port)) :
                (Objects.nonNull(port) ? serverPool.acquire(port) : serverPool.acquire());

        return new ServerReservationResponse(server.reservationId(), server.server().getUrl());

    }

    public ServerPoolObjectStatusResponse start(@NonNull String reservationId) {

        ServerPool.ServerPoolObject server = serverPool.get(reservationId);
        if (!server.server().isRunning()) {
            server.server().start();
        }
        return new ServerPoolObjectStatusResponse(server.reservationId(), server.server().getUrl(), ServerStatus.RUNNING);

    }

    public ServerPoolObjectStatusResponse stop(@NonNull String reservationId) {

        ServerPool.ServerPoolObject object = serverPool.get(reservationId);
        if (object.server().isRunning()) {
            object.server().stop();
        }
        return new ServerPoolObjectStatusResponse(object.reservationId(), object.server().getUrl(), ServerStatus.IDLE);

    }

    public void startServersInSeparateThreads(@NonNull List<ServerPool.ServerPoolObject> servers) {

        servers.forEach(server -> {
            Executors.newSingleThreadExecutor().execute(() -> server.server().start());
        });
        await().atMost(50, TimeUnit.SECONDS).until(() ->
                servers.stream()
                        .map(ServerPool.ServerPoolObject::server).allMatch(AppiumDriverLocalService::isRunning));
    }

    public void recycle(@NonNull String reservationId) {
        serverPool.recycle(reservationId);
    }

    public void recycleAll() {
        serverPool.recycleAll();
    }
}
