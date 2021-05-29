package com.branc.pino.graphics;

import java.awt.*;
import java.awt.geom.AffineTransform;

interface GraphicState {
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

    void dispose();
}
