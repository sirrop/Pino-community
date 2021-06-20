package jp.gr.java_conf.alpius.pino.brush.event;

import java.util.EventListener;

public interface SelectedBrushChangeListener extends EventListener {
    void selectedBrushChanged(SelectedBrushChangedEvent e);
}
