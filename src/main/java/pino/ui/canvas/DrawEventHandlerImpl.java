package pino.ui.canvas;

import com.branc.pino.core.annotaion.Internal;
import com.branc.pino.brush.Brush;
import com.branc.pino.brush.event.DrawEvent;
import com.branc.pino.ui.canvas.DrawEventHandler;
import com.branc.pino.ui.input.MouseEvent;
import com.google.common.flogger.FluentLogger;

import java.util.concurrent.*;
import java.util.function.Supplier;

@Internal
public class DrawEventHandlerImpl implements DrawEventHandler<DrawEvent> {
    private static final FluentLogger log = FluentLogger.forEnclosingClass();

    private final Supplier<Brush<?>> brush;
    private final BlockingQueue<DrawEvent> queue;
    private final ScheduledExecutorService executor;


    public DrawEventHandlerImpl(Supplier<Brush<?>> brush) {
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
                DrawEvent e;
                try {
                    e = queue.poll(5, TimeUnit.MINUTES);
                } catch (InterruptedException interruptedException) {
                    return;
                }
                if (e != null) {
                    if (e.getEventType() == MouseEvent.PRESSED) {
                        brush = DrawEventHandlerImpl.this.brush.get();
                        if (brush == null) return;
                        brush.first(e);
                    } else if (e.getEventType() == MouseEvent.DRAGGED) {
                        if (brush == null) return;
                        brush.main(e);
                    } else if (e.getEventType() == MouseEvent.RELEASED) {
                        if (brush == null) return;
                        brush.last(e);
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
    public void enqueue(DrawEvent drawEvent) {
        if (!pausing) queue.add(drawEvent);
    }

    @Override
    public void dispose() {
        shutdown();
    }
}
