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
import jp.gr.java_conf.alpius.pino.graphics.Graphics2DNoOp;
import jp.gr.java_conf.alpius.pino.graphics.canvas.Canvas;
import jp.gr.java_conf.alpius.pino.memento.IncompatibleMementoException;
import jp.gr.java_conf.alpius.pino.memento.Memento;
import jp.gr.java_conf.alpius.pino.memento.MementoBase;

import java.awt.*;
import java.awt.image.BufferedImage;

public class DrawableLayer extends LayerObject {
    // ロック時、描画不可
    @Bind
    private volatile boolean locked = false;

    public final boolean isLocked() {
        return locked;
    }

    public final void setLocked(boolean value) {
        if (locked != value) {
            var old = locked;
            locked = value;
            firePropertyChange("locked", old, locked);
        }
    }

    @Override
    public final Canvas getCanvas() {
        return super.getCanvas();
    }

    @Override
    protected void renderContent(Graphics2D g, Shape aoi, boolean ignoreRough) {
        g.drawImage(getCanvas().snapshot(), 0, 0, null);
    }

    /**
     * このレイヤに描画を行うグラフィックスコンテキストを返します。<br>
     * このメソッドを使用して返されたグラフィックスコンテキストは、レイヤの座標とキャンバスの座標を補正するアフィン変換が設定された状態になります。
     * ただし、{@link DrawableLayer#isLocked()}がtrueのとき、{@link Graphics2DNoOp}のインスタンスが返され、この場合はアフィン変換は設定されません。<br>
     * このようなオブジェクトの選択を回避したい場合、{@code getCanvas().createGraphics()}のルートを使用することが出来ます。
     * この回避ルートを使用してグラフィックスコンテキストを獲得した場合、デフォルトではレイヤーのプロパティが反映されていないことに注意する必要があります。
     * @return このレイヤーに描画するグラフィックスコンテキスト
     */
    public Graphics2D createGraphics() {
        if (isLocked()) {
            return Graphics2DNoOp.getInstance();
        }

        var g = getCanvas().createGraphics();
        g.translate(-getX(), -getY());
        g.rotate(-getRotate() * Math.PI * 2 / 360);
        g.scale(1 / getScaleX(), 1 / getScaleY());
        return g;
    }

    public Memento<?> createMemento() {
        return new MyMemento(this, super.createMemento());
    }

    public void restore(Memento<?> memento) {
        if (memento instanceof MyMemento m) {
            super.restore(m.getParent());
            var g = createGraphics();
            g.setComposite(AlphaComposite.Src);
            g.drawImage(m.offscreenImage, 0, 0, null);
            g.dispose();
        } else {
            throw new IncompatibleMementoException();
        }
    }

    private static class MyMemento extends MementoBase<DrawableLayer> {
        private final BufferedImage offscreenImage;

        public MyMemento(DrawableLayer layer, Memento<?> superMemento) {
            super(layer, superMemento);
            offscreenImage = layer.getCanvas().snapshot();
        }
    }
}
