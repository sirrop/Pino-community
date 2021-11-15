package jp.gr.java_conf.alpius.pino.graphics;

import jp.gr.java_conf.alpius.pino.graphics.color.ColorSpace;
import jp.gr.java_conf.alpius.pino.graphics.utils.Checks;

import java.nio.Buffer;
import java.util.Arrays;

/**
 * {@link PixelModel}のスケルトン実装を提供します。
 */
abstract class AbstractPixelModel implements PixelModel {
    private final int w;
    private final int h;
    private final ColorSpace cs;
    private final boolean hasAlpha;

    protected AbstractPixelModel(int w, int h, ColorSpace cs, boolean hasAlpha) {
        this.cs = cs;
        this.hasAlpha = hasAlpha;
        Checks.require(w > 0, "w <= 0");
        Checks.require(h > 0, "h <= 0");
        this.w = w;
        this.h = h;
    }

    @Override
    public int getWidth() {
        return w;
    }

    @Override
    public int getHeight() {
        return h;
    }

    protected ColorSpace getColorSpace() {
        return cs;
    }

    protected boolean hasAlpha() {
        return hasAlpha;
    }

    @Override
    public void setPixels(int x, int y, int w, int h, double[] pixels, Buffer data) {
        int numComponents = getColorSpace().getNumComponents();
        if (hasAlpha()) {
            numComponents += 1;
        }

        double[] pixel = Arrays.copyOf(pixels, numComponents);

        final int maxY = y + h;
        final int maxX = x + w;

        for (int offset = numComponents; y < maxY; ++y) {
            for (; x < maxX; ++x, offset += numComponents) {
                setPixel(x, y, pixel, data);
                System.arraycopy(pixels, offset, pixel, 0, numComponents);
            }
        }
    }

    @Override
    public void setPixels(int x, int y, int w, int h, float[] pixels, Buffer data) {
        int numComponents = getColorSpace().getNumComponents();
        if (hasAlpha()) {
            numComponents += 1;
        }

        float[] pixel = Arrays.copyOf(pixels, numComponents);

        final int maxY = y + h;
        final int maxX = x + w;

        for (int offset = numComponents; y < maxY; ++y) {
            for (; x <  maxX; ++x, offset += numComponents) {
                setPixel(x, y, pixel, data);
                System.arraycopy(pixels, offset, pixel, 0, numComponents);
            }
        }
    }

    @Override
    public void setPixels(int x, int y, int w, int h, int[] pixels, Buffer data) {
        int numComponents = getColorSpace().getNumComponents();
        if (hasAlpha()) {
            numComponents += 1;
        }

        int[] pixel = Arrays.copyOf(pixels, numComponents);

        final int maxY = y + h;
        final int maxX = x + w;

        for (int offset = numComponents; y < maxY; ++y) {
            for (; x < maxX; ++x, offset += numComponents) {
                setPixel(x, y, pixel, data);
                System.arraycopy(pixels, offset, pixel, 0, numComponents);
            }
        }
    }

    @Override
    public double[] getPixels(int x, int y, int w, int h, double[] pixels, Buffer data) {
        int numComponents = getColorSpace().getNumComponents();
        if (hasAlpha()) {
            numComponents += 1;
        }

        if (pixels == null) {
            int nPixels = w * h;
            pixels = new double[nPixels * numComponents];
        }

        final int maxY = y + h;
        final int maxX = x + w;

        double[] pixel = null;
        for (int offset = 0; y <  maxY; ++y) {
            for (; x < maxX; ++x, offset += numComponents) {
                pixel = getPixel(x, y, pixel, data);
                System.arraycopy(pixel, 0, pixels, offset, pixel.length);
            }
        }

        return pixels;
    }

    @Override
    public float[] getPixels(int x, int y, int w, int h, float[] pixels, Buffer data) {
        int numComponents = getColorSpace().getNumComponents();
        if (hasAlpha()) {
            numComponents += 1;
        }

        if (pixels == null) {
            int nPixels = w * h;
            pixels = new float[nPixels * numComponents];
        }

        final int maxY = y + h;
        final int maxX = x + w;

        float[] pixel = null;
        for (int offset = 0; y < maxY; ++y) {
            for (; x < maxX; ++x, offset += numComponents) {
                pixel = getPixel(x, y, pixel, data);
                System.arraycopy(pixel, 0, pixels, offset, pixel.length);
            }
        }

        return pixels;
    }

    @Override
    public int[] getPixels(int x, int y, int w, int h, int[] pixels, Buffer data) {
        int numComponents = getColorSpace().getNumComponents();
        if (hasAlpha()) {
            numComponents += 1;
        }

        if (pixels == null) {
            int nPixels = w * h;
            pixels = new int[nPixels * numComponents];
        }

        final int maxY = y + h;
        final int maxX = x + w;

        int[] pixel = null;
        for (int offset = 0; y < maxY; ++y) {
            for (; x < maxX; ++x, offset += numComponents) {
                pixel = getPixel(x, y, pixel, data);
                System.arraycopy(pixel, 0, pixels, offset, pixel.length);
            }
        }

        return pixels;
    }
}
