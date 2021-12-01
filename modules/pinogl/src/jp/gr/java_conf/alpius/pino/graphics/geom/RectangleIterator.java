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

class RectangleIterator implements Iterator<PathElement> {
    private final Transform tx;

    private final double x;
    private final double y;
    private final double w;
    private final double h;

    private int index = 0;

    public RectangleIterator(Rectangle rect, Transform tx) {
        this.tx = tx;
        x = rect.getX();
        y = rect.getY();
        w = rect.getWidth();
        h = rect.getHeight();
        if (w < 0 || h < 0) {
            index = 6;
        }
    }

    @Override
    public boolean hasNext() {
        return index < 6;
    }

    @Override
    public PathElement next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        index++;

        if (index == 5) {
            return PathElement.close();
        }

        Point2D p = Point2D.at(x, y);

        if (index == 1 || index == 2) {
            p = p.add(w, 0);
        }
        if (index == 2 || index == 3) {
            p = p.add(0, h);
        }

        if (tx != null) {
            p = tx.transform(p);
        }

        if (index == 0) {
            return PathElement.moveTo(p);
        } else {
            return PathElement.lineTo(p);
        }
    }
}
