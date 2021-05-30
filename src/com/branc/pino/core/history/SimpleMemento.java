package com.branc.pino.core.history;

import java.util.HashMap;
import java.util.Map;

public class SimpleMemento<T> implements Memento<T> {
    private final T instance;
    private final Map<Object, Object> properties = new HashMap<>();

    public SimpleMemento(T instance) {
        this.instance = instance;
    }

    @Override
    public T getParent() {
        return instance;
    }

    @Override
    public void put(Object key, Object value) {
        properties.put(key, value);
    }

    @Override
    public Object get(Object key) {
        return properties.get(key);
    }
}
