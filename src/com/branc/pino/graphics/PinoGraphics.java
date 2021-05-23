package com.branc.pino.graphics;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * Graphics2Dに代わって描画を行うApiです。
 *
 * fillメソッドで返されるDrawingApiは図形の内部を単一のPaintで埋め、outlineで返されるDrawingApiは図形のアウトラインを単一のPaintで描きます。
 * これらのメソッドで返されるDrawingApiはもとのPinoGraphicsと異なる状態を保持しており、DrawingApiで加えた変更はPinoGraphicsには適用されません。
 * しかし、作成されたときのDrawingApiの状態は作成時のPinoGraphicsの状態と同じになります。
 *
 * <p>
 *     <b>重要：PinoGraphicsをdisposeすると、子のDrawingApiもすべてdisposeされます。しかし、子のDrawingApiをdisposeしても、親のPinoGraphicsには影響を与えません</b>
 * </p>
 */
public interface PinoGraphics extends GraphicState {
    /**
     * BufferedImageに描画を行うPinoGraphicsオブジェクトを作成します。
     * <br />
     *  <b>このメソッドで作成されるPinoGraphicsはスレッドセーフではありません。</b>
     * マルチスレッドで使用する場合は手動での同期処理を行うか、
     * スレッド毎に異なるPinoGraphicsを使用してください
     * @param target 描画対象
     * @return 描画を行うPinoGraphicsオブジェクト
     */
    static PinoGraphics create(BufferedImage target) {
        return new BufImgPinoGraphics(target);
    }

    PinoGraphics NO_OP = new NoOp();

    /**
     * @return すべての操作を無視するPinoGraphics
     */
    static PinoGraphics noOp() {
        return NO_OP;
    }

    void setAntialias(Antialias antialias);
    Antialias getAntialias();

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

    DrawingApi fill();
    DrawingApi outline();

    @Override
    void dispose();
}
