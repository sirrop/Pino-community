package com.branc.pino.brush.event;


import com.branc.pino.layer.DrawableLayer;
import com.branc.pino.ui.event.EventType;
import com.branc.pino.ui.input.MouseButton;
import com.branc.pino.ui.input.MouseEvent;

import java.util.EventObject;
import java.util.Objects;

public class DrawEvent extends EventObject {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DrawEvent drawEvent = (DrawEvent) o;
        return Double.compare(drawEvent.x, x) == 0 && Double.compare(drawEvent.y, y) == 0 && mouseButton == drawEvent.mouseButton && layerObject.equals(drawEvent.layerObject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mouseButton, x, y, layerObject);
    }

    private final MouseButton mouseButton;
    private final EventType<MouseEvent> eventType;
    private final double x;
    private final double y;
    private final DrawableLayer layerObject;

    @Override
    public String toString() {
        return "DrawEvent{" +
                "mouseButton=" + mouseButton +
                ", x=" + x +
                ", y=" + y +
                ", layer=" + layerObject +
                '}';
    }

    public DrawEvent(
            MouseButton mouseButton,
            EventType<MouseEvent> eventType,
            double x,
            double y,
            DrawableLayer layer
    ) {
        super(layer);
        this.mouseButton = mouseButton;
        this.eventType = eventType;
        this.x = x;
        this.y = y;
        this.layerObject = layer;
    }

    public MouseButton getMouseButton() {
        return mouseButton;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public DrawableLayer getLayer() {
        return layerObject;
    }

    public EventType<MouseEvent> getEventType() {
        return eventType;
    }
}
