package jp.gr.java_conf.alpius.pino.brush;

import jp.gr.java_conf.alpius.pino.application.ApplicationManager;
import jp.gr.java_conf.alpius.pino.brush.event.SelectedBrushChangeListener;
import jp.gr.java_conf.alpius.pino.brush.event.SelectedBrushChangedEvent;
import jp.gr.java_conf.alpius.pino.core.util.Disposable;
import jp.gr.java_conf.alpius.pino.core.util.Disposer;
import jp.gr.java_conf.alpius.pino.internal.brush.PencilContext;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BrushManager extends AbstractList<BrushContext> {
    public static BrushManager getInstance() {
        return ApplicationManager.getApp().getService(BrushManager.class);
    }

    private final List<BrushContext> contexts;


    public BrushManager(List<BrushContext> contexts) {
        this.contexts = contexts;
        contexts.add(new PencilContext());
        select(0);
    }

    @Override
    public int size() {
        return contexts.size();
    }

    @Override
    public BrushContext get(int index) {
        return contexts.get(index);
    }

    private final List<SelectedBrushChangeListener> selectedBrushChangeListenerList = new ArrayList<>();

    public void addSelectedBrushChangeListener(SelectedBrushChangeListener listener) {
        selectedBrushChangeListenerList.add(listener);
    }

    public void addSelectedBrushChangeListener(SelectedBrushChangeListener listener, Disposable parent) {
        addSelectedBrushChangeListener(listener);
        Disposer.registerDisposable(parent, () -> removeSelectedBrushChangeListener(listener));
    }

    public void removeSelectedBrushChangeListener(SelectedBrushChangeListener listener) {
        selectedBrushChangeListenerList.remove(listener);
    }

    private void fireSelectedBrushChanged(BrushContext oldBrush, BrushContext newBrush) {
        var e = new SelectedBrushChangedEvent(this, oldBrush, newBrush);
        var list = new LinkedList<>(selectedBrushChangeListenerList);
        for (SelectedBrushChangeListener listener : list) {
            listener.selectedBrushChanged(e);
        }
    }

    private BrushContext selectedBrush;

    public void select(BrushContext context) {
        if (contexts.contains(context)) {
            selectedBrush = context;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void select(int index) {
        select(contexts.get(index));
    }

    public BrushContext getSelectedBrush() {
        return selectedBrush;
    }
}
