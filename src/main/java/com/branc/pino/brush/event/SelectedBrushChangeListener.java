package com.branc.pino.brush.event;

import java.util.EventListener;

public interface SelectedBrushChangeListener extends EventListener {
    void selectedBrushChanged(SelectedBrushChangedEvent e);
}
