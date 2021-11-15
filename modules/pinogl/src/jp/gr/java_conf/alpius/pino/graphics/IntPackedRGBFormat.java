package jp.gr.java_conf.alpius.pino.graphics;

import jp.gr.java_conf.alpius.pino.graphics.color.ColorSpace;

import java.nio.Buffer;
import java.nio.ByteBuffer;

final class IntPackedRGBFormat extends RGBFormat {
    public IntPackedRGBFormat(ColorSpace anyRgbColorSpace, boolean hasAlpha) {
        super(anyRgbColorSpace, hasAlpha);
    }

    @Override
    public Buffer allocate(int w, int h) {
        return ByteBuffer.allocate(w * h * 4);
    }

    @Override
    public PixelModel createPixelModel(int w, int h) {
        return new IntPackedRGBPixelModel(w, h, getColorSpace(), hasAlpha());
    }
}
