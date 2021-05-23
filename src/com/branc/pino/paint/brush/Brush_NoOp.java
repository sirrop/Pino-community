package com.branc.pino.paint.brush;

import com.branc.pino.graphics.PinoGraphics;
import com.google.common.flogger.FluentLogger;

public class Brush_NoOp extends Brush<BrushContext> {
    private static final FluentLogger log = FluentLogger.forEnclosingClass();
    private static final Brush<?> instance = new Brush_NoOp(null, null);

    public static Brush<?> getInstance() {
        return instance;
    }

    public Brush_NoOp(BrushContext context, PinoGraphics g) {
        super(context, g);
    }

    @Override
    public void first(double x, double y) {

    }

    @Override
    public void main(double x, double y) {

    }

    @Override
    public void last(double x, double y) {
    }
}
