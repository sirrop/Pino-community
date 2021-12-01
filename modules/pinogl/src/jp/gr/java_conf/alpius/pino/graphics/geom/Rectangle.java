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

import jp.gr.java_conf.alpius.pino.graphics.transform.Transform;

import java.util.Iterator;
import java.util.Objects;

public class Rectangle extends Shape {
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return w;
    }

    @Override
    public String toString() {
        return "Rectangle{" +
                "x=" + x +
                ", y=" + y +
                ", w=" + w +
                ", h=" + h +
                '}';
    }

    public double getHeight() {
        return h;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rectangle that = (Rectangle) o;
        return Double.compare(that.x, x) == 0 && Double.compare(that.y, y) == 0 && Double.compare(that.w, w) == 0 && Double.compare(that.h, h) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, w, h);
    }

    private final double x;
    private final double y;
    private final double w;
    private final double h;

    private Rectangle(double x, double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public static Rectangle makeXYWH(double x, double y, double w, double h) {
        return new Rectangle(x, y, w, h);
    }

    public static Rectangle makeWH(double w, double h) {
        return makeXYWH(0, 0, w, h);
    }

    @Override
    public Iterator<PathElement> iterator(Transform tx) {
        return new RectangleIterator(this, tx);
    }
}
