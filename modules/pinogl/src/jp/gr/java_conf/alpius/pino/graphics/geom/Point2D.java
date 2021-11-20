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

public record Point2D(double x, double y) {
    public static final Point2D ZERO = new Point2D(0, 0);

    public static Point2D at(double x, double y) {
        if (x == 0 && y == 0) {
            return ZERO;
        }
        return new Point2D(x, y);
    }

    public Point2D add(double x, double y) {
        return at(this.x + x, this.y + y);
    }

    public Point2D add(Point2D p) {
        return at(x + p.x, y + p.y);
    }

    public Point2D subtract(double x, double y) {
        return at(this.x - x, this.y - y);
    }

    public Point2D subtract(Point2D p) {
        return at(this.x - p.x, this.y - p.y);
    }

    public Point2D multiply(double factor) {
        return at(this.x * factor, this.y * factor);
    }
}
