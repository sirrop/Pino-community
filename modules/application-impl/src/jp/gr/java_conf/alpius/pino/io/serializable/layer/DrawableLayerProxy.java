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

package jp.gr.java_conf.alpius.pino.io.serializable.layer;

import jp.gr.java_conf.alpius.pino.annotation.Beta;
import jp.gr.java_conf.alpius.pino.graphics.layer.DrawableLayer;
import jp.gr.java_conf.alpius.pino.io.serializable.canvas.CanvasProxy;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.util.Objects;


@Beta
public class DrawableLayerProxy extends LayerProxy {
    @Serial
    private static final long serialVersionUID = -7042378345476013826L;

    public DrawableLayerProxy(DrawableLayer layer) {
        super(layer);
        locked = layer.isLocked();
        opacityProtected = layer.isOpacityProtected();
        canvasProxy = CanvasProxy.create(layer.getCanvas());
    }

    private transient boolean locked;
    private transient boolean opacityProtected;
    private transient CanvasProxy canvasProxy;

    @Override
    public DrawableLayer createLayer(int w, int h) {
        DrawableLayer layer = new DrawableLayer(canvasProxy.createCanvas(w, h));
        initializeLayer(layer, w, h);
        layer.setLocked(locked);
        layer.setOpacityProtected(opacityProtected);
        return layer;
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeBoolean(locked);
        out.writeBoolean(opacityProtected);
        out.writeObject(canvasProxy);
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        locked = in.readBoolean();
        opacityProtected = in.readBoolean();
        canvasProxy = (CanvasProxy) Objects.requireNonNull(in.readObject());
    }
}
