package jp.gr.java_conf.alpius.pino.graphics.paint;

import jp.gr.java_conf.alpius.pino.graphics.Color;
import jp.gr.java_conf.alpius.pino.graphics.Composite;

public final class ColorPaint extends Paint {
    private final Color color;

    public ColorPaint(boolean antialias, Composite composite, float opacity, Color color) {
        super(antialias, composite, opacity);
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public ColorPaintBuilder toBuilder() {
        return new ColorPaintBuilder()
                .setAntialias(isAntialias())
                .setComposite(getComposite())
                .setOpacity(getOpacity())
                .setColor(color);
    }
}
