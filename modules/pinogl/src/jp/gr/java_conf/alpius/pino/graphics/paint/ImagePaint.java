package jp.gr.java_conf.alpius.pino.graphics.paint;

import jp.gr.java_conf.alpius.pino.graphics.Composite;
import jp.gr.java_conf.alpius.pino.graphics.Image;
import jp.gr.java_conf.alpius.pino.graphics.utils.Checks;

public final class ImagePaint extends Paint {
    private final Image image;
    private final int x;
    private final int y;
    private final int w;
    private final int h;

    public ImagePaint(boolean antialias, Composite composite, float opacity, Image image, int x, int y, int w, int h) {
        super(antialias, composite, opacity);
        Checks.require(w > 0, "w <= 0");
        Checks.require(h > 0, "h <= 0");
        this.image = image;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public Image getImage() {
        return image;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return w;
    }

    public int getHeight() {
        return h;
    }

    @Override
    public PaintBuilder toBuilder() {
        return new ImagePaintBuilder()
                .setAntialias(isAntialias())
                .setComposite(getComposite())
                .setOpacity(getOpacity())
                .setImage(image)
                .setPosition(x, y)
                .setSize(w, h);
    }
}
