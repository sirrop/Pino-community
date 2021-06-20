package jp.gr.java_conf.alpius.pino.ui.canvas;

import jp.gr.java_conf.alpius.pino.core.util.Disposable;

public interface DrawEventHandler<E> extends Disposable {
    void start();

    void pause();

    void resume();

    void shutdown();

    void enqueue(E e);
}
