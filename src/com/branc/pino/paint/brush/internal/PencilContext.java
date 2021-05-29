package com.branc.pino.paint.brush.internal;

import com.branc.pino.graphics.PinoGraphics;
import com.branc.pino.paint.brush.Brush;
import com.branc.pino.paint.brush.BrushContext;

public class PencilContext extends BrushContext {
    public PencilContext() {
        setName("鉛筆");
    }

    @Override
    public Brush<? extends BrushContext> createBrush(PinoGraphics g) {
        return new Pencil(this, g);
    }
}
