package pino.brush;

import com.branc.pino.brush.Brush;
import com.branc.pino.brush.BrushContext;
import com.branc.pino.core.annotaion.Internal;

@Internal
public class PencilContext extends BrushContext {
    public PencilContext() {
        setName("鉛筆");
    }

    @Override
    public Brush<? extends BrushContext> createBrush() {
        return new Pencil(this);
    }
}
