package jp.gr.java_conf.alpius.pino.internal.brush;

import jp.gr.java_conf.alpius.imagefx.Graphics;
import jp.gr.java_conf.alpius.pino.brush.Brush;
import jp.gr.java_conf.alpius.pino.core.annotaion.Internal;
import jp.gr.java_conf.alpius.pino.layer.DrawableLayer;

@Internal
public abstract class BrushHelper {
    private static BrushAccessor accessor;

    public interface BrushAccessor {
        void setHelper(Brush<?> brush, BrushHelper helper);

        BrushHelper getHelper(Brush<?> brush);

        void setTarget(Brush<?> brush, DrawableLayer target);

        DrawableLayer getTarget(Brush<?> brush);
        Graphics getGraphics(Brush<?> brush);
    }

    public static void setAccessor(BrushAccessor newAccessor) {
        if (accessor != null) throw new IllegalStateException();
        accessor = newAccessor;
    }

    public static BrushAccessor getAccessor() {
        if (accessor == null) throw new IllegalStateException();
        return accessor;
    }

    public static void setTarget(Brush<?> brush, DrawableLayer layer) {
        getAccessor().setTarget(brush, layer);
    }

    public static DrawableLayer getTarget(Brush<?> brush) {
        return getAccessor().getTarget(brush);
    }

    public static Graphics getGraphics(Brush<?> brush) {
        return getAccessor().getGraphics(brush);
    }
}
