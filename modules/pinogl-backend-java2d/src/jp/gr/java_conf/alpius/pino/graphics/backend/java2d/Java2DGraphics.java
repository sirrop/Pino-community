package jp.gr.java_conf.alpius.pino.graphics.backend.java2d;

import jp.gr.java_conf.alpius.pino.graphics.Graphics;
import jp.gr.java_conf.alpius.pino.graphics.angle.Angle;
import jp.gr.java_conf.alpius.pino.graphics.angle.StandardAngleUnit;
import jp.gr.java_conf.alpius.pino.graphics.geom.ArcType;
import jp.gr.java_conf.alpius.pino.graphics.geom.Shape;
import jp.gr.java_conf.alpius.pino.graphics.paint.PaintContext;
import jp.gr.java_conf.alpius.pino.graphics.paint.stroke.Stroke;
import jp.gr.java_conf.alpius.pino.graphics.transform.Transform;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;

public class Java2DGraphics implements Graphics {
    private final Java2DRTTexture target;
    private final Graphics2D g;

    public Java2DGraphics(Java2DRTTexture target) {
        this.target = target;
        g = target.getBufferedImage().createGraphics();
    }

    public Graphics drawImage(BufferedImage image, int x, int y) {
        g.drawImage(image, x, y, null);
        return this;
    }

    @Override
    public void dispose() {
        g.dispose();
    }

    @Override
    public Graphics translate(double tx, double ty) {
        g.translate(tx, ty);
        return this;
    }

    @Override
    public Graphics rotate(double rad) {
        g.rotate(StandardAngleUnit.DEG.fromRadian(rad));
        return this;
    }

    @Override
    public Graphics rotate(double rad, double x, double y) {
        g.rotate(StandardAngleUnit.DEG.fromRadian(rad), x, y);
        return this;
    }

    @Override
    public Graphics scale(double sx, double sy) {
        g.scale(sx, sy);
        return this;
    }

    @Override
    public Graphics shear(double sx, double sy) {
        g.shear(sx, sy);
        return this;
    }

    @Override
    public Graphics transform(Transform transform) {
        g.transform(Java2DUtils.toJava2D(transform));
        return this;
    }

    @Override
    public Graphics setTransform(Transform transform) {
        g.setTransform(Java2DUtils.toJava2D(transform));
        return this;
    }

    @Override
    public Transform getTransform() {
        return Java2DUtils.fromJava2D(g.getTransform());
    }

    @Override
    public Graphics draw(Shape shape, PaintContext paint) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public Graphics drawPoint(int x, int y, PaintContext paint) {
        return drawLine(x, y, x, y, paint);
    }

    @Override
    public Graphics drawLine(int x0, int y0, int x1, int y1, PaintContext paint) {
        var itr = strokeInitializeIterator(paint);
        while (itr.hasNext()) {
            itr.next().run();
            g.drawLine(x0, y0, x1, y1);
        }
        return this;
    }

    @Override
    public Graphics drawRect(int x, int y, int w, int h, PaintContext paint) {
        if (paint.getPaint() != null) {
            setFill(paint);
            g.fillRect(x, y, w, h);
        }
        if (!paint.getStrokes().isEmpty()) {
            var itr = strokeInitializeIterator(paint);
            while (itr.hasNext()) {
                itr.next().run();
                g.drawRect(x, y, w, h);
            }
        }
        return this;
    }

    private void setFill(PaintContext ctx) {
        var p = ctx.getPaint();
        g.setPaint(Java2DUtils.toJava2D(p));
        g.setComposite(Java2DUtils.toJava2D(p.getComposite(), p.getOpacity()));
        if (p.isAntialias()) {
            g.addRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
        } else {
            g.addRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF));
        }
    }

    private Iterator<Runnable> strokeInitializeIterator(PaintContext paint) {
        var list = new ArrayList<Runnable>();
        for (var stroke: paint.getStrokes()) {
            list.add(() -> setStroke(stroke));
        }
        return list.iterator();
    }

    private void setStroke(Stroke stroke) {
        var p = Java2DUtils.toJava2D(stroke.getPaint());
        var s = Java2DUtils.getBasicStroke(stroke);
        g.setPaint(p);
        g.setStroke(s);
        g.setComposite(Java2DUtils.toJava2D(stroke.getPaint().getComposite(), stroke.getPaint().getOpacity()));
        if (stroke.getPaint().isAntialias()) {
            g.addRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
        } else {
            g.addRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF));
        }
    }

    @Override
    public Graphics drawOval(int x, int y, int w, int h, PaintContext paint) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public Graphics drawArc(int x, int y, int w, int h, double startRad, double arcExtent, ArcType type, PaintContext paint) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public Graphics drawArc(int x, int y, int w, int h, Angle start, Angle extent, ArcType type, PaintContext paint) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public Graphics drawRoundRect(int x, int y, int w, int h, int arcWidth, int arcHeight, PaintContext paint) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
