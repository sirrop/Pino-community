package com.branc.pino.graphics;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

public interface DrawingApi extends GraphicState {
    void setAntialiasing(Antialiasing antialiasing);
    Antialiasing getAntialiasing();

    void setInterpolation(Interpolation interpolation);
    Interpolation getInterpolation();

    void setComposite(Composite composite);
    Composite getComposite();

    void setBackground(Color c);
    Color getBackground();

    void setPaint(Paint paint);
    Paint getPaint();

    void setStroke(Stroke stroke);
    Stroke getStroke();

    void clip(Shape s);
    Shape getClip();
    void setClip(Shape s);

    void setFont(Font font);
    Font getFont();


    AffineTransform getTransform();
    void setTransform(AffineTransform affine);

    void rotate(double rad);
    void rotate(double rad, double centerX, double centerY);

    void scale(double sx, double sy);
    void shear(double shx, double shy);

    void translate(double tx, double ty);

    void transform(AffineTransform t);

    DrawingApi lineTo(double x, double y);
    DrawingApi curveTo(
            double x1, double y1,
            double x2, double y2,
            double x3, double y3
    );
    DrawingApi quadTo(double x1, double y1, double x2, double y2);
    DrawingApi moveTo(double x, double y);
    default DrawingApi lineRelative(double dx, double dy) {
        return lineTo(getCurrentX() + dx, getCurrentY() + dy);
    }
    default DrawingApi moveRelative(double dx, double dy) {
        return moveTo(getCurrentX() + dx, getCurrentY() + dy);
    }
    double getCurrentX();
    double getCurrentY();

    default DrawingApi drawString(String str) {
        return drawString(str, 0, 0);
    }
    DrawingApi drawString(String str, double x, double y);

    default DrawingApi drawCircle(double width) {
        return drawCircle(width, 0, 0);
    }
    default DrawingApi drawCircle(double width, double x, double y) {
        Shape s = new Arc2D.Double(getCurrentX() + x, getCurrentY() + y, width, width, 0, 360, Arc2D.CHORD);
        return draw(s);
    }

    default DrawingApi drawRect(double width, double height) {
        return drawRect(width, height, 0, 0);
    }
    default DrawingApi drawRect(double width, double height, double x, double y) {
        Shape s = new Rectangle2D.Double(
                x + getCurrentX(), y + getCurrentY(),
                width, height);
        return draw(s);
    }

    default DrawingApi drawRoundRect(double width, double height, double arcW, double arcH) {
        return drawRoundRect(width, height, arcW, arcH, 0, 0);
    }
    
    default DrawingApi drawRoundRect(
            double width, double height,
            double arcW, double arcH,
            double x, double y) {
        Shape s = new RoundRectangle2D.Double(
                x + getCurrentX(), y + getCurrentY(),
                width, height,
                arcW, arcH
        );
        return draw(s);
    }



    DrawingApi draw(Shape s);

    @Override
    void dispose();
}
