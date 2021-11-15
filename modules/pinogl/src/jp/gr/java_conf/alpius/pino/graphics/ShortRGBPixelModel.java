package jp.gr.java_conf.alpius.pino.graphics;

import jp.gr.java_conf.alpius.pino.graphics.color.ColorSpace;

import java.nio.Buffer;
import java.nio.ByteBuffer;

final class ShortRGBPixelModel extends AbstractPixelModel {
    private final int[] shifts;
    private final int[] masks;

    public ShortRGBPixelModel(int w, int h, ColorSpace cs, boolean hasAlpha, int[] shifts, int[] masks) {
        super(w, h, cs, hasAlpha);
        this.shifts = shifts;
        this.masks = masks;
    }

    @Override
    public double[] getPixel(int x, int y, double[] pixel, Buffer data) {
        if (pixel == null) {
            int numComponents = getColorSpace().getNumComponents();
            pixel = new double[numComponents];
        }
        if (data instanceof ByteBuffer buffer) {
            short p = buffer.getShort(indexOf(x, y));
            int r, g, b;
            r = p >> shifts[0] & masks[0];
            g = p >> shifts[1] & masks[1];
            b = p & masks[2];

            pixel[0] = ((double) r) / masks[0];
            pixel[1] = ((double) g) / masks[1];
            pixel[2] = ((double) b) / masks[2];

            return pixel;
        }
        throw new IllegalArgumentException("data is not compatible");
    }

    @Override
    public float[] getPixel(int x, int y, float[] pixel, Buffer data) {
        if (pixel == null) {
            int numComponents = getColorSpace().getNumComponents();
            pixel = new float[numComponents];
        }
        if (data instanceof ByteBuffer buffer) {
            short p = buffer.getShort(indexOf(x, y));
            int r, g, b;
            r = p >> shifts[0] & masks[0];
            g = p >> shifts[1] & masks[1];
            b = p & masks[2];

            pixel[0] = ((float) r) / masks[0];
            pixel[1] = ((float) g) / masks[1];
            pixel[2] = ((float) b) / masks[2];

            return pixel;
        }
        throw new IllegalArgumentException("data is not compatible");
    }

    @Override
    public int[] getPixel(int x, int y, int[] pixel, Buffer data) {
        if (pixel == null) {
            int numComponents = getColorSpace().getNumComponents();
            pixel = new int[numComponents];
        }
        if (data instanceof ByteBuffer buffer) {
            short p = buffer.getShort(indexOf(x, y));

            pixel[0] = p >> shifts[0] & masks[0];
            pixel[1] = p >> shifts[1] & masks[1];
            pixel[2] = p & masks[2];

            return pixel;
        }
        throw new IllegalArgumentException("data is not compatible");
    }


    @Override
    public void setPixel(int x, int y, double[] pixel, Buffer data) {
        if (data instanceof ByteBuffer buffer) {
            short color;
            int r, g, b;

            r = Math.toIntExact(Math.round(pixel[0] * masks[0]));
            g = Math.toIntExact(Math.round(pixel[1] * masks[1]));
            b = Math.toIntExact(Math.round(pixel[2] * masks[2]));

            color = (short) (r << shifts[0] | g << shifts[1] | b);
            buffer.putShort(indexOf(x, y), color);
        }
    }

    @Override
    public void setPixel(int x, int y, float[] pixel, Buffer data) {
        if (data instanceof ByteBuffer buffer) {
            short color;
            int r, g, b;

            r = Math.round(pixel[0] * masks[0]);
            g = Math.round(pixel[1] * masks[1]);
            b = Math.round(pixel[2] * masks[2]);

            color = (short) (r << shifts[0] | g << shifts[1] | b);
            buffer.putShort(indexOf(x, y), color);
        }
    }

    @Override
    public void setPixel(int x, int y, int[] pixel, Buffer data) {
        if (data instanceof ByteBuffer buffer) {
            short color;
            int r, g, b;

            r = pixel[0] & masks[0];
            g = pixel[1] & masks[1];
            b = pixel[2] & masks[2];

            color = (short) (r << shifts[0] | g << shifts[1] | b);
            buffer.putShort(indexOf(x, y), color);
        }
    }

    private int indexOf(int x, int y) {
        return (y * getWidth() + x) * 2;
    }
}
