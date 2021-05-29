package com.branc.pino.paint.brush;

import com.branc.pino.application.ApplicationError;
import com.branc.pino.core.util.Disposable;
import com.branc.pino.core.util.Disposer;
import com.branc.pino.graphics.PinoGraphics;
import com.branc.pino.ui.attr.NumberAttribute;
import com.branc.pino.ui.editor.EditorTarget;

import java.awt.*;
import java.beans.IntrospectionException;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

public abstract class BrushContext implements Disposable, EditorTarget {
    public BrushContext() {
        try {
            define(NAME, "getName", "setName");
            define(COLOR, "getColor", "setColor").setDisplayName("色");
            PropertyDescriptor width = define(WIDTH, "getWidth", "setWidth");
            width.setDisplayName("幅");
            width.setValue(NumberAttribute.MIN.getAttributeName(), 0.0);
            width.setValue(NumberAttribute.MAX.getAttributeName(), 200.0);
            width.setValue(NumberAttribute.BLOCK_INCREMENT.getAttributeName(), 1.0);
            PropertyDescriptor opacity = define(OPACITY, "getOpacity", "setOpacity");
            opacity.setDisplayName("不透明度");
            opacity.setValue(NumberAttribute.MIN.getAttributeName(), 0.0);
            opacity.setValue(NumberAttribute.MAX.getAttributeName(), 100.0);
            opacity.setValue(NumberAttribute.BLOCK_INCREMENT.getAttributeName(), 1.0);
        } catch (IntrospectionException e) {
            throw new ApplicationError(e);
        }
    }

    public static final String NAME = "name";
    public static final String COLOR = "color";
    public static final String WIDTH = "width";
    public static final String OPACITY = "opacity";

    private final List<PropertyDescriptor> descriptors = new ArrayList<>();
    protected final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public List<PropertyDescriptor> getPropertyDescList() {
        return Collections.unmodifiableList(descriptors);
    }

    protected PropertyDescriptor define(String propertyName) throws IntrospectionException {
        var res = new PropertyDescriptor(propertyName, getClass());
        descriptors.add(res);
        return res;
    }

    protected PropertyDescriptor define(String propertyName, String readMethodName, String writeMethodName) throws IntrospectionException {
        var res = new PropertyDescriptor(propertyName, getClass(), readMethodName, writeMethodName);
        descriptors.add(res);
        return res;
    }

    public PropertyDescriptor lookup(String propertyName) {
        for (PropertyDescriptor descriptor: descriptors) {
            if (descriptor.getName().equals(propertyName)) {
                return descriptor;
            }
        }
        throw new NoSuchElementException();
    }

    public final void addListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public final void addListener(PropertyChangeListener listener, Disposable parent) {
        addListener(listener);
        Disposer.registerDisposable(parent, () -> removeListener(listener));
    }

    public final void removeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }



    private String name = getClass().getSimpleName();
    public final void setName(String name) {
        this.name = name;
    }
    public final String getName() {
        return name;
    }

    private float width = 5;

    public final void setWidth(float value) {
        if (value < 0) {
            throw new IllegalArgumentException("value must be more than or equal to 0.");
        }
        var old = width;
        width = value;
        support.firePropertyChange(WIDTH, old, value);
    }

    public final float getWidth() {
        return width;
    }

    private float opacity = 100f;

    public final void setOpacity(float value) {
        if (value < 0 || 100 < value) {
            throw new IllegalArgumentException("value must be between 0 and 1.");
        }
        var old = opacity;
        opacity = value;
        support.firePropertyChange(OPACITY, old, value);
    }

    public final float getOpacity() {
        return opacity;
    }

    private Color color = Color.BLACK;

    public final void setColor(Color value) {
        if (value != null) {
            var old = color;
            color = value;
            support.firePropertyChange(COLOR, old, value);
        }
    }

    public final Color getColor() {
        return color;
    }

    public abstract Brush<? extends BrushContext> createBrush(PinoGraphics g);

    @Override
    public void dispose() {}
}
