package com.branc.pino.paint.brush.event;

import java.util.EventListener;

public interface SelectedBrushChangeListener extends EventListener {
    void selectedBrushChanged(SelectedBrushChangedEvent e);
}
