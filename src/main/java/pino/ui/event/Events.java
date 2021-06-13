package pino.ui.event;

import com.branc.pino.ui.event.Event;
import com.branc.pino.ui.event.EventType;

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
