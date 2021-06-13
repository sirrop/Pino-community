package com.branc.pino.ui.input;

import com.branc.pino.ui.event.Event;
import com.branc.pino.ui.event.EventType;
import pino.ui.event.Events;

public class MouseEvent extends Event {
    public static final EventType<MouseEvent> ANY = Events.newType(Event.ROOT, "MouseEvent", "Any");
    public static final EventType<MouseEvent> CLICKED = Events.newType(ANY, "MouseEvent", "Clicked");
    public static final EventType<MouseEvent> PRESSED = Events.newType(ANY, "MouseEvent", "Pressed");
    public static final EventType<MouseEvent> RELEASED = Events.newType(ANY, "MouseEvent", "Released");
    public static final EventType<MouseEvent> DRAGGED = Events.newType(ANY, "MouseEvent", "Dragged");

    private final EventType<MouseEvent> eventType;
    private final double x;
    private final double y;
    private final double sceneX;
    private final double sceneY;

    public EventType<MouseEvent> getEventType() {
        return eventType;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getSceneX() {
        return sceneX;
    }

    public double getSceneY() {
        return sceneY;
    }

    public MouseButton getButton() {
        return button;
    }

    public boolean isShiftDown() {
        return isShiftDown;
    }

    public boolean isAltDown() {
        return isAltDown;
    }

    public boolean isCtrlDown() {
        return isCtrlDown;
    }

    public boolean isMetaDown() {
        return isMetaDown;
    }

    private final MouseButton button;
    private final boolean isShiftDown;
    private final boolean isAltDown;
    private final boolean isCtrlDown;
    private final boolean isMetaDown;

    public MouseEvent(
            Object source,
            EventType<MouseEvent> eventType,
            double x, double y,
            double sceneX, double sceneY,
            MouseButton button,
            boolean isShiftDown,
            boolean isAltDown,
            boolean isCtrlDown,
            boolean isMetaDown
    ) {
        super(source);
        this.eventType = eventType;
        this.x = x;
        this.y = y;
        this.sceneX = sceneX;
        this.sceneY = sceneY;
        this.button = button;
        this.isShiftDown = isShiftDown;
        this.isAltDown = isAltDown;
        this.isCtrlDown = isCtrlDown;
        this.isMetaDown = isMetaDown;
    }
}
