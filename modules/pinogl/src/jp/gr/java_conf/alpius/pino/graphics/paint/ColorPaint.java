package jp.gr.java_conf.alpius.pino.graphics.paint;

import jp.gr.java_conf.alpius.pino.graphics.Color;
import jp.gr.java_conf.alpius.pino.graphics.Composite;

import java.util.Collection;

public final class ColorPaint extends Paint {
    private final Color color;

    public ColorPaint() {
        this(null);
    }

    public ColorPaint(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public ColorPaint setAntialias(boolean value) {
        return (ColorPaint) super.setAntialias(value);
    }

    @Override
    public ColorPaint setComposite(Composite composite) {
        return (ColorPaint) super.setComposite(composite);
    }

    @Override
    public ColorPaint setOpacity(float opacity) {
        return (ColorPaint) super.setOpacity(opacity);
    }

    @Override
    public ColorPaint addStroke(Stroke stroke) {
        return (ColorPaint) super.addStroke(stroke);
    }

    @Override
    public ColorPaint addStrokes(Collection<? extends Stroke> c) {
        return (ColorPaint) super.addStrokes(c);
    }

    @Override
    public ColorPaint removeStroke(Stroke stroke) {
       return (ColorPaint) super.removeStroke(stroke);
    }

    @Override
    public ColorPaint removeStrokes(Collection<? extends Stroke> c) {
        return (ColorPaint) super.removeStrokes(c);
    }

    @Override
    public ColorPaint clearStrokes() {
        return (ColorPaint) super.clearStrokes();
    }
}
