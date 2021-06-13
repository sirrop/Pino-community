package com.branc.pino.graphics.geom;

import com.mchange.util.AssertException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Rect {
    public static final Rect EMPTY = new Rect(0, 0, 0, 0);

    private final float x;
    private final float y;
    private final float w;
    private final float h;

    private Rect(float x,
                float y,
                float w,
                float h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public static Rect makeWH(float w, float h) {
        if (w < 0) throw new IllegalArgumentException();
        if (h < 0) throw new IllegalArgumentException();
        return new Rect(0, 0, w, h);
    }

    public static Rect makeXYWH(float x, float y, float w, float h) {
        if (w < 0) throw new IllegalArgumentException();
        if (h < 0) throw new IllegalArgumentException();
        return new Rect(x, y, w, h);
    }

    public static Rect makeLTRB(float left, float top, float right, float bottom) {
        if (left > right) throw new IllegalArgumentException();
        if (top > bottom) throw new IllegalArgumentException();
        return new Rect(left, top, right - left, bottom - top);
    }

    public float getWidth()  {
        return w;
    }

    public float getHeight() {
        return h;
    }

    public float getMinX() {
        return x;
    }

    public float getMinY() {
        return y;
    }

    public float getMaxX() {
        return x + w;
    }

    public float getMaxY() {
        return y + h;
    }

    public Point2D getPoint(Location location) {
        switch (location) {
            case TOP_LEFT: return new Point2D(getMinX(), getMinY());
            case TOP_RIGHT: return new Point2D(getMaxX(), getMinY());
            case BOTTOM_LEFT: return new Point2D(getMinX(), getMaxY());
            case BOTTOM_RIGHT: return new Point2D(getMaxX(), getMaxY());
        }
        throw new AssertionError();
    }

    @Nullable
    public Rect intersect(@NotNull Rect other) {
        if (getMaxX() <= other.getMinX() ||
        other.getMaxX() <= getMinX() ||
        getMaxY() <= other.getMinY() ||
        other.getMaxY() <= getMinY()) return null;
        return makeLTRB(
                Math.max(getMinX(), other.getMinX()),
                Math.max(getMinY(), other.getMinY()),
                Math.max(getMaxX(), other.getMaxX()),
                Math.max(getMaxY(), other.getMaxY())
        );
    }

    @NotNull
    public Rect scale(float scale) {
        return scale(scale, scale);
    }

    @NotNull
    public Rect scale(float sx, float sy) {
        return makeXYWH(x, y, w * sx, h * sy);
    }

    @NotNull
    public Rect offset(float dx, float dy) {
        return makeXYWH(x + dx, y + dy, w, h);
    }

    @NotNull
    public Rect offset(@NotNull Point2D vec) {
        return offset(vec.getX(), vec.getY());
    }

    public enum Location {
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT
    }
}
