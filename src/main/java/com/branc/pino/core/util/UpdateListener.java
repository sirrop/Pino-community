package com.branc.pino.core.util;

import java.util.EventListener;

public interface UpdateListener<T> extends EventListener {
    void updated(T source);
}
