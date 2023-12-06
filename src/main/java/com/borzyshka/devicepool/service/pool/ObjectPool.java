package com.borzyshka.devicepool.service.pool;

import lombok.NonNull;

import java.util.concurrent.TimeUnit;

public interface ObjectPool<T> {

    @NonNull
    T acquire(int duration, @NonNull TimeUnit timeUnit);

    void recycle(@NonNull String reservationId);

    void destroy();
}
