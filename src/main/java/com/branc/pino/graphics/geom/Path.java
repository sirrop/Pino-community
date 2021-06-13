package com.branc.pino.graphics.geom;

import com.branc.pino.core.util.LinkedNode;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.LinkedList;

public final class Path implements Iterable<PathSegment> {
    private final LinkedList<PathSegment> paths = new LinkedList<>();

    @NotNull
    public Path moveTo(float x, float y) {
        return moveTo(new Point2D(x, y));
    }

    @NotNull
    public Path moveTo(@NotNull Point2D p) {
        paths.add(new PathSegment(PathVerb.MOVE_TO, p, null, null, null));
        return this;
    }

    @NotNull
    public Path lineTo(float x, float y) {
        return lineTo(new Point2D(x, y));
    }

    @NotNull
    public Path lineTo(@NotNull Point2D p) {
        paths.add(new PathSegment(PathVerb.LINE_TO, p, null, null, null));
        return this;
    }

    @NotNull
    public Path quadTo(float x1, float y1, float x2, float y2) {
        return quadTo(new Point2D(x1, y1), new Point2D(x2, y2));
    }

    @NotNull
    public Path quadTo(Point2D p1, Point2D p2) {
        paths.add(new PathSegment(PathVerb.QUAD_TO, p1, p2, null, null));
        return this;
    }

    @NotNull
    public Path cubicTo(float x1, float y1, float x2, float y2, float x3, float y3) {
        return cubicTo(new Point2D(x1, y1), new Point2D(x2, y2), new Point2D(x3, y3));
    }

    @NotNull
    public Path cubicTo(@NotNull Point2D p1, @NotNull Point2D p2, @NotNull Point2D p3) {
        paths.add(new PathSegment(PathVerb.CUBIC_TO, p1, p2, p3, null));
        return this;
    }

    @NotNull
    public Path closePath() {
        paths.add(new PathSegment(PathVerb.CLOSE, null, null, null, null));
        return this;
    }

    @NotNull
    public Path append(Rect rect) {
        return append(rect, PathDirection.CLOCKWISE);
    }

    @NotNull
    public Path append(Rect rect, PathDirection dir) {
        return append(rect, dir, Rect.Location.TOP_LEFT);
    }

    private static final LinkedNode<Rect.Location> TOP_LEFT;
    private static final LinkedNode<Rect.Location> TOP_RIGHT;
    private static final LinkedNode<Rect.Location> BOTTOM_LEFT;
    private static final LinkedNode<Rect.Location> BOTTOM_RIGHT;

    static {
        TOP_LEFT = new LinkedNode<>(Rect.Location.TOP_LEFT);
        TOP_RIGHT = new LinkedNode<>(Rect.Location.TOP_RIGHT);
        BOTTOM_LEFT = new LinkedNode<>(Rect.Location.BOTTOM_LEFT);
        BOTTOM_RIGHT = new LinkedNode<>(Rect.Location.BOTTOM_RIGHT);
        TOP_LEFT.append(TOP_RIGHT);
        TOP_RIGHT.append(BOTTOM_RIGHT);
        BOTTOM_RIGHT.append(BOTTOM_LEFT);
        BOTTOM_LEFT.append(TOP_LEFT);
    }

    @NotNull
    public Path append(Rect rect, PathDirection dir, Rect.Location start) {
        LinkedNode<Rect.Location> s;
        switch (start) {
            case TOP_LEFT:
                s = TOP_LEFT;
                break;
            case TOP_RIGHT:
                s = TOP_RIGHT;
                break;
            case BOTTOM_LEFT:
                s = BOTTOM_LEFT;
                break;
            case BOTTOM_RIGHT:
                s = BOTTOM_RIGHT;
                break;
            default:
                throw new IllegalArgumentException();
        }
        moveTo(rect.getPoint(s.get()));
        for (int i = 0; i < 3; i++) {
            switch (dir) {
                case CLOCKWISE:
                    s = s.next();
                    break;
                case COUNTER_CLOCKWISE:
                    s = s.previous();
                    break;
            }

            lineTo(rect.getPoint(s.get()));
        }
        return this;
    }

    @NotNull
    public Path append(@NotNull Point2D[] pts, boolean close) {
        for (int i = 0; i < pts.length; i++) {
            if (i == 0) moveTo(pts[0]);
            else lineTo(pts[i]);
        }
        if (close) closePath();
        return this;
    }

    @NotNull
    public Path append(@NotNull Path path) {
        for (PathSegment pathSegment : path) {
            paths.add(pathSegment);
        }
        return this;
    }

    @Override
    public @NotNull Iterator<PathSegment> iterator() {
        return paths.iterator();
    }
}
