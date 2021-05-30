package com.branc.pino.paint.layer;

import com.branc.pino.application.ApplicationError;
import com.branc.pino.core.history.Memento;
import com.branc.pino.core.history.MementoException;
import com.branc.pino.core.history.RestoreException;
import com.branc.pino.core.history.SimpleMemento;
import com.branc.pino.core.util.Disposable;
import com.branc.pino.core.util.Disposer;
import com.branc.pino.core.util.UpdateListener;
import com.branc.pino.ui.attr.NumberAttribute;
import com.branc.pino.ui.editor.EditorTarget;

import java.awt.image.BufferedImage;
import java.beans.IntrospectionException;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public abstract class LayerObject implements Disposable, EditorTarget, Serializable {
    private static final long serialVersionUID = 42L;

    private transient List<PropertyDescriptor> descriptors;
    protected transient PropertyChangeSupport support;

    public static final String NAME = "pino:layer-object:name";
    public static final String OPACITY = "pino:layer-object:opacity";
    public static final String VISIBLE = "pino:layer-object:visible";
    public static final String ROUGH = "pino:layer-object:rough";

    public LayerObject() {
        init();
    }
    private void init() {
        try {
            descriptors = new ArrayList<>();
            support = new PropertyChangeSupport(this);
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

    private final List<UpdateListener<LayerObject>> listenerList = new ArrayList<>();

    public void addHistoryListener(UpdateListener<LayerObject> listener) {
        listenerList.add(listener);
    }

    public void addHistoryListener(UpdateListener<LayerObject> listener, Disposable parent) {
        addHistoryListener(listener);
        Disposer.registerDisposable(parent, () -> removeHistoryListener(listener));
    }

    public void removeHistoryListener(UpdateListener<LayerObject> listener) {
        listenerList.remove(listener);
    }

    protected void requestRegisterMemento() {
        listenerList.forEach(it -> it.updated(this));
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

    private LayerState state = LayerState.STABLE;
    public LayerState getState() {
        return state;
    }

    private String name = getClass().getSimpleName();

    public final String getName() {
        return name;
    }

    public final void setName(String value) {
        if (state == LayerState.STABLE) {
            requestRegisterMemento();
        }
        if (value != null) {
            var old = name;
            name = value;
            support.firePropertyChange(NAME, old, value);
        }
    }

    private float opacity = 100f;

    public final void setOpacity(float value) {
        if (state == LayerState.STABLE) {
            requestRegisterMemento();
        }
        if (value < 0 || 100 < value) {
            throw new IllegalArgumentException("value must be between 0 and 100.");
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
        if (state == LayerState.STABLE) {
            requestRegisterMemento();
        }
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
        if (state == LayerState.STABLE) {
            requestRegisterMemento();
        }
    }

    public final boolean isRough() {
        return isRough;
    }

    public abstract BufferedImage toImage();

    @Override
    public void dispose() {}

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        init();
        ois.defaultReadObject();
        String name = (String) ois.readObject();
        float opacity = ois.readFloat();
        boolean isVisible = ois.readBoolean();
        boolean isRough = ois.readBoolean();
        if (opacity < 0 || 100 < opacity) throw new InvalidObjectException("opacity must be between 0 and 100.");
        setName(name);
        setOpacity(opacity);
        setVisible(isVisible);
        setRough(isRough);
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeObject(name);
        oos.writeFloat(opacity);
        oos.writeBoolean(isVisible);
        oos.writeBoolean(isRough);
    }

    public Memento<LayerObject> createMemento() throws MementoException {
        try {
            Memento<LayerObject> memento = new SimpleMemento<>(this);
            for (PropertyDescriptor desc: getPropertyDescList()) {
                memento.put(desc, desc.getReadMethod().invoke(this));
            }
            return memento;
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new MementoException(e);
        }
    }

    /**
     * LayerObjectの状態を復元します。デフォルトでは、getPropertyDescList()で返されるPropertyDescriptor
     * の値を復元します。getPropertyDescList()で返されない値を復元する必要がある場合は、このメソッドをオーバーライドしてください。
     * @param memento 復元する状態
     * @throws MementoException Memento関連の何かしらの例外が発生した際に投げられます
     * @throws RestoreException 復元時に何かしらの例外が発生した際に投げられます
     */
    public void restore(Memento<LayerObject> memento) throws MementoException {
        try {
            state = LayerState.RESTORING;
            for (PropertyDescriptor desc : getPropertyDescList()) {
                desc.getWriteMethod().invoke(this, memento.get(desc));
            }
            state = LayerState.STABLE;
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RestoreException(e);
        }
    }
}
