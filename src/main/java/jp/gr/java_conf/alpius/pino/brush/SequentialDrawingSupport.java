package jp.gr.java_conf.alpius.pino.brush;

import com.google.common.flogger.FluentLogger;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import jp.gr.java_conf.alpius.imagefx.Graphics;
import jp.gr.java_conf.alpius.pino.core.util.Disposable;
import jp.gr.java_conf.alpius.pino.internal.brush.BrushHelper;
import jp.gr.java_conf.alpius.pino.internal.layer.DrawableHelper;

public final class SequentialDrawingSupport implements Disposable {
    private static final FluentLogger log = FluentLogger.forEnclosingClass();

    private final Brush<?> brush;

    private int[] tmp;
    private WritableImage surface;
    private Graphics g;

    // 初期化は初めて使われるタイミングまで遅延
    public SequentialDrawingSupport(Brush<?> brush) {
        this.brush = brush;
    }

    private void init() {
        log.atFine().log("initialize SequentialDrawingSupport for %s", brush);
        surface = DrawableHelper.getImage(
                BrushHelper.getTarget(brush)
        );
        int w = (int) surface.getWidth();
        int h = (int) surface.getHeight();
        tmp = new int[w * h];
        surface.getPixelReader().getPixels(0, 0, w, h, PixelFormat.getIntArgbInstance(), tmp, 0, w);
        g = BrushHelper.getGraphics(brush);
    }

    private boolean initialized = false;
    private void initOnce() {
        if (!initialized) {
            initialized = true;
            init();
        }
    }

    private void undo() {
        surface.getPixelWriter().setPixels(0, 0, (int) surface.getWidth(), (int) surface.getHeight(), PixelFormat.getIntArgbInstance(), tmp, 0, (int) surface.getWidth());
        g.updateSurface();
    }

    public void begin() {
        initOnce();
        g.beginPath();
    }

    public void moveTo(double x, double y) {
        g.moveTo(x, y);
    }

    public void lineTo(double x, double y) {
        undo();
        g.lineTo(x, y);
        g.stroke();
    }

    @Override
    public void dispose() {

    }
}
