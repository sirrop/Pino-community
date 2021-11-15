package jp.gr.java_conf.alpius.pino.graphics.paint;

import jp.gr.java_conf.alpius.pino.graphics.Composite;
import jp.gr.java_conf.alpius.pino.graphics.Image;

import java.util.Collection;
import java.util.Objects;

public final class ImagePaint extends Paint {
    private final Image image;
    private final int x;
    private final int y;
    private final int w;
    private final int h;

    public ImagePaint(Image image) {
        this(image, 0, 0, image.getWidth(), image.getHeight());
    }

    public ImagePaint(Image image, int x, int y, int w, int h) {
        this.image = Objects.requireNonNull(image, "image == null");
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
    public ImagePaint setAntialias(boolean value) {
        return (ImagePaint) super.setAntialias(value);
    }

    @Override
    public ImagePaint setComposite(Composite composite) {
        return (ImagePaint) super.setComposite(composite);
    }

    @Override
    public ImagePaint setOpacity(float opacity) {
        return (ImagePaint) super.setOpacity(opacity);
    }

    @Override
    public ImagePaint addStroke(Stroke stroke) {
        return (ImagePaint) super.addStroke(stroke);
    }

    @Override
    public ImagePaint addStrokes(Collection<? extends Stroke> c) {
        return (ImagePaint) super.addStrokes(c);
    }

    @Override
    public ImagePaint removeStroke(Stroke stroke) {
        return (ImagePaint) super.removeStroke(stroke);
    }

    @Override
    public ImagePaint removeStrokes(Collection<? extends Stroke> c) {
        return (ImagePaint) super.removeStrokes(c);
    }

    @Override
    public ImagePaint clearStrokes() {
        return (ImagePaint) super.clearStrokes();
    }
}
