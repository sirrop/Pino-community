package jp.gr.java_conf.alpius.pino.internal.brush;

import jp.gr.java_conf.alpius.pino.brush.Brush;
import jp.gr.java_conf.alpius.pino.brush.BrushContext;
import jp.gr.java_conf.alpius.pino.core.annotaion.Internal;

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
