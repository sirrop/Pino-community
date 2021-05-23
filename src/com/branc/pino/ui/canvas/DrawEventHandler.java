package com.branc.pino.ui.canvas;

import com.branc.pino.core.util.Disposable;
import com.branc.pino.paint.brush.Brush;
import javafx.scene.input.MouseEvent;

import java.util.function.Supplier;

public interface DrawEventHandler<E> extends Disposable {
    void start();
    void pause();
    void resume();
    void shutdown();
    void enqueue(E e);
    void setRate(double rate);
    double getRate();

    static DrawEventHandler<MouseEvent> createFxHandler(Supplier<Brush<?>> supplier) {
        return new FxHandler(supplier);
    }
}
