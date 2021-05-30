package com.branc.pino.core.history;

public interface Memento<T> {
    T getParent();
    void put(Object key, Object value);
    Object get(Object key);
}
