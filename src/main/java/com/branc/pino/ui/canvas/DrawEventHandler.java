package com.branc.pino.ui.canvas;

import com.branc.pino.core.util.Disposable;

public interface DrawEventHandler<E> extends Disposable {
    void start();

    void pause();

    void resume();

    void shutdown();

    void enqueue(E e);
}
