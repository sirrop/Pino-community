package com.branc.pino.graphics;

import java.awt.*;
import java.awt.geom.AffineTransform;

class NoOp implements PinoGraphics {

    @Override
    public void dispose() {

    }

    @Override
    public void setAntialiasing(Antialiasing antialiasing) {

    }

    @Override
    public Antialiasing getAntialiasing() {
        return null;
    }

    @Override
    public void setInterpolation(Interpolation interpolation) {

    }

    @Override
    public Interpolation getInterpolation() {
        return null;
    }

    @Override
    public void setComposite(Composite composite) {

    }

    @Override
    public Composite getComposite() {
        return null;
    }

    @Override
    public void setBackground(Color c) {

    }

    @Override
    public Color getBackground() {
        return null;
    }

    @Override
    public void setPaint(Paint paint) {

    }

    @Override
    public Paint getPaint() {
        return null;
    }

    @Override
    public void setStroke(Stroke stroke) {

    }

    @Override
    public Stroke getStroke() {
        return null;
    }

    @Override
    public void clip(Shape s) {

    }

    @Override
    public Shape getClip() {
        return null;
    }

    @Override
    public void setClip(Shape s) {

    }

    @Override
    public void setFont(Font font) {

    }

    @Override
    public Font getFont() {
        return null;
    }

    @Override
    public AffineTransform getTransform() {
        return null;
    }

    @Override
    public void setTransform(AffineTransform affine) {

    }

    @Override
    public void rotate(double rad) {

    }

    @Override
    public void rotate(double rad, double centerX, double centerY) {

    }

    @Override
    public void scale(double sx, double sy) {

    }

    @Override
    public void shear(double shx, double shy) {

    }

    @Override
    public void translate(double tx, double ty) {

    }

    @Override
    public void transform(AffineTransform t) {

    }

    @Override
    public DrawingApi fill() {
        return NO_OP_API;
    }

    @Override
    public DrawingApi outline() {
        return NO_OP_API;
    }

    private static final NoOpApi NO_OP_API = new NoOpApi();

    private static class NoOpApi implements DrawingApi {

        @Override
        public void dispose() {

        }

        @Override
        public void setAntialiasing(Antialiasing antialiasing) {

        }

        @Override
        public Antialiasing getAntialiasing() {
            return null;
        }

        @Override
        public void setInterpolation(Interpolation interpolation) {

        }

        @Override
        public Interpolation getInterpolation() {
            return null;
        }

        @Override
        public void setComposite(Composite composite) {

        }

        @Override
        public Composite getComposite() {
            return null;
        }

        @Override
        public void setBackground(Color c) {

        }

        @Override
        public Color getBackground() {
            return null;
        }

        @Override
        public void setPaint(Paint paint) {

        }

        @Override
        public Paint getPaint() {
            return null;
        }

        @Override
        public void setStroke(Stroke stroke) {

        }

        @Override
        public Stroke getStroke() {
            return null;
        }

        @Override
        public void clip(Shape s) {

        }

        @Override
        public Shape getClip() {
            return null;
        }

        @Override
        public void setClip(Shape s) {

        }

        @Override
        public void setFont(Font font) {

        }

        @Override
        public Font getFont() {
            return null;
        }

        @Override
        public AffineTransform getTransform() {
            return null;
        }

        @Override
        public void setTransform(AffineTransform affine) {

        }

        @Override
        public void rotate(double rad) {

        }

        @Override
        public void rotate(double rad, double centerX, double centerY) {

        }

        @Override
        public void scale(double sx, double sy) {

        }

        @Override
        public void shear(double shx, double shy) {

        }

        @Override
        public void translate(double tx, double ty) {

        }

        @Override
        public void transform(AffineTransform t) {

        }

        @Override
        public DrawingApi lineTo(double x, double y) {
            return null;
        }

        @Override
        public DrawingApi curveTo(double x1, double y1, double x2, double y2, double x3, double y3) {
            return null;
        }

        @Override
        public DrawingApi quadTo(double x1, double y1, double x2, double y2) {
            return null;
        }

        @Override
        public DrawingApi moveTo(double x, double y) {
            return null;
        }

        @Override
        public double getCurrentX() {
            return 0;
        }

        @Override
        public double getCurrentY() {
            return 0;
        }

        @Override
        public DrawingApi drawString(String str, double x, double y) {
            return null;
        }

        @Override
        public DrawingApi draw(Shape s) {
            return null;
        }
    }
}
