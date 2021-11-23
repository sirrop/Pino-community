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
import java.util.NoSuchElementException;

class ArcIterator implements Iterator<PathElement> {
    private final double x;
    private final double y;
    private final double w;
    private final double h;
    private final double angle;
    private double increment;
    private double cv;
    private final Transform tx;
    private int index;
    private int arcSegs;
    private int lineSegs;

    public ArcIterator(Arc arc, Transform tx) {
        this.w = arc.getWidth() / 2;
        this.h = arc.getHeight() / 2;
        this.x = arc.getX() + w;
        this.y = arc.getY() + h;
        this.angle = -arc.getStartRad();
        this.tx = tx;
        double ext = -arc.getExtentRad();
        if (ext >= 2 * Math.PI || ext <= -2 * Math.PI) {
            arcSegs = 4;
            this.cv = 0.5522847498307933;
            if (ext < 0) {
                increment = -increment;
                cv = -cv;
            }
        } else {
            arcSegs = (int) Math.ceil(Math.abs(ext) * 2 / Math.PI);
            this.increment = ext / arcSegs;
            this.cv = btan(increment);
            if (cv == 0) {
                arcSegs = 0;
            }
        }
        lineSegs = switch (arc.getType()) {
            case OPEN -> 0;
            case CHORD -> 1;
            case ROUND -> 2;
        };
        if (w < 0 || h < 0) {
            arcSegs = lineSegs = -1;
        }
    }

    private static double btan(double increment) {
        increment /= 2.0;
        return 4.0 / 3.0 * Math.sin(increment) / (1.0 + Math.cos(increment));
    }

    @Override
    public boolean hasNext() {
        return index <= arcSegs + lineSegs;
    }

    @Override
    public PathElement next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        index++;
        double angle = this.angle;
        if (index == 0) {
            Point2D start = Point2D.at(x, y);
            Point2D usrStart = tx.transform(start);
            return PathElement.moveTo(usrStart.x(), usrStart.y());
        }
        angle += increment * (index - 1);
        double relx = Math.cos(angle);
        double rely = Math.sin(angle);
        Point2D p0 = Point2D.at(x + (relx - cv * rely) * w, y + (rely + cv * relx) * h);
        angle += increment;
        relx = Math.cos(angle);
        rely = Math.sin(angle);
        Point2D p1 = Point2D.at(x + (relx + cv * rely) * w, y + (rely - cv * relx) * h);
        Point2D p2 = Point2D.at(x + relx * w, y + rely * h);
        if (tx != null) {
            p0 = tx.transform(p0);
            p1 = tx.transform(p1);
            p2 = tx.transform(p2);
        }
        return PathElement.cubicTo(p0, p1, p2);
    }
}
