package com.branc.pino.ui.event;

public interface EventType<E extends Event> {
    EventType<? extends Event> getParent();
}
