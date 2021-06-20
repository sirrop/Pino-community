package jp.gr.java_conf.alpius.pino.brush;

import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import jp.gr.java_conf.alpius.imagefx.Graphics;
import jp.gr.java_conf.alpius.pino.brush.event.DrawEvent;
import jp.gr.java_conf.alpius.pino.core.util.Disposable;
import jp.gr.java_conf.alpius.pino.internal.brush.BrushHelper;
import jp.gr.java_conf.alpius.pino.internal.layer.DrawableHelper;
import jp.gr.java_conf.alpius.pino.internal.util.FXUtils;
import jp.gr.java_conf.alpius.pino.layer.DrawableLayer;

public abstract class Brush<T extends BrushContext> implements Disposable {
    private BrushHelper helper = null;

    static {
        BrushHelper.setAccessor(new BrushHelper.BrushAccessor() {
                                    @Override
                                    public void setHelper(Brush<?> brush, BrushHelper helper) {
                                        brush.helper = helper;
                                    }

                                    @Override
                                    public BrushHelper getHelper(Brush<?> brush) {
                                        return brush.helper;
                                    }

                                    @Override
                                    public void setTarget(Brush<?> brush, DrawableLayer target) {
                                        brush.setLayer(target);
                                    }

                                    @Override
                                    public DrawableLayer getTarget(Brush<?> brush) {
                                        return brush.layer;
                                    }

                                    @Override
                                    public Graphics getGraphics(Brush<?> brush) {
                                        return brush.g;
                                    }
                                }
        );
    }

    private final T context;
    private DrawableLayer layer;
    private Graphics g;

    private void setLayer(DrawableLayer layer) {
        this.layer = layer;
        this.g = DrawableHelper.getGraphics(layer);
        initGraphics();
    }

    private void initGraphics() {
        g.setFill(FXUtils.toFX(context.getColor()));
        g.setStroke(FXUtils.toFX(context.getColor()));
        g.setLineWidth(context.getWidth());
        g.setGlobalAlpha(context.getOpacity() / 100);
        g.setGlobalBlendMode(FXUtils.toFX(context.getBlendMode()));
        g.setLineCap(StrokeLineCap.ROUND);
        g.setLineJoin(StrokeLineJoin.ROUND);
        init();
    }

    /**
     * ブラシを初期化します。
     * このメソッドは描画が始まる前に一度だけ呼び出されます。
     * <p>
     *     このメソッドをオーバーライドすることで、コンストラクタではできないブラシの初期化を行えます。
     *     なお、以下の初期化はデフォルトで行われるため、開発者は明示的に初期化する必要はありません
     * </p>
     * <table>
     *     <tr><th>Graphicsプロパティ名</th> <th>値</th></tr>
     *     <tr><td>Fill</td><td>color</td></tr>
     *     <tr><td>Stroke</td><td>color</td></tr>
     *     <tr><td>LineWidth</td><td>width</td></tr>
     *     <tr><td>GlobalAlpha</td><td>opacity</td></tr>
     *     <tr><td>GlobalBlendMode</td><td>blendMode</td></tr>
     * </table>
     */
    protected void init() {

    }

    public Brush(T context) {
        this.context = context;
    }

    public final T getContext() {
        return context;
    }

    protected final void drawLine(double x0, double y0, double x1, double y1) {
        g.strokeLine(x0, y0, x1, y1);
    }

    public abstract void first(DrawEvent e);

    public abstract void main(DrawEvent e);

    public abstract void last(DrawEvent e);

    @Override
    public void dispose() {

    }
}
