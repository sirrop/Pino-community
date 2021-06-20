package jp.gr.java_conf.alpius.pino.ui.canvas;

import javafx.scene.canvas.Canvas;

import java.awt.image.BufferedImage;

public interface CanvasBackground {
    static FxCanvasBackground bind(Canvas canvas) {
        return new FxCanvasBackground(canvas, null);
    }

    void setPattern(BufferedImage pattern);

    BufferedImage getPattern();
}
