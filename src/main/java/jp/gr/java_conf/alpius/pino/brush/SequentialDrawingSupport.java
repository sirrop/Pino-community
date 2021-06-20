package jp.gr.java_conf.alpius.pino.brush;

import com.google.common.flogger.FluentLogger;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import jp.gr.java_conf.alpius.imagefx.Graphics;
import jp.gr.java_conf.alpius.pino.core.util.Disposable;
import jp.gr.java_conf.alpius.pino.internal.brush.BrushHelper;
import jp.gr.java_conf.alpius.pino.internal.layer.DrawableHelper;

/**
 * 連続的な描画をサポートするクラスです。
 * <p>
 *     {@link Brush#drawLine(double, double, double, double)}を連続で使用すると、
 *     前回描画した部分と重なり、不透明度をさげたときに想定と異なった動作をする場合があります。
 * </p>
 * <p>
 *     このクラスを使用すると、描画をひとつのパスで行うことができます。
 * </p>
 */
public final class SequentialDrawingSupport implements Disposable {
    private static final FluentLogger log = FluentLogger.forEnclosingClass();

    private final Brush<?> brush;

    private int[] tmp;
    private WritableImage surface;
    private Graphics g;

    /**
     * 新しくSequentialDrawingSupportを作成します
     * @param brush このクラスを使用するブラシ
     */
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

    /**
     * 現在のパスを空にリセットし、初期化も同時に行います。
     */
    public void begin() {
        initOnce();
        g.beginPath();
    }

    /**
     * 現在のパスに対して、指定されたx, y座標への移動コマンドを実行します。
     * 座標は、パスに追加された時点の最新の変換によって変換され、その後変換に加えられた変更の影響を受けません。
     * @param x 移動するx座標
     * @param y 移動するy座標
     */
    public void moveTo(double x, double y) {
        g.moveTo(x, y);
    }

    /**
     * 指定されたx,y座標に直線を作成します。
     * @param x 線の終点のx座標
     * @param y 線の終点のy座標
     */
    public void lineTo(double x, double y) {
        undo();
        g.lineTo(x, y);
        g.stroke();
    }

    /**
     * 指定された座標を使用して、3次ベジェ曲線を作成します
     * @param xc1 最初のベジェ制御点のx座標
     * @param yc1 最初のベジェ制御点のy座標
     * @param xc2 2番目のベジェ制御店のx座標
     * @param yc2 2番目のベジェ制御店のy座標
     * @param x1 終点のx座標
     * @param y1 終点のy座標
     */
    public void bezierCurveTo(double xc1, double yc1, double xc2, double yc2, double x1, double y1) {
        undo();
        g.bezierCurveTo(xc1, yc1, xc2, yc2, x1, y1);
        g.stroke();
    }

    /**
     * 指定された座標を使用して、2次ベジェ曲線を作成します。
     * @param xc 制御点のx座標
     * @param yc 制御点のy座標
     * @param x1 終点のx座標
     * @param y1 終点のy座標
     */
    public void quadraticCurveTo(double xc, double yc, double x1, double y1) {
        undo();
        g.quadraticCurveTo(xc, yc, x1, y1);
        g.stroke();
    }

    @Override
    public void dispose() {

    }
}
