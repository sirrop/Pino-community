package pino.brush;

import com.branc.pino.brush.Brush;
import com.branc.pino.core.annotaion.Internal;
import com.branc.pino.layer.DrawableLayer;

@Internal
public abstract class BrushHelper {
    private static BrushAccessor accessor;

    public interface BrushAccessor {
        void setHelper(Brush<?> brush, BrushHelper helper);

        BrushHelper getHelper(Brush<?> brush);

        void setTarget(Brush<?> brush, DrawableLayer target);
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
}
