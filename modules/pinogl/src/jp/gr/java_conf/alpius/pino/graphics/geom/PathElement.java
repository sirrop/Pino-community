/*
 * Copyright [2021] [shiro]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.gr.java_conf.alpius.pino.graphics.geom;

import java.util.Arrays;
import java.util.Objects;

public record PathElement(PathSegment segment, double[] coords) {
    private static final double[] EMPTY = arrayOf();

    public static PathElement close() {
        return new PathElement(PathSegment.CLOSE, EMPTY);
    }

    public static PathElement moveTo(double x, double y) {
        return new PathElement(PathSegment.MOVE_TO, arrayOf(x, y));
    }

    public static PathElement moveTo(Point2D p) {
        return moveTo(p.x(), p.y());
    }

    public static PathElement lineTo(double x, double y) {
        return new PathElement(PathSegment.LINE_TO, arrayOf(x, y));
    }

    public static PathElement lineTo(Point2D p) {
        return lineTo(p.x(), p.y());
    }

    public static PathElement cubicTo(double x0, double y0, double x1, double y1, double x2, double y2) {
        return new PathElement(PathSegment.CUBIC_TO, arrayOf(x0, y0, x1, y1, x2, y2));
    }

    public static PathElement cubicTo(Point2D p0, Point2D p1, Point2D p2) {
        return cubicTo(p0.x(), p0.y(), p1.x(), p1.y(), p2.x(), p2.y());
    }

    public static PathElement quadTo(double x0, double y0, double x1, double y1, double x2, double y2) {
        return new PathElement(PathSegment.QUAD_TO, arrayOf(x0, y0, x1, y1, x2, y2));
    }

    public static PathElement quadTo(Point2D p0, Point2D p1, Point2D p2) {
        return quadTo(p0.x(), p0.y(), p1.x(), p1.y(), p2.x(), p2.y());
    }

    private static double[] arrayOf(double... values) {
        return values;
    }

    public PathElement(PathSegment segment, double[] coords) {
        this.segment = Objects.requireNonNull(segment);
        if (coords.length == 0) {
            this.coords = coords;
        } else {
            this.coords = Arrays.copyOf(coords, coords.length);
        }
    }

    @Override
    public double[] coords() {
        return Arrays.copyOf(coords, coords.length);
    }
}
