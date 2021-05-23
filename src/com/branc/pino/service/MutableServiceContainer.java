package com.branc.pino.service;

import java.util.function.Supplier;

public interface MutableServiceContainer extends ServiceContainer {
    <T> void register(Class<T> base, Supplier<? extends T> supplier);
    <T> void register(Class<T> base, T instance);
    void unregister(Class<?> base);
}
