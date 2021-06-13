package com.branc.pino.ui.event;

import java.util.EventObject;

import static pino.ui.event.Events.newType;

public class Event extends EventObject {
    public static final EventType<Event> ROOT = newType(null, "Event", "Root");

    public Event(Object source) {
        super(source);
    }
}
