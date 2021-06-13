package com.branc.pino.brush.event;

import com.branc.pino.brush.BrushContext;
import com.branc.pino.brush.BrushManager;

import java.util.EventObject;

public class SelectedBrushChangedEvent extends EventObject {
    private final BrushContext oldBrush;
    private final BrushContext newBrush;

    public SelectedBrushChangedEvent(
            BrushManager source,
            BrushContext oldBrush,
            BrushContext newBrush
    ) {
        super(source);
        this.oldBrush = oldBrush;
        this.newBrush = newBrush;
    }

    public BrushManager getBrushManager() {
        return (BrushManager) getSource();
    }

    public BrushContext getOldBrush() {
        return oldBrush;
    }

    public BrushContext getNewBrush() {
        return newBrush;
    }
}
