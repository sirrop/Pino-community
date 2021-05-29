package com.branc.pino.paint.brush;

import com.branc.pino.core.util.Disposable;
import com.branc.pino.graphics.Antialiasing;
import com.branc.pino.graphics.DrawingApi;
import com.branc.pino.graphics.Interpolation;
import com.branc.pino.graphics.PinoGraphics;

import java.awt.*;
import java.awt.geom.AffineTransform;

public abstract class Brush<T extends BrushContext> implements Disposable, PinoGraphics {


    @Override
    public void setAntialiasing(Antialiasing antialiasing) {
        g.setAntialiasing(antialiasing);
    }

    @Override
    public Antialiasing getAntialiasing() {
        return g.getAntialiasing();
    }

    @Override
    public void setInterpolation(Interpolation interpolation) {
        g.setInterpolation(interpolation);
    }

    @Override
    public Interpolation getInterpolation() {
        return g.getInterpolation();
    }

    @Override
    public void setComposite(Composite composite) {
        g.setComposite(composite);
    }

    @Override
    public Composite getComposite() {
        return g.getComposite();
    }

    @Override
    public void setBackground(Color c) {
        g.setBackground(c);
    }

    @Override
    public Color getBackground() {
        return g.getBackground();
    }

    @Override
    public void setPaint(Paint paint) {
        g.setPaint(paint);
    }

    @Override
    public Paint getPaint() {
        return g.getPaint();
    }

    @Override
    public void setStroke(Stroke stroke) {
        g.setStroke(stroke);
    }

    @Override
    public Stroke getStroke() {
        return g.getStroke();
    }

    @Override
    public void clip(Shape s) {
        g.clip(s);
    }

    @Override
    public Shape getClip() {
        return g.getClip();
    }

    @Override
    public void setClip(Shape s) {
        g.setClip(s);
    }

    @Override
    public void setFont(Font font) {
        g.setFont(font);
    }

    @Override
    public Font getFont() {
        return g.getFont();
    }

    @Override
    public AffineTransform getTransform() {
        return g.getTransform();
    }

    @Override
    public void setTransform(AffineTransform affine) {
        g.setTransform(affine);
    }

    @Override
    public void rotate(double rad) {
        g.rotate(rad);
    }

    @Override
    public void rotate(double rad, double centerX, double centerY) {
        g.rotate(rad, centerX, centerY);
    }

    @Override
    public void scale(double sx, double sy) {
        g.scale(sx, sy);
    }

    @Override
    public void shear(double shx, double shy) {
        g.shear(shx, shy);
    }

    @Override
    public void translate(double tx, double ty) {
        g.translate(tx, ty);
    }

    @Override
    public void transform(AffineTransform t) {
        g.transform(t);
    }

    @Override
    public DrawingApi fill() {
        return g.fill();
    }

    @Override
    public DrawingApi outline() {
        return g.outline();
    }


    private final T context;
    private final PinoGraphics g;

    public Brush(T context, PinoGraphics g) {
        this.context = context;
        this.g = g;
    }

    public T getContext() {
        return context;
    }


    public void init() {}
    public abstract void first(double x, double y);
    public abstract void main(double x, double y);
    public abstract void last(double x, double y);

    @Override
    public void dispose() {
        g.dispose();
    }
}
