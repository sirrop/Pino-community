package com.branc.pino.brush;

import com.branc.pino.application.ApplicationManager;
import com.branc.pino.brush.event.SelectedBrushChangeListener;
import com.branc.pino.brush.event.SelectedBrushChangedEvent;
import com.branc.pino.core.util.Disposable;
import com.branc.pino.core.util.Disposer;
import pino.brush.PencilContext;

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
