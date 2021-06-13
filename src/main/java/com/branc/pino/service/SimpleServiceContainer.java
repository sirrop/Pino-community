package com.branc.pino.service;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SimpleServiceContainer implements MutableServiceContainer {
    private final Map<Class<?>, Supplier<?>> map = new HashMap<>();

    @Override
    public <T> void register(Class<T> base, Supplier<? extends T> supplier) {
        map.put(base, supplier);
    }

    @Override
    public <T> void register(Class<T> base, T instance) {
        map.put(base, () -> instance);
    }

    @Override
    public void unregister(Class<?> base) {
        map.remove(base);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getService(Class<T> serviceClass) {
        var supplier = map.get(serviceClass);
        if (supplier == null) {
            return null;
        }
        return (T) map.get(serviceClass).get();
    }
}
