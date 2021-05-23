package com.branc.pino.ui.editor;

import com.branc.pino.core.util.Disposable;
import com.branc.pino.core.util.Disposer;

import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.util.List;

public interface EditorTarget {
    List<PropertyDescriptor> getPropertyDescList();
    void addListener(PropertyChangeListener listener);
    default void addListener(PropertyChangeListener listener, Disposable parent) {
        addListener(listener);
        Disposer.registerDisposable(parent, () -> removeListener(listener));
    }
    void removeListener(PropertyChangeListener listener);
    PropertyDescriptor lookup(String name);
}
