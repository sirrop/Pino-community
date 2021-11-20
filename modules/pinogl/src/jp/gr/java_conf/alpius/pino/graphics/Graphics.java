package jp.gr.java_conf.alpius.pino.graphics;

import jp.gr.java_conf.alpius.pino.graphics.angle.Angle;
import jp.gr.java_conf.alpius.pino.graphics.angle.StandardAngleUnit;
import jp.gr.java_conf.alpius.pino.graphics.geom.ArcType;
import jp.gr.java_conf.alpius.pino.graphics.paint.PaintContext;
import jp.gr.java_conf.alpius.pino.graphics.transform.Transform;

public interface Graphics extends GraphicsResource {
    Graphics translate(double tx, double ty);
    default Graphics rotate(Angle angle) {
        return rotate(angle.getAngle(StandardAngleUnit.RAD));
    }
    Graphics rotate(double rad);
    default Graphics rotate(Angle angle, double x, double y) {
        return rotate(angle.getAngle(StandardAngleUnit.RAD), x, y);
    }
    Graphics rotate(double rad, double x, double y);
    Graphics scale(double sx, double sy);
    Graphics shear(double sx, double sy);
    Graphics transform(Transform transform);

    Graphics setTransform(Transform transform);
    Transform getTransform();

    Graphics drawPoint(int x, int y, PaintContext paint);
    Graphics drawLine(int x0, int y0, int x1, int y1, PaintContext paint);
    Graphics drawRect(int x, int y, int w, int h, PaintContext paint);
    Graphics drawOval(int x, int y, int w, int h, PaintContext paint);
    Graphics drawArc(int x, int y, int w, int h, double startRad, double arcExtent, ArcType type, PaintContext paint);
    Graphics drawArc(int x, int y, int w, int h, Angle start, Angle extent, ArcType type, PaintContext paint);
    Graphics drawRoundRect(int x, int y, int w, int h, int arcWidth, int arcHeight, PaintContext paint);
}