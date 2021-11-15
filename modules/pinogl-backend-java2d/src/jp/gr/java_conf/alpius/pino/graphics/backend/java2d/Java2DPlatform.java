package jp.gr.java_conf.alpius.pino.graphics.backend.java2d;

import jp.gr.java_conf.alpius.pino.graphics.*;
import jp.gr.java_conf.alpius.pino.graphics.backend.Platform;
import jp.gr.java_conf.alpius.pino.graphics.utils.Checks;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

public class Java2DPlatform extends Platform implements ResourceFactory {

    private static final Collection<? extends PixelFormat> AVAILABLE_FORMATS = Arrays.asList(StandardPixelFormat.values());

    public Java2DPlatform(Java2DDescriptor desc) {
        super(desc);
    }

    @Override
    public ResourceFactory getResourceFactory() {
        return this;
    }

    @Override
    public Collection<? extends PixelFormat> getAvailableFormats() {
        return AVAILABLE_FORMATS;
    }



    @Override
    public Java2DRTTexture createTexture(Image image) {
        int w = image.getWidth();
        int h = image.getHeight();
        var format = image.getPixelFormat();
        var alphaType = image.getAlphaType();
        var texture = new Java2DRTTexture(BufferedImage(w, h, format, alphaType), format, alphaType);
        var g = texture.createGraphics();
        g.drawImage(image, 0, 0);
        return texture;
    }

    private BufferedImage BufferedImage(int w, int h, PixelFormat format, AlphaType type) {
        Checks.require(w > 0, "w > 0");
        Checks.require(h > 0, "h > 0");
        Objects.requireNonNull(format, "format == null");
        if (StandardPixelFormat.RGB24.equals(format)) {
            int biType = BufferedImage.TYPE_INT_RGB;
            return new BufferedImage(w, h, biType);
        } else if (StandardPixelFormat.ARGB32.equals(format)) {
            int biType;
            if (type == AlphaType.OPAQUE) {
                biType = BufferedImage.TYPE_INT_RGB;
            } else if (type == AlphaType.UNPREMUL) {
                biType = BufferedImage.TYPE_INT_ARGB;
            } else {
                biType = BufferedImage.TYPE_INT_ARGB_PRE;
            }
            return new BufferedImage(w, h, biType);
        } else if (StandardPixelFormat.RGB565 == format) {
            return new BufferedImage(w, h, BufferedImage.TYPE_USHORT_565_RGB);
        } else if (StandardPixelFormat.RGB555 == format) {
            return new BufferedImage(w, h, BufferedImage.TYPE_USHORT_555_RGB);
        }

        throw new UnsupportedOperationException("Not supported yet[format=" + format + ", type=" + type + "]");
    }

    @Override
    public boolean isFormatSupported(PixelFormat format) {
        return format == StandardPixelFormat.RGB24 ||
                format == StandardPixelFormat.ARGB32 ||
                format == StandardPixelFormat.RGB565 ||
                format == StandardPixelFormat.RGB555;
    }

    @Override
    public boolean isCompatibleTexture(Texture texture) {
        return texture.getClass() == Java2DRTTexture.class;
    }
}
