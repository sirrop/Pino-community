package jp.gr.java_conf.alpius.pino.graphics;

import jp.gr.java_conf.alpius.pino.graphics.color.ColorSpace;

import java.nio.Buffer;

public interface PixelFormat {
    Buffer allocate(int w, int h);
    ColorSpace getColorSpace();
    PixelModel createPixelModel(int w, int h);
    boolean hasAlpha();
}
