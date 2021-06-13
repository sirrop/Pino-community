package com.branc.pino.layer;

import com.branc.pino.project.Project;
import com.branc.pino.project.ProjectManager;
import pino.layer.DrawableHelper;
import pino.layer.DrawablePeer;
import pino.layer.LayerPeer;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class DrawableLayer extends LayerObject {
    static {
        DrawableHelper.setDrawableAccessor(new DrawableHelper.DrawableAccessor() {
            @Override
            public LayerPeer doCreatePeer(LayerObject object) {
                return ((DrawableLayer) object).doCreatePeer();
            }
        });
    }

    {
        DrawableHelper.initHelper(this);
    }

    public DrawableLayer() {
        Project p = ProjectManager.getInstance().getProject();
        int w = (int) Math.floor(p.getWidth());
        int h = (int) Math.floor(p.getHeight());
        image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        setName("レイヤー");
    }

    private final BufferedImage image;

    private LayerPeer doCreatePeer() {
        return new DrawablePeer(this, image);
    }

    public Graphics2D createGraphics() {
        Graphics2D g = image.createGraphics();
        AffineTransform tx = new AffineTransform();
        tx.translate(getX(), getY());
        tx.rotate(getRotateZ());
        tx.scale(getScaleX(), getScaleY());
        g.setTransform(tx);
        return g;
    }
}
