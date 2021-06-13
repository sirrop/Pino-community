package com.branc.pino.brush;

import com.branc.pino.core.util.Disposable;
import com.branc.pino.brush.event.DrawEvent;
import com.branc.pino.layer.DrawableLayer;
import pino.brush.BrushHelper;

import java.awt.*;

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
                                }
        );
    }

    private final T context;
    private DrawableLayer layer;
    private Graphics2D g;

    private void setLayer(DrawableLayer layer) {
        this.layer = layer;
        this.g = layer.createGraphics();
        initGraphics();
    }

    private void initGraphics() {
        g.setColor(context.getColor());
        g.setStroke(new BasicStroke(context.getWidth(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, context.getOpacity() / 100f));
    }

    public Brush(T context) {
        this.context = context;
    }

    protected void drawLine(int x0, int y0, int x1, int y1) {
        g.drawLine(x0, y0, x1, y1);
    }

    public T getContext() {
        return context;
    }

    public abstract void first(DrawEvent e);

    public abstract void main(DrawEvent e);

    public abstract void last(DrawEvent e);

    @Override
    public void dispose() {
        g.dispose();
    }
}
