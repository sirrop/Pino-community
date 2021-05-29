package com.branc.pino.paint.layer.internal;

import com.branc.pino.graphics.PinoGraphics;
import com.branc.pino.paint.layer.Drawable;
import com.branc.pino.paint.layer.LayerObject;
import com.branc.pino.project.ProjectManager;

import java.awt.image.BufferedImage;
import java.io.*;

public class FullColorBitmapLayer extends LayerObject implements Drawable {
    private static final long serialVersionUID = 1L;

    private transient BufferedImage lastReturned;
    private transient BufferedImage img;

    public FullColorBitmapLayer() {
        var project = ProjectManager.getInstance().getProject();
        img = new BufferedImage((int) project.getWidth(), (int) project.getHeight(), BufferedImage.TYPE_INT_ARGB);
        init();
    }


    private void init()  {
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

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeInt(img.getWidth());
        oos.writeInt(img.getHeight());
        int[] data = img.getRGB(0, 0, img.getWidth(), img.getHeight(), null, 0, img.getWidth());
        for (int color: data) {
            oos.writeInt(color);
        }
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        int width = ois.readInt();
        int height = ois.readInt();
        if (width <= 0) throw new InvalidObjectException("width is negative");
        if (height <= 0) throw new InvalidObjectException("height is negative");
        int[] data = new int[width * height];
        for (int i = 0, size = width * height; i < size; i++) {
            data[i] = ois.readInt();
        }
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        img.setRGB(0, 0, width, height, data, 0, width);
    }
}
