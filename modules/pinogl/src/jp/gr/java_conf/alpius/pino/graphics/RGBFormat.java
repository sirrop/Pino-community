package jp.gr.java_conf.alpius.pino.graphics;

import jp.gr.java_conf.alpius.pino.graphics.color.ColorSpace;

abstract class RGBFormat implements PixelFormat {
    private final ColorSpace anyRgbColorSpace;
    private final boolean hasAlpha;

    protected RGBFormat(ColorSpace anyRgbColorSpace, boolean hasAlpha) {
        assert anyRgbColorSpace.getType() == ColorSpace.TYPE_RGB : "anyRgbColorSpace is not RGB family";
        this.anyRgbColorSpace = anyRgbColorSpace;
        this.hasAlpha = hasAlpha;
    }

    @Override
    public ColorSpace getColorSpace() {
        return anyRgbColorSpace;
    }

    @Override
    public boolean hasAlpha() {
        return hasAlpha;
    }
}
