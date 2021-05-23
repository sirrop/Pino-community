package com.branc.pino.paint.layer.internal;

import com.branc.pino.graphics.PinoGraphics;
import com.branc.pino.paint.layer.Drawable;
import com.branc.pino.paint.layer.LayerObject;
import com.branc.pino.project.ProjectManager;

import java.awt.image.BufferedImage;

public class FullColorBitmapLayer extends LayerObject implements Drawable {
    private BufferedImage lastReturned;
    private final BufferedImage img;

    public FullColorBitmapLayer() {
        var project = ProjectManager.getInstance().getProject();
        img = new BufferedImage((int) project.getWidth(), (int) project.getHeight(), BufferedImage.TYPE_INT_ARGB);
        setName("フルカラービットマップレイヤー");
    }

    @Override
    public PinoGraphics createGraphics() {
        return PinoGraphics.create(img);
    }

    @Override
    public BufferedImage toImage() {
        if (lastReturned == null || !img.getRaster().getDataBuffer().equals(lastReturned.getRaster().getDataBuffer())) {
            var res = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
            var g = res.createGraphics();
            g.drawImage(img, 0, 0, null);
            g.dispose();

            lastReturned = res;
            return res;
        } else {
            return lastReturned;
        }
    }
}
