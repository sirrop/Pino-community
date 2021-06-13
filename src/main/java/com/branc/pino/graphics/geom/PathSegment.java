package com.branc.pino.graphics.geom;

import org.jetbrains.annotations.NotNull;

import static com.branc.pino.graphics.geom.PathVerb.*;

public final class PathSegment {
    public PathSegment(@NotNull PathVerb verb, Point2D p0, Point2D p1, Point2D p2, Point2D p3) {
        this.verb = verb;
        this.p0 = p0;
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
    }

    @NotNull
    private final PathVerb verb;


    private final Point2D p0;


    private final Point2D p1;


    private final Point2D p2;


    private final Point2D p3;

    public final boolean isClose() {
        return verb == CLOSE;
    }

    public final boolean isLineTo() {
        return verb == LINE_TO;
    }

    public final boolean isMoveTo() {
        return verb == MOVE_TO;
    }

    public final boolean isCubicTo() {
        return verb == CUBIC_TO;
    }

    public final boolean isQuadTo() {
        return verb == QUAD_TO;
    }

    public final boolean isDone() {
        return verb == DONE;
    }
}
