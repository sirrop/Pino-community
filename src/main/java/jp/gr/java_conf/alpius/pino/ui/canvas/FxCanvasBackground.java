package jp.gr.java_conf.alpius.pino.ui.canvas;

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
        var g = res.createGraphics();
        g.setPaint(Color.WHITE);
        g.fillRect(0, 0, 20, 20);
        g.fillRect(20, 20, 20, 20);
        g.setPaint(Color.LIGHT_GRAY);
        g.fillRect(0, 20, 20, 20);
        g.fillRect(20, 0, 20, 20);
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
