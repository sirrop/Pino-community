package jp.gr.java_conf.alpius.pino.internal.ui.event;

import jp.gr.java_conf.alpius.pino.ui.event.Event;
import jp.gr.java_conf.alpius.pino.ui.event.EventType;

public final class Events {
    private Events() {
    }

    public static <E extends Event> EventType<E> newType(EventType<?> root, String typeName, String eventName) {
        return new EventType<>() {
            @Override
            public EventType<? extends Event> getParent() {
                return root;
            }

            @Override
            public String toString() {
                return typeName + ":" + eventName;
            }
        };
    }
}
