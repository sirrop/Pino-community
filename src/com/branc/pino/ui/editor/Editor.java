package com.branc.pino.ui.editor;

public interface Editor<T extends EditorTarget> {
    void setTarget(T target);
    T getTarget();
}
