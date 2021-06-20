package jp.gr.java_conf.alpius.pino.ui.editor;

import jp.gr.java_conf.alpius.pino.core.util.Disposable;
import jp.gr.java_conf.alpius.pino.core.util.Disposer;

import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.util.List;

public interface EditorTarget {
    List<PropertyDescriptor> getUnmodifiablePropertyList();
    void addListener(PropertyChangeListener listener);
    default void addListener(PropertyChangeListener listener, Disposable parent) {
        addListener(listener);
        Disposer.registerDisposable(parent, () -> removeListener(listener));
    }
    void removeListener(PropertyChangeListener listener);
}
