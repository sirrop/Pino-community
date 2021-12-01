package jp.gr.java_conf.alpius.pino.graphics;

import jp.gr.java_conf.alpius.pino.graphics.color.ColorSpace;
import jp.gr.java_conf.alpius.pino.graphics.color.RGBColorSpace;
import jp.gr.java_conf.alpius.pino.graphics.utils.Checks;

import java.nio.Buffer;
import java.util.Objects;

public class Image {
    private final Buffer data;
    private final int w;
    private final int h;
    private final PixelFormat format;
    private final AlphaType type;
    private final PixelModel pixelModel;

    public Image(int w, int h, PixelFormat format, AlphaType type) {
        Checks.require(w > 0, "w <= 0");
        Checks.require(h > 0, "h <= 0");
        Objects.requireNonNull(format, "format == null");
        if (format.hasAlpha()) {
            Objects.requireNonNull(type, "type == null");
            this.type = type;
        } else {
            this.type = AlphaType.OPAQUE;
        }
        this.w = w;
        this.h = h;
        this.data = format.allocate(w, h);
        this.format = format;
        pixelModel = format.createPixelModel(w, h);
    }

    public ColorSpace getColorSpace() {
        return format.getColorSpace();
    }

    public Buffer getData() {
        return data;
    }

    public int getWidth() {
        return w;
    }

    public int getHeight() {
        return h;
    }

    public PixelFormat getPixelFormat() {
        return format;
    }

    public AlphaType getAlphaType() {
        return type;
    }

    public int getRGB(int x, int y) {
        float[] pixel = pixelModel.getPixel(x, y, (float[]) null, data);
        float[] colorValues = new float[getColorSpace().getNumComponents()];
        float opacity;
        if (format.hasAlpha()) {
            System.arraycopy(pixel, 1, colorValues, 0, colorValues.length);
            opacity = pixel[0];
        } else {
            colorValues = pixel;
            opacity = 1f;
        }
        if (type == AlphaType.PREMUL) {
            for (int i = 0; i < colorValues.length; ++i) {
                colorValues[i] /= opacity;
            }
        }
        float[] xyz = getColorSpace().toCIEXYZ(colorValues);
        float[] rgb = RGBColorSpace.CS_sRGB_D65.fromCIEXYZ(xyz);

        int a, r, g, b;
        a = Math.round(opacity * 0xff);
        r = Math.round(rgb[0] * 0xff);
        g = Math.round(rgb[1] * 0xff);
        b = Math.round(rgb[2] * 0xff);
        return a << 24 | r << 16 | g << 8 | b;
    }

    public void setRGB(int x, int y, int color) {
        int a = color >> 24 & 0xff;
        int r = color >> 16 & 0xff;
        int g = color >> 8 & 0xff;
        int b = color & 0xff;

        float[] rgb = {
                ((float) r) / 0xff, ((float) g) / 0xff, ((float) b) / 0xff
        };
        float opacity = ((float) a) / 0xff;

        float[] xyz = RGBColorSpace.CS_sRGB_D65.toCIEXYZ(rgb);
        float[] colorValues = getColorSpace().fromCIEXYZ(xyz);

        float[] pixel;

        if (type == AlphaType.PREMUL) {
            for (int i = 0; i < colorValues.length; ++i) {
                colorValues[i] *= opacity;
            }
        }
        if (format.hasAlpha()) {
            pixel = new float[colorValues.length + 1];
            System.arraycopy(colorValues, 0, pixel, 1, colorValues.length);
            pixel[0] = opacity;
        } else {
            pixel = colorValues;
        }

        pixelModel.setPixel(x, y, pixel, data);
    }

    public int[] getRGB(int x, int y, int w, int h, int[] pixels, int offset, int scanSize) {
        if (pixels == null) {
            pixels = new int[w * h];
        }

        var temp = x;

        final int maxY = y + h;
        final int maxX = x + w;

        for (; y < maxY; ++y, offset += scanSize) {
            for (; x < maxX; ++x) {
                pixels[offset + x] = getRGB(x, y);
            }
            x = temp;
        }

        return pixels;
    }

    public Color getColor(int x, int y) {
        float[] pixel = pixelModel.getPixel(x, y, (float[]) null, data);
        float[] colorComponents = new float[getColorSpace().getNumComponents()];
        float opacity;
        System.arraycopy(pixel, 1, colorComponents, 0, colorComponents.length);
        if (format.hasAlpha()) {
            opacity = pixel[0];
        } else {
            opacity = 1f;
        }
        if (type == AlphaType.PREMUL) {
            for (int i = 0; i < colorComponents.length; ++i) {
                colorComponents[i] /= opacity;
            }
        }
        return new Color(getColorSpace(), colorComponents, opacity);
    }

    public void setColor(int x, int y, Color color) {
        float[] pixel;
        if (format.hasAlpha()) {
            pixel = color.getComponents(getColorSpace());
            if (type == AlphaType.PREMUL) {
                for (int i = 1; i < pixel.length; ++i) {
                    pixel[i] *= color.getOpacity();
                }
            }
        } else {
            pixel = color.getColorComponents(getColorSpace());
        }
        pixelModel.setPixel(x, y, pixel, data);
    }

    public void setRGB(int x, int y, int w, int h, int[] pixels, int offset, int scanSize) {
        int temp = x;
        Objects.requireNonNull(pixels, "pixels == null");

        final int maxY = y + h;
        final int maxX = x + w;

        for (; y < maxY; ++y, offset += scanSize) {
            for (; x < maxX; ++x) {
                setRGB(x, y, pixels[offset + x]);
            }
            x = temp;
        }
    }
}
