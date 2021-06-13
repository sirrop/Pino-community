package com.branc.pino.graphics.geom;

import org.jetbrains.annotations.NotNull;

public final class Point2D {
    public static final Point2D ZERO = new Point2D(0, 0);

    private final float x;

    public final float y;

    public Point2D(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    @NotNull
    public Point2D offset(float dx, float dy) {
        float newX = x + dx;
        float newY = y + dy;
        return new Point2D(newX, newY);
    }

    @NotNull
    public Point2D offset(@NotNull Point2D vec) {
        return offset(vec.x, vec.y);
    }

    @NotNull
    public Point2D scale(float scale) {
        return scale(scale, scale);
    }

    @NotNull
    public Point2D scale(float sx, float sy) {
        return new Point2D(x * sx, y * sy);
    }
}
