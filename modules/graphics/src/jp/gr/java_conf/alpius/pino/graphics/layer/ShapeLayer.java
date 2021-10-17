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

package jp.gr.java_conf.alpius.pino.graphics.layer;

import jp.gr.java_conf.alpius.pino.beans.Bind;
import jp.gr.java_conf.alpius.pino.graphics.internal.util.ColorUtils;
import jp.gr.java_conf.alpius.pino.memento.IncompatibleMementoException;
import jp.gr.java_conf.alpius.pino.memento.Memento;
import jp.gr.java_conf.alpius.pino.memento.MementoBase;

import java.awt.*;

/**
 * 単純な図形を表すレイヤーです。
 */
public abstract class ShapeLayer extends LayerObject {
    @Bind
    private Color fill = new Color(0x4169e1);

    public final Color getFill() {
        return fill;
    }

    public final void setFill(Color fill) {
        if (!(fill == null && this.fill == ColorUtils.TRANSPARENT) && this.fill != fill) {
            if (fill == null) {
                fill = ColorUtils.TRANSPARENT;
            }

            var old = this.fill;
            this.fill = fill;
            firePropertyChange("fill", old, fill);
        }
    }

    @Override
    protected abstract void renderContent(Graphics2D g, Shape aoi, boolean ignoreRough);

    public Memento<?> createMemento() {
        return new ShapeMemento(this, super.createMemento());
    }

    public void restore(Memento<?> memento) {
        if (memento == null) {
            throw new NullPointerException("memento is null!");
        }

        super.restore(memento.getParent());

        if (memento instanceof ShapeMemento m) {
            fill = m.fill;
        } else {
            throw new IncompatibleMementoException("\"memento\" is not compatible to LayerObject.");
        }
    }

    private static class ShapeMemento extends MementoBase<ShapeLayer> {
        private final Color fill;
        public ShapeMemento(ShapeLayer originator, Memento<?> parentMemento) {
            super(originator, parentMemento);
            fill = originator.fill;
        }
    }
}
