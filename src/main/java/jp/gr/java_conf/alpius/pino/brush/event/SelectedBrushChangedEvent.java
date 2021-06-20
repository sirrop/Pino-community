package jp.gr.java_conf.alpius.pino.brush.event;

import jp.gr.java_conf.alpius.pino.brush.BrushContext;
import jp.gr.java_conf.alpius.pino.brush.BrushManager;

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
