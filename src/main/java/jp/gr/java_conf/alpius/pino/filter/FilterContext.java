package jp.gr.java_conf.alpius.pino.filter;

import jp.gr.java_conf.alpius.pino.core.util.Disposable;
import jp.gr.java_conf.alpius.pino.internal.beans.BeenPeer;
import jp.gr.java_conf.alpius.pino.ui.editor.EditorTarget;

import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.util.List;

public abstract class FilterContext implements Disposable, EditorTarget {
    private BeenPeer<FilterContext> peer;

    public abstract Filter createFilter();

    private BeenPeer<FilterContext> getPeer() {
        if (peer == null) {
            peer = new BeenPeer<>(this);
        }

        return peer;
    }

    @Override
    public void dispose() {

    }

    @Override
    public List<PropertyDescriptor> getUnmodifiablePropertyList() {
        return getPeer().getUnmodifiableProperties();
    }

    @Override
    public void addListener(PropertyChangeListener listener) {
        getPeer().addListener(listener);
    }

    @Override
    public void removeListener(PropertyChangeListener listener) {
        getPeer().removeListener(listener);
    }
}
