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

package jp.gr.java_conf.alpius.pino.graphics.layer.geom;

import jp.gr.java_conf.alpius.pino.beans.Bind;
import jp.gr.java_conf.alpius.pino.beans.Min;
import jp.gr.java_conf.alpius.pino.graphics.layer.ShapeLayer;
import jp.gr.java_conf.alpius.pino.memento.IncompatibleMementoException;
import jp.gr.java_conf.alpius.pino.memento.Memento;
import jp.gr.java_conf.alpius.pino.memento.MementoBase;

import java.awt.*;

public class Ellipse extends ShapeLayer {
    @Bind
    @Min(0)
    private int width = 100;

    public int getWidth() {
        return width;
    }

    public void setWidth(int value) {
        if (width < 0) {
            throw new IllegalArgumentException();
        }
        if (value != width) {
            var old = width;
            width = value;
            firePropertyChange("width", old, value);
        }
    }

    @Bind
    @Min(0)
    private int height = 100;

    public int getHeight() {
        return height;
    }

    public void setHeight(int value) {
        if (height < 0) {
            throw new IllegalArgumentException();
        }
        if (value != height) {
            var old = height;
            height = value;
            firePropertyChange("height", old, value);
        }
    }

    @Override
    protected void renderContent(Graphics2D g, Shape aoi, boolean ignoreRough) {
        g.addRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
        g.setPaint(getFill());
        g.fillArc(0, 0, width, height, 0, 360);
    }

    public Memento<?> createMemento() {
        return new EllipseMemento(this, super.createMemento());
    }

    public void restore(Memento<?> memento) {
        if (memento == null) {
            throw new NullPointerException("memento is null!");
        }

        super.restore(memento.getParent());

        if (memento instanceof EllipseMemento m) {
            width = m.width;
            height = m.height;
        } else {
            throw new IncompatibleMementoException("\"memento\" is not compatible to LayerObject.");
        }
    }

    private static class EllipseMemento extends MementoBase<Ellipse> {
        private final int width;
        private final int height;
        public EllipseMemento(Ellipse originator, Memento<?> parentMemento) {
            super(originator, parentMemento);
            width = originator.width;
            height = originator.height;
        }
    }
}
