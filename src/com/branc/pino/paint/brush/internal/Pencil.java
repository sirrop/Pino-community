package com.branc.pino.paint.brush.internal;

import com.branc.pino.graphics.DrawingApi;
import com.branc.pino.graphics.PinoGraphics;
import com.branc.pino.paint.brush.Brush;
import com.google.common.flogger.FluentLogger;

import java.awt.*;

public class Pencil extends Brush<PencilContext> {
    private static final FluentLogger log = FluentLogger.forEnclosingClass();
    private final DrawingApi fill = fill();

    public Pencil(PencilContext context, PinoGraphics g) {
        super(context, g);
        float width = context.getWidth();
        fill.setStroke(new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        fill.setPaint(context.getColor());
    }

    @Override
    public void first(double x, double y) {
        fill.moveTo(x, y);
        log.atInfo().log("move to (%f, %f)", x, y);
    }

    @Override
    public void main(double x, double y) {
        fill.lineTo(x, y);
        log.atInfo().log("line to (%f, %f)", x, y);
    }

    @Override
    public void last(double x, double y) {
        fill.lineTo(x, y);
        log.atInfo().log("line to (%f, %f)", x, y);
    }
}
