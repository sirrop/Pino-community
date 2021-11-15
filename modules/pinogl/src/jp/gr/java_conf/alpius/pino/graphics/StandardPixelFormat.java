package jp.gr.java_conf.alpius.pino.graphics;

import jp.gr.java_conf.alpius.pino.graphics.color.ColorSpace;
import jp.gr.java_conf.alpius.pino.graphics.color.RGBColorSpace;

import java.nio.Buffer;

public enum StandardPixelFormat implements PixelFormat {
    RGB24(new IntPackedRGBFormat(RGBColorSpace.CS_sRGB_D65, false)),
    ARGB32(new IntPackedRGBFormat(RGBColorSpace.CS_sRGB_D65, true)),
    RGB48(new LongPackedRGBFormat(RGBColorSpace.CS_sRGB_D65, false)),
    ARGB64(new LongPackedRGBFormat(RGBColorSpace.CS_sRGB_D65, true)),

    RGB565(new RGB565Format(RGBColorSpace.CS_sRGB_D65)),
    RGB555(new RGB555Format(RGBColorSpace.CS_sRGB_D65));

    private final PixelFormat implement;

    StandardPixelFormat(PixelFormat implement) {
        this.implement = implement;
    }

    @Override
    public Buffer allocate(int w, int h) {
        return implement.allocate(w, h);
    }

    @Override
    public ColorSpace getColorSpace() {
        return implement.getColorSpace();
    }

    @Override
    public PixelModel createPixelModel(int w, int h) {
        return implement.createPixelModel(w, h);
    }

    @Override
    public boolean hasAlpha() {
        return implement.hasAlpha();
    }
}
