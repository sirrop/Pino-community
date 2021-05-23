package com.branc.pino.paint.brush;

import com.branc.pino.application.ApplicationManager;
import com.branc.pino.paint.brush.internal.PencilContext;

import java.util.ArrayList;
import java.util.List;

public class BrushManager {
    public static BrushManager getInstance() {
        return ApplicationManager.getApp().getService(BrushManager.class);
    }
    private final List<BrushContext> contexts = new ArrayList<>();

    public BrushManager() {
        contexts.add(new PencilContext());
        select(0);
    }

    private BrushContext selectedBrush;

    public List<BrushContext> getContexts() {
        return contexts;
    }

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
