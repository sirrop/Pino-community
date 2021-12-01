package jp.gr.java_conf.alpius.pino.graphics;

import jp.gr.java_conf.alpius.pino.graphics.color.ColorSpace;

import java.nio.Buffer;
import java.nio.ByteBuffer;

final class IntPackedRGBPixelModel extends AbstractPixelModel {
    public IntPackedRGBPixelModel(int w, int h, ColorSpace cs, boolean hasAlpha) {
        super(w, h, cs, hasAlpha);
    }

    @Override
    public double[] getPixel(int x, int y, double[] pixel, Buffer data) {
        if (pixel == null) {
            int numComponents = getColorSpace().getNumComponents();
            if (hasAlpha()) {
                pixel = new double[numComponents + 1];
            } else {
                pixel = new double[numComponents];
            }
        }
        if (data instanceof ByteBuffer buffer) {
            int p = buffer.getInt(indexOf(x, y));
            int r, g, b;
            r = p >> 16 & 0xff;
            g = p >> 8 & 0xff;
            b = p & 0xff;
            if (hasAlpha()) {
                int a = p >> 24 & 0xff;
                pixel[0] = ((double) a) / 0xff;
                pixel[1] = ((double) r) / 0xff;
                pixel[2] = ((double) g) / 0xff;
                pixel[3] = ((double) b) / 0xff;
            } else {
                pixel[0] = ((double) r) / 0xff;
                pixel[1] = ((double) g) / 0xff;
                pixel[2] = ((double) b) / 0xff;
            }
            return pixel;
        }
        throw new IllegalArgumentException("data is not compatible");
    }

    @Override
    public float[] getPixel(int x, int y, float[] pixel, Buffer data) {
        if (pixel == null) {
            int numComponents = getColorSpace().getNumComponents();
            if (hasAlpha()) {
                pixel = new float[numComponents + 1];
            } else {
                pixel = new float[numComponents];
            }
        }
        if (data instanceof ByteBuffer buffer) {
            int p = buffer.getInt(indexOf(x, y));
            int r, g, b;
            r = p >> 16 & 0xff;
            g = p >> 8 & 0xff;
            b = p & 0xff;
            if (hasAlpha()) {
                int a = p >> 24 & 0xff;
                pixel[0] = ((float) a) / 0xff;
                pixel[1] = ((float) r) / 0xff;
                pixel[2] = ((float) g) / 0xff;
                pixel[3] = ((float) b) / 0xff;
            } else {
                pixel[0] = ((float) r) / 0xff;
                pixel[1] = ((float) g) / 0xff;
                pixel[2] = ((float) b) / 0xff;
            }
            return pixel;
        }
        throw new IllegalArgumentException("data is not compatible");
    }

    @Override
    public int[] getPixel(int x, int y, int[] pixel, Buffer data) {
        if (pixel == null) {
            int numComponents = getColorSpace().getNumComponents();
            if (hasAlpha()) {
                pixel = new int[numComponents + 1];
            } else {
                pixel = new int[numComponents];
            }
        }
        if (data instanceof ByteBuffer buffer) {
            int p = buffer.getInt(indexOf(x, y));
            int r, g, b;
            r = p >> 16 & 0xff;
            g = p >> 8 & 0xff;
            b = p & 0xff;
            if (hasAlpha()) {
                int a = p >> 24 & 0xff;
                pixel[0] = a;
                pixel[1] = r;
                pixel[2] = g;
                pixel[3] = b;
            } else {
                pixel[0] = r;
                pixel[1] = g;
                pixel[2] = b;
            }
            return pixel;
        }
        throw new IllegalArgumentException("data is not compatible");
    }

    @Override
    public void setPixel(int x, int y, double[] pixel, Buffer data) {
        if (data instanceof ByteBuffer buffer) {
            int color;
            int a, r, g, b;
            if (hasAlpha()) {
                a = Math.toIntExact(Math.round(pixel[0] * 0xff));
                r = Math.toIntExact(Math.round(pixel[1] * 0xff));
                g = Math.toIntExact(Math.round(pixel[2] * 0xff));
                b = Math.toIntExact(Math.round(pixel[3] * 0xff));
            } else {
                a = 0xff;
                r = Math.toIntExact(Math.round(pixel[0] * 0xff));
                g = Math.toIntExact(Math.round(pixel[1] * 0xff));
                b = Math.toIntExact(Math.round(pixel[2] * 0xff));
            }
            color = a << 24 | r << 16 | g << 8 | b;
            buffer.putInt(indexOf(x, y), color);
        }
    }

    @Override
    public void setPixel(int x, int y, float[] pixel, Buffer data) {
        if (data instanceof ByteBuffer buffer) {
            int color;
            int a, r, g, b;
            if (hasAlpha()) {
                a = Math.round(pixel[0] * 0xff);
                r = Math.round(pixel[1] * 0xff);
                g = Math.round(pixel[2] * 0xff);
                b = Math.round(pixel[3] * 0xff);
            } else {
                a = 0xff;
                r = Math.round(pixel[0] * 0xff);
                g = Math.round(pixel[1] * 0xff);
                b = Math.round(pixel[2] * 0xff);
            }
            color = a << 24 | r << 16 | g << 8 | b;
            buffer.putInt(indexOf(x, y), color);
        }
    }

    @Override
    public void setPixel(int x, int y, int[] pixel, Buffer data) {
        if (data instanceof ByteBuffer buffer) {
            int color;
            int a, r, g, b;
            if (hasAlpha()) {
                a = pixel[0] & 0xff;
                r = pixel[1] & 0xff;
                g = pixel[2] & 0xff;
                b = pixel[3] & 0xff;
            } else {
                a = 0xff;
                r = pixel[0] & 0xff;
                g = pixel[1] & 0xff;
                b = pixel[2] & 0xff;
            }
            color = a << 24 | r << 16 | g << 8 | b;
            buffer.putInt(indexOf(x, y), color);
        }
    }

    private int indexOf(int x, int y) {
        return (y * getWidth() + x) * 4;
    }
}
