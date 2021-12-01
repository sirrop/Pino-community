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
import jp.gr.java_conf.alpius.pino.graphics.layer.LayerObject;
import jp.gr.java_conf.alpius.pino.graphics.layer.ParentBase;
import jp.gr.java_conf.alpius.pino.io.serializable.composite.CompositeProxy;

import java.io.*;
import java.util.Objects;

@Beta
public abstract class LayerProxy implements Serializable {
    public static LayerProxy create(LayerObject layer) {
        if (layer == null) return null;
        if (layer instanceof DrawableLayer drawable) {
            return new DrawableLayerProxy(drawable);
        }
        throw new IllegalArgumentException("Unknown layer");
    }

    @Serial
    private static final long serialVersionUID = 1393285591675544735L;

    private enum State {
        INITIALIZING, INITIALIZED
    }

    private transient State state;

    private transient String name;
    private transient float opacity;
    private transient boolean visible;
    private transient boolean rough;
    private transient CompositeProxy composite;
    private transient double x;
    private transient double y;
    private transient double rotate;
    private transient double scaleX;
    private transient double scaleY;
    private transient LayerProxy clip;
    private transient ParentProxy parent;


    protected LayerProxy(LayerObject layer) {
        initializer().withName(layer.getName())
                .withOpacity(layer.getOpacity())
                .withVisible(layer.isVisible())
                .withRough(layer.isRough())
                .withComposite(CompositeProxy.create(layer.getCompositeFactory()))
                .withX(layer.getX())
                .withY(layer.getY())
                .withRotate(layer.getRotate())
                .withScaleX(layer.getScaleX())
                .withScaleY(layer.getScaleY())
                .withClip(LayerProxy.create(layer.getClip()))
                .withParent((ParentProxy) LayerProxy.create((ParentBase) layer.getParent()))
                .initialize();
    }

    private Initializer initializer() {
        if (state == State.INITIALIZED) {
            throw new IllegalStateException("LayerProxy has been already initialized.");
        }
        state = State.INITIALIZING;
        return new Initializer(this);
    }

    public abstract LayerObject createLayer(int w, int h);

    protected final void initializeLayer(LayerObject layer, int w, int h) {
        if (state != State.INITIALIZED) {
            throw new IllegalStateException("LayerProxy has not been initialized yet.");
        }
        layer.setName(name);
        layer.setOpacity(opacity);
        layer.setVisible(visible);
        layer.setRough(rough);
        layer.setCompositeFactory(composite.create());
        layer.setX(x);
        layer.setY(y);
        layer.setRotate(rotate);
        layer.setScaleX(scaleX);
        layer.setScaleY(scaleY);
        if (clip != null) {
            layer.setClip(clip.createLayer(w, h));
        }
        if (parent != null) {
            layer.setParent(parent.createLayer(w, h));
        }
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(name);
        out.writeFloat(opacity);
        out.writeBoolean(visible);
        out.writeBoolean(rough);
        out.writeObject(composite);
        out.writeDouble(x);
        out.writeDouble(y);
        out.writeDouble(rotate);
        out.writeDouble(scaleX);
        out.writeDouble(scaleY);
        out.writeObject(clip);
        out.writeObject(parent);
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        var name = (String) in.readObject();
        var opacity = in.readFloat();
        var visible = in.readBoolean();
        var rough = in.readBoolean();
        var composite = (CompositeProxy) in.readObject();
        var x = in.readDouble();
        var y = in.readDouble();
        var rotate = in.readDouble();
        var scaleX = in.readDouble();
        var scaleY = in.readDouble();
        var clip = (LayerProxy) in.readObject();
        var parent = (ParentProxy) in.readObject();

        initializer().withName(name)
                .withOpacity(opacity)
                .withVisible(visible)
                .withRough(rough)
                .withComposite(composite)
                .withX(x)
                .withY(y)
                .withRotate(rotate)
                .withScaleX(scaleX)
                .withScaleY(scaleY)
                .withClip(clip)
                .withParent(parent)
                .initialize();
    }

    private static class Initializer {
        private final LayerProxy proxy;
        Initializer(LayerProxy proxy) {
            this.proxy = proxy;
        }

        private String name;
        private float opacity;
        private boolean visible;
        private boolean rough;
        private CompositeProxy composite;
        private double x;
        private double y;
        private double rotate;
        private double scaleX;
        private double scaleY;
        private LayerProxy clip;
        private ParentProxy parent;

        public void initialize() {
            Objects.requireNonNull(name, "name == null");
            checkIsNaNOrInfinite(opacity);
            Objects.requireNonNull(composite, "compositeFactory == null");
            checkIsNaNOrInfinite(x, "x");
            checkIsNaNOrInfinite(y, "y");
            checkIsNaNOrInfinite(rotate, "rotate");
            checkIsNaNOrInfinite(scaleX, "scaleX");
            checkIsNaNOrInfinite(scaleY, "scaleY");
            if (opacity < 0 || 1 < opacity) {
                throw new IllegalArgumentException("opacity < 0 || 1 < opacity");
            }
            if (scaleX < 0) {
                throw new IllegalArgumentException("scaleX < 0");
            }
            if (scaleY < 0) {
                throw new IllegalArgumentException("scaleY < 0");
            }

            proxy.name = name;
            proxy.opacity = opacity;
            proxy.visible = visible;
            proxy.rough = rough;
            proxy.composite = composite;
            proxy.x = x;
            proxy.y = y;
            proxy.rotate = rotate;
            proxy.scaleX = scaleX;
            proxy.scaleY = scaleY;
            proxy.clip = clip;
            proxy.parent = parent;

            proxy.state = State.INITIALIZED;
        }

        private static void checkIsNaNOrInfinite(float value) {
            if (Float.isInfinite(value)) {
                throw new IllegalArgumentException("opacity" + " is Infinite");
            }
            if (Float.isNaN(value)) {
                throw new IllegalArgumentException("opacity" + " is NaN");
            }
        }

        private static void checkIsNaNOrInfinite(double value, String name) {
            if (Double.isInfinite(value)) {
                throw new IllegalArgumentException(name + " is Infinite");
            }
            if (Double.isNaN(value)) {
                throw new IllegalArgumentException(name + " is NaN");
            }
        }

        public Initializer withName(String name) {
            this.name = name;
            return this;
        }

        public Initializer withOpacity(float opacity) {
            this.opacity = opacity;
            return this;
        }

        public Initializer withVisible(boolean visible) {
            this.visible = visible;
            return this;
        }

        public Initializer withRough(boolean rough) {
            this.rough = rough;
            return this;
        }

        public Initializer withComposite(CompositeProxy composite) {
            this.composite = composite;
            return this;
        }

        public Initializer withX(double x) {
            this.x = x;
            return this;
        }

        public Initializer withY(double y) {
            this.y = y;
            return this;
        }

        public Initializer withRotate(double rotate) {
            this.rotate = rotate;
            return this;
        }

        public Initializer withScaleX(double scaleX) {
            this.scaleX = scaleX;
            return this;
        }

        public Initializer withScaleY(double scaleY) {
            this.scaleY = scaleY;
            return this;
        }

        public Initializer withClip(LayerProxy clip) {
            this.clip = clip;
            return this;
        }

        public Initializer withParent(ParentProxy parent) {
            this.parent = parent;
            return this;
        }
    }
}
