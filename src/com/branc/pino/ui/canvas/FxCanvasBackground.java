package com.branc.pino.ui.canvas;

import com.branc.pino.graphics.PinoGraphics;
import javafx.beans.binding.DoubleBinding;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.awt.*;
import java.awt.image.BufferedImage;

class FxCanvasBackground extends Rectangle implements CanvasBackground {
    private static final BufferedImage DEFAULT_PATTERN;

    static {
        var res = new BufferedImage(40, 40, BufferedImage.TYPE_BYTE_GRAY);
        var g = PinoGraphics.create(res);
        var fill = g.fill();
        fill.setPaint(Color.WHITE);
        fill.drawRect(20, 20, 0, 0)
                .drawRect(20, 20, 20, 20)
                .setPaint(Color.LIGHT_GRAY);
        fill.drawRect(20, 20, 0, 20)
                .drawRect(20, 20, 20, 0);
        g.dispose();
        DEFAULT_PATTERN = res;
    }

    private BufferedImage pattern;

    public FxCanvasBackground(Canvas target, BufferedImage pattern) {
        if (pattern == null) {
            this.pattern = DEFAULT_PATTERN;
        } else {
            this.pattern = pattern;
        }
        widthProperty().bind(target.widthProperty());
        heightProperty().bind(target.heightProperty());
        rotateProperty().bind(target.rotateProperty());
        translateXProperty().bind(target.translateXProperty());
        translateYProperty().bind(target.translateYProperty());
        scaleXProperty().bind(target.scaleXProperty());
        scaleYProperty().bind(target.scaleYProperty());
        updateBackground();
    }

    private void updateBackground() {
        var img = SwingFXUtils.toFXImage(pattern, null);
        var fill = new ImagePattern(img, 0, 0, img.getWidth(), img.getHeight(), false);
        setFill(fill);
    }

    @Override
    public void setPattern(BufferedImage pattern) {
        this.pattern = pattern;
        updateBackground();
    }

    @Override
    public BufferedImage getPattern() {
        return pattern;
    }
}
