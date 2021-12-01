package jp.gr.java_conf.alpius.pino.graphics;

import jp.gr.java_conf.alpius.pino.graphics.color.ColorSpace;

import java.nio.Buffer;
import java.nio.ByteBuffer;

final class RGB565Format extends RGBFormat {
    private static final int[] SHIFTS = {
            11, 6, 0
    };

    private static final int[] MASKS = {
            0b11111, 0b111111, 0b11111
    };

    public RGB565Format(ColorSpace anyRgbColorSpace) {
        super(anyRgbColorSpace, false);
    }

    @Override
    public Buffer allocate(int w, int h) {
        return ByteBuffer.allocate(w * h * 2);
    }

    @Override
    public PixelModel createPixelModel(int w, int h) {
        return new ShortRGBPixelModel(w, h, getColorSpace(), hasAlpha(), SHIFTS, MASKS);
    }
}
