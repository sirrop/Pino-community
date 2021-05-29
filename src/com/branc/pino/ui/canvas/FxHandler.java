package com.branc.pino.ui.canvas;

import com.branc.pino.paint.brush.Brush;
import com.google.common.flogger.FluentLogger;
import javafx.scene.input.MouseEvent;
import org.apache.commons.lang3.time.StopWatch;

import java.util.concurrent.*;
import java.util.function.Supplier;

class FxHandler implements DrawEventHandler<MouseEvent> {
    private static final FluentLogger log = FluentLogger.forEnclosingClass();

    private final Supplier<Brush<?>> brush;
    private final BlockingQueue<MouseEvent> queue;
    private final ScheduledExecutorService executor;


    public FxHandler(Supplier<Brush<?>> brush) {
        this.brush = brush;
        this.queue = new ArrayBlockingQueue<>(1024);
        executor = Executors.newScheduledThreadPool(1);
    }

    @Override
    public void start() {
        var task = new Runnable() {
            private Brush<?> brush;
            @Override
            public void run() {
                MouseEvent e;
                try {
                    e = queue.poll(5, TimeUnit.MINUTES);
                } catch (InterruptedException interruptedException) {
                    return;
                }
                if (e != null) {
                    if (e.getEventType().equals(MouseEvent.MOUSE_PRESSED)) {
                        brush = FxHandler.this.brush.get();
                        if (brush == null) return;
                        brush.first(e.getX(), e.getY());
                    } else if (e.getEventType().equals(MouseEvent.MOUSE_DRAGGED)) {
                        if (brush == null) return;
                        brush.main(e.getX(), e.getY());
                    } else if (e.getEventType().equals(MouseEvent.MOUSE_RELEASED)) {
                        if (brush == null) return;
                        brush.last(e.getX(), e.getY());
                        brush.dispose();
                    }
                }
            }
        };
        executor.scheduleAtFixedRate(task, 0, 1, TimeUnit.NANOSECONDS);
    }

    @Override
    public void pause() {
        log.atInfo().log();
        pausing = true;
    }

    @Override
    public void resume() {
        log.atInfo().log();
        pausing = false;
    }

    @Override
    public void shutdown() {
        log.atInfo().log();
        executor.shutdownNow();
    }

    private boolean pausing = false;

    @Override
    public void enqueue(MouseEvent mouseEvent) {
        if (!pausing) queue.add(mouseEvent);
    }

    @Override
    public void dispose() {
        shutdown();
    }
}
