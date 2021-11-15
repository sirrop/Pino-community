package jp.gr.java_conf.alpius.pino.graphics;

import jp.gr.java_conf.alpius.pino.graphics.color.ColorSpace;

import java.nio.Buffer;
import java.nio.ByteBuffer;

final class LongPackedRGBPixelModel extends AbstractPixelModel {
    LongPackedRGBPixelModel(int w, int h, ColorSpace cs, boolean hasAlpha) {
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
            long p = buffer.getLong(indexOf(x, y));
            long r, g, b;
            r = p >> 32 & 0xffff;
            g = p >> 16 & 0xffff;
            b = p & 0xffff;
            if (hasAlpha()) {
                long a = p >> 48 & 0xffff;
                pixel[0] = ((double) a) / 0xffff;
                pixel[1] = ((double) r) / 0xffff;
                pixel[2] = ((double) g) / 0xffff;
                pixel[3] = ((double) b) / 0xffff;
            } else {
                pixel[0] = ((double) r) / 0xffff;
                pixel[1] = ((double) g) / 0xffff;
                pixel[2] = ((double) b) / 0xffff;
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
            long p = buffer.getLong(indexOf(x, y));
            long r, g, b;
            r = p >> 32 & 0xffff;
            g = p >> 16 & 0xffff;
            b = p & 0xffff;
            if (hasAlpha()) {
                long a = p >> 48 & 0xffff;
                pixel[0] = ((float) a) / 0xffff;
                pixel[1] = ((float) r) / 0xffff;
                pixel[2] = ((float) g) / 0xffff;
                pixel[3] = ((float) b) / 0xffff;
            } else {
                pixel[0] = ((float) r) / 0xffff;
                pixel[1] = ((float) g) / 0xffff;
                pixel[2] = ((float) b) / 0xffff;
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
            long p = buffer.getInt(indexOf(x, y));
            long r, g, b;
            r = p >> 32 & 0xffff;
            g = p >> 16 & 0xffff;
            b = p & 0xffff;
            if (hasAlpha()) {
                long a = p >> 48 & 0xffff;
                pixel[0] = Math.toIntExact(a);
                pixel[1] = Math.toIntExact(r);
                pixel[2] = Math.toIntExact(g);
                pixel[3] = Math.toIntExact(b);
            } else {
                pixel[0] = Math.toIntExact(r);
                pixel[1] = Math.toIntExact(g);
                pixel[2] = Math.toIntExact(b);
            }
            return pixel;
        }
        throw new IllegalArgumentException("data is not compatible");
    }


    @Override
    public void setPixel(int x, int y, double[] pixel, Buffer data) {
        if (data instanceof ByteBuffer buffer) {
            long color;
            long a, r, g, b;
            if (hasAlpha()) {
                a = Math.round(pixel[0] * 0xffff);
                r = Math.round(pixel[1] * 0xffff);
                g = Math.round(pixel[2] * 0xffff);
                b = Math.round(pixel[3] * 0xffff);
            } else {
                a = 0xffff;
                r = Math.round(pixel[0] * 0xffff);
                g = Math.round(pixel[1] * 0xffff);
                b = Math.round(pixel[2] * 0xffff);
            }
            color = a << 48 | r << 32 | g << 16 | b;
            buffer.putLong(indexOf(x, y), color);
        }
    }

    @Override
    public void setPixel(int x, int y, float[] pixel, Buffer data) {
        if (data instanceof ByteBuffer buffer) {
            long color;
            long a, r, g, b;
            if (hasAlpha()) {
                a = Math.round(pixel[0] * 0xffff);
                r = Math.round(pixel[1] * 0xffff);
                g = Math.round(pixel[2] * 0xffff);
                b = Math.round(pixel[3] * 0xffff);
            } else {
                a = 0xffff;
                r = Math.round(pixel[0] * 0xffff);
                g = Math.round(pixel[1] * 0xffff);
                b = Math.round(pixel[2] * 0xffff);
            }
            color = a << 48 | r << 32 | g << 16 | b;
            buffer.putLong(indexOf(x, y), color);
        }
    }

    @Override
    public void setPixel(int x, int y, int[] pixel, Buffer data) {
        if (data instanceof ByteBuffer buffer) {
            long color;
            long a, r, g, b;
            if (hasAlpha()) {
                a = pixel[0] & 0xffff;
                r = pixel[1] & 0xffff;
                g = pixel[2] & 0xffff;
                b = pixel[3] & 0xffff;
            } else {
                a = 0xffff;
                r = pixel[0] & 0xffff;
                g = pixel[1] & 0xffff;
                b = pixel[2] & 0xffff;
            }
            color = a << 48 | r << 32 | g << 16 | b;
            buffer.putLong(indexOf(x, y), color);
        }
    }

    private int indexOf(int x, int y) {
        return (y * getWidth() + x) * 8;
    }
}
