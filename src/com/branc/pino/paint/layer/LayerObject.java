package com.branc.pino.paint.layer;

import com.branc.pino.application.ApplicationError;
import com.branc.pino.core.util.Disposable;
import com.branc.pino.core.util.Disposer;
import com.branc.pino.ui.attr.NumberAttribute;
import com.branc.pino.ui.attr.ViewType;
import com.branc.pino.ui.editor.EditorTarget;

import java.awt.image.BufferedImage;
import java.beans.IntrospectionException;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

public abstract class LayerObject implements Disposable, EditorTarget {
    private final List<PropertyDescriptor> descriptors = new ArrayList<>();
    protected final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public static final String NAME = "pino:layer-object:name";
    public static final String OPACITY = "pino:layer-object:opacity";
    public static final String VISIBLE = "pino:layer-object:visible";
    public static final String ROUGH = "pino:layer-object:rough";

    public LayerObject() {
        try {
            define(NAME, "getName", "setName");
            PropertyDescriptor opacity = define(OPACITY, "getOpacity", "setOpacity");
            opacity.setDisplayName("不透明度");
            opacity.setValue(NumberAttribute.MIN.getAttributeName(), 0.0);
            opacity.setValue(NumberAttribute.MAX.getAttributeName(), 100.0);
            opacity.setValue(NumberAttribute.BLOCK_INCREMENT.getAttributeName(), 1.0);
            define(VISIBLE, "isVisible", "setVisible").setDisplayName("表示");
            define(ROUGH, "isRough", "setRough").setDisplayName("下書き");
        } catch (IntrospectionException e) {
            throw new ApplicationError(e);
        }
    }

    // -- Utilities for bean --

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


    // -- Common properties --

    private String name = getClass().getSimpleName();

    public final String getName() {
        return name;
    }

    public final void setName(String value) {
        if (value != null) {
            var old = name;
            name = value;
            support.firePropertyChange(NAME, old, value);
        }
    }

    private float opacity = 100f;

    public final void setOpacity(float value) {
        if (value < 0 || 100 < value) {
            throw new IllegalArgumentException("value must be between 0 and 1.");
        }
        if (value != opacity) {
            var old = opacity;
            opacity = value;
            support.firePropertyChange(OPACITY, old, value);
        }
    }

    public final float getOpacity() {
        return opacity;
    }


    private boolean isVisible = true;

    public final void setVisible(boolean value) {
        if (value != isVisible) {
            isVisible = value;
            support.firePropertyChange(VISIBLE, !value, value);
        }
    }

    public final boolean isVisible() {
        return isVisible;
    }


    private boolean isRough = false;

    public final void setRough(boolean value) {
        if (value != isRough) {
            isRough = value;
            support.firePropertyChange(ROUGH, !value, value);
        }
    }

    public final boolean isRough() {
        return isRough;
    }

    public abstract BufferedImage toImage();

    @Override
    public void dispose() {}
}
