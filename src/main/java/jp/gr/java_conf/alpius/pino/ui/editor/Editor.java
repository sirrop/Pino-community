package jp.gr.java_conf.alpius.pino.ui.editor;

public interface Editor<T extends EditorTarget> {
    void setTarget(T target);

    T getTarget();
}
