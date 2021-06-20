package jp.gr.java_conf.alpius.pino.ui.event;

import java.util.EventObject;

import static jp.gr.java_conf.alpius.pino.internal.ui.event.Events.newType;

public class Event extends EventObject {
    public static final EventType<Event> ROOT = newType(null, "Event", "Root");

    public Event(Object source) {
        super(source);
    }
}
