package jp.gr.java_conf.alpius.pino.graphics.backend.java2d;

import jp.gr.java_conf.alpius.pino.graphics.AlphaType;
import jp.gr.java_conf.alpius.pino.graphics.PixelFormat;
import jp.gr.java_conf.alpius.pino.graphics.RTTexture;

import java.awt.image.BufferedImage;

public class Java2DRTTexture implements RTTexture {
    private final BufferedImage image;
    private final PixelFormat format;
    private final AlphaType type;

    Java2DRTTexture(BufferedImage image, PixelFormat format, AlphaType type) {
        this.image = image;
        this.format = format;
        this.type = type;
    }

    @Override
    public Java2DGraphics createGraphics() {
        return new Java2DGraphics(this);
    }

    @Override
    public int getWidth() {
        return image.getWidth();
    }

    @Override
    public int getHeight() {
        return image.getHeight();
    }

    @Override
    public AlphaType getAlphaType() {
        return type;
    }

    public BufferedImage getBufferedImage() {
        return image;
    }
}
