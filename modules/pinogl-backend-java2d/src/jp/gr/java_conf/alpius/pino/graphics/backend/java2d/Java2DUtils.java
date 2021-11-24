package jp.gr.java_conf.alpius.pino.graphics.backend.java2d;

import jp.gr.java_conf.alpius.pino.graphics.AlphaType;
import jp.gr.java_conf.alpius.pino.graphics.Composite;
import jp.gr.java_conf.alpius.pino.graphics.Image;
import jp.gr.java_conf.alpius.pino.graphics.color.RGBColorSpace;
import jp.gr.java_conf.alpius.pino.graphics.paint.ColorPaint;
import jp.gr.java_conf.alpius.pino.graphics.paint.ImagePaint;
import jp.gr.java_conf.alpius.pino.graphics.paint.Paint;
import jp.gr.java_conf.alpius.pino.graphics.paint.stroke.Stroke;
import jp.gr.java_conf.alpius.pino.graphics.transform.Affine2D;
import jp.gr.java_conf.alpius.pino.graphics.transform.Transform;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public final class Java2DUtils {
    private Java2DUtils() {
    }

    public static AffineTransform toJava2D(Transform transform) {
        var array = ((Affine2D) transform).flatten();
        return new AffineTransform(array);
    }

    public static Transform fromJava2D(AffineTransform transform) {
        var array = new double[9];
        transform.getMatrix(array);
        return Affine2D.makeFromFlatMatrix(array);
    }

    public static BufferedImage toJava2D(Image image) {
        BufferedImage img;
        if (image.getAlphaType() == AlphaType.OPAQUE) {
            img = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        } else if (image.getAlphaType() == AlphaType.UNPREMUL) {
            img = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        } else {
            img = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
        }
        int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
        img.setRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
        return img;
    }

    public static BasicStroke getBasicStroke(Stroke stroke) {
        int cap = switch (stroke.getCap()) {
            case BUTT -> BasicStroke.CAP_BUTT;
            case ROUND -> BasicStroke.CAP_ROUND;
            case SQUARE -> BasicStroke.CAP_SQUARE;
        };
        int join = switch (stroke.getJoin()) {
            case BEVEL -> BasicStroke.JOIN_BEVEL;
            case MITER -> BasicStroke.JOIN_MITER;
            case ROUND -> BasicStroke.JOIN_ROUND;
        };
        float width = stroke.getStrokeWidth();
        return new BasicStroke(width, cap, join);
    }

    public static java.awt.Paint toJava2D(Paint paint) {
        if (paint instanceof ColorPaint c) {
            var color = c.getColor();
            var components = color.getColorComponents(RGBColorSpace.CS_sRGB_D65);
            return new Color(ColorSpace.getInstance(ColorSpace.CS_sRGB), components, color.getOpacity());
        } else if (paint instanceof ImagePaint imagePaint) {
            var image = toJava2D(imagePaint.getImage());
            return new TexturePaint(image, new Rectangle(imagePaint.getX(), imagePaint.getY(), imagePaint.getWidth(), imagePaint.getHeight()));
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public static java.awt.Composite toJava2D(Composite composite, float opacity) {
        var mode = Composite.getBlendMode(composite);
        return switch (mode) {
            case CLEAR -> AlphaComposite.getInstance(AlphaComposite.CLEAR, opacity);
            case SRC -> AlphaComposite.getInstance(AlphaComposite.SRC, opacity);
            case DST -> AlphaComposite.getInstance(AlphaComposite.DST, opacity);
            case SRC_OVER -> AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity);
            case DST_OVER -> AlphaComposite.getInstance(AlphaComposite.DST_OVER, opacity);
            case SRC_IN -> AlphaComposite.getInstance(AlphaComposite.SRC_IN, opacity);
            case DST_IN -> AlphaComposite.getInstance(AlphaComposite.DST_IN, opacity);
            case SRC_ATOP -> AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, opacity);
            case DST_ATOP -> AlphaComposite.getInstance(AlphaComposite.DST_ATOP, opacity);
            case SRC_OUT -> AlphaComposite.getInstance(AlphaComposite.SRC_OUT, opacity);
            case DST_OUT -> AlphaComposite.getInstance(AlphaComposite.DST_OUT, opacity);
            case XOR -> AlphaComposite.getInstance(AlphaComposite.XOR, opacity);
            default -> throw new UnsupportedOperationException("Unsupported BlendMode: " + mode);
        };
    }
}
