package com.branc.pino.graphics;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;

abstract class GraphicStateBase implements GraphicState {
    private Antialias antialias;
    private Interpolation interpolation;
    private Composite composite;
    private Paint paint;
    private Color background;
    private Stroke stroke;
    private AffineTransform affine;
    private Shape clip;
    private Font font;

    protected GraphicStateBase(
            Antialias antialias,
            Interpolation interpolation,
            Composite composite,
            Paint paint,
            Color background,
            Stroke stroke,
            AffineTransform affine,
            Shape clip,
            Font font
    ) {
        this.antialias = antialias;
        this.interpolation = interpolation;
        this.composite = composite;
        this.paint = paint;
        this.background = background;
        this.stroke = stroke;
        this.affine = affine;
        this.clip = clip;
        this.font = font;
    }

    protected void initState(Graphics2D g) {
        g.addRenderingHints(getRenderingHints());
        g.setComposite(composite);
        g.setPaint(paint);
        g.setBackground(background);
        g.setStroke(stroke);
        if (affine != null) g.setTransform(affine); else g.setTransform(AffineTransform.getTranslateInstance(0, 0));
        g.setClip(clip);
        g.setFont(font);
    }

    private RenderingHints getRenderingHints() {
        RenderingHints hints;
        if (antialias == Antialias.NONE) {
            hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        } else {
            hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        }

        if (interpolation == Interpolation.BICUBIC) {
            hints.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        } else if (interpolation == Interpolation.BILINEAR) {
            hints.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        } else {
            hints.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        }
        return hints;
    }

    @Override
    public Antialias getAntialias() {
        return antialias;
    }

    @Override
    public void setAntialias(Antialias antialias) {
        this.antialias = antialias;
    }

    @Override
    public Interpolation getInterpolation() {
        return interpolation;
    }

    @Override
    public void setInterpolation(Interpolation interpolation) {
        this.interpolation = interpolation;
    }

    @Override
    public Composite getComposite() {
        return composite;
    }

    @Override
    public void setComposite(Composite composite) {
        this.composite = composite;
    }

    @Override
    public Paint getPaint() {
        return paint;
    }

    @Override
    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    @Override
    public Color getBackground() {
        return background;
    }

    @Override
    public void setBackground(Color background) {
        this.background = background;
    }

    @Override
    public Stroke getStroke() {
        return stroke;
    }

    @Override
    public void setStroke(Stroke stroke) {
        this.stroke = stroke;
    }

    public AffineTransform getTransform() {
        return affine;
    }

    @Override
    public void clip(Shape s) {
        if (clip == null) {
            clip = s;
        } else {
            if (clip instanceof Area) {
                Area c = (Area) clip;
                c.subtract(new Area(s));
            } else {
                clip = new Area(clip);
                ((Area) clip).subtract(new Area(s));
            }
        }
    }

    @Override
    public void rotate(double rad) {
        if (affine == null) {
            affine = AffineTransform.getRotateInstance(rad);
        } else {
            affine.rotate(rad);
        }
    }

    @Override
    public void rotate(double rad, double centerX, double centerY) {
        if (affine == null) {
            affine = AffineTransform.getRotateInstance(rad, centerX, centerY);
        } else {
            affine.rotate(rad, centerX, centerY);
        }
    }

    @Override
    public void scale(double sx, double sy) {
        if (affine == null) {
            affine = AffineTransform.getScaleInstance(sx, sy);
        } else {
            affine.scale(sx, sy);
        }
    }

    @Override
    public void shear(double shx, double shy) {
        if (affine == null) {
            affine = AffineTransform.getShearInstance(shx, shy);
        } else {
            affine.shear(shx, shy);
        }
    }

    @Override
    public void translate(double tx, double ty) {
        if (affine == null) {
            affine = AffineTransform.getTranslateInstance(tx, ty);
        } else {
            affine.translate(tx, ty);
        }
    }

    @Override
    public void transform(AffineTransform t) {
        if (affine == null) {
            affine = new AffineTransform(t);
        } else {
            affine.concatenate(t);
        }
    }

    public void setTransform(AffineTransform affine) {
        this.affine = new AffineTransform(affine);
    }

    @Override
    public Shape getClip() {
        return clip;
    }

    @Override
    public void setClip(Shape clip) {
        this.clip = clip;
    }

    @Override
    public Font getFont() {
        return font;
    }

    @Override
    public void setFont(Font font) {
        this.font = font;
    }
}
