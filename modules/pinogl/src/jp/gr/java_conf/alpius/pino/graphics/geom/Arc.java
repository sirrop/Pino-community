package jp.gr.java_conf.alpius.pino.graphics.geom;

import jp.gr.java_conf.alpius.pino.graphics.angle.Angle;
import jp.gr.java_conf.alpius.pino.graphics.angle.StandardAngleUnit;
import jp.gr.java_conf.alpius.pino.graphics.transform.Transform;

import java.util.Iterator;
import java.util.Objects;

public final class Arc extends Shape {
    @Override
    public String toString() {
        return "Arc{" +
                "x=" + x +
                ", y=" + y +
                ", w=" + w +
                ", h=" + h +
                ", startRad=" + startRad +
                ", extentRad=" + extentRad +
                '}';
    }

    private final double x;
    private final double y;
    private final double w;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Arc arc = (Arc) o;
        return Double.compare(arc.x, x) == 0 && Double.compare(arc.y, y) == 0 && Double.compare(arc.w, w) == 0 && Double.compare(arc.h, h) == 0 && Double.compare(arc.startRad, startRad) == 0 && Double.compare(arc.extentRad, extentRad) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, w, h, startRad, extentRad);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return w;
    }

    public double getHeight() {
        return h;
    }

    public double getStartRad() {
        return startRad;
    }

    public double getExtentRad() {
        return extentRad;
    }

    private final double h;
    private final double startRad;
    private final double extentRad;
    private final ArcType type;

    // コンストラクタは非公開です。オブジェクトの作成は、staticメソッドによって提供されます
    private Arc(double x, double y, double w, double h, double start, double extent, ArcType type) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.startRad = start;
        this.extentRad = extent;
        this.type = type;
    }

    public static Arc makeWH(double w, double h, Angle start, Angle extent, ArcType type) {
        var rad = StandardAngleUnit.RAD;
        return makeWH(w, h, start.getAngle(rad), extent.getAngle(rad), type);
    }

    public static Arc makeWH(double w, double h, double start, double extent, ArcType type) {
        return new Arc(0, 0, w, h, start, extent, type);
    }

    public static Arc makeXYWH(double x, double y, double w, double h, Angle start, Angle extent, ArcType type) {
        var rad = StandardAngleUnit.RAD;
        return makeXYWH(x, y, w, h, start.getAngle(rad), extent.getAngle(rad), type);
    }

    public static Arc makeXYWH(double x, double y, double w, double h, double start, double extent, ArcType type) {
        return new Arc(x, y, w, h, start, extent, type);
    }

    @Override
    public Iterator<PathElement> iterator(Transform tx) {
        return new ArcIterator(this, tx);
    }

    public ArcType getType() {
        return type;
    }
}
