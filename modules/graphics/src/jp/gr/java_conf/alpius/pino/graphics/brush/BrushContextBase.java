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

package jp.gr.java_conf.alpius.pino.graphics.brush;

import jp.gr.java_conf.alpius.pino.graphics.DelegatingGraphics2D;
import jp.gr.java_conf.alpius.pino.graphics.brush.event.DrawEvent;
import jp.gr.java_conf.alpius.pino.graphics.layer.DrawableLayer;
import jp.gr.java_conf.alpius.pino.memento.Memento;

public class BrushContextBase<T extends Brush> extends DelegatingGraphics2D implements BrushContext {
    private final T brush;
    private final DrawableLayer layer;
    private Memento<?> memento;

    public BrushContextBase(T brush, DrawableLayer layer) {
        super(layer.createGraphics());
        this.brush = brush;
        this.layer = layer;
        initialize();
    }

    protected void initialize() {

    }

    protected final void saveLayer() {
        memento = layer.createMemento();
    }

    protected final void restoreLayer() {
        layer.restore(memento);
    }

    public final T getBrush() {
        return brush;
    }

    /**
     * 描画のターゲットを返します。このメソッドで返されるレイヤーの参照は、このオブジェクトが生きている間変更されません
     * @return 描画のターゲット
     */
    protected final DrawableLayer getTarget() {
        return layer;
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void onStart(DrawEvent e) {
        onDrawing(e);
    }

    @Override
    public void onDrawing(DrawEvent e) {

    }

    @Override
    public void onFinished(DrawEvent e) {
        onDrawing(e);
    }
}
