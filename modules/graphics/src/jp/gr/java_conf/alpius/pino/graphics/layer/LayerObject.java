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

import jp.gr.java_conf.alpius.pino.beans.BeanPeer;
import jp.gr.java_conf.alpius.pino.beans.Bind;
import jp.gr.java_conf.alpius.pino.beans.Min;
import jp.gr.java_conf.alpius.pino.beans.Range;
import jp.gr.java_conf.alpius.pino.disposable.Disposable;
import jp.gr.java_conf.alpius.pino.graphics.AlphaBlend;
import jp.gr.java_conf.alpius.pino.graphics.CompositeFactory;
import jp.gr.java_conf.alpius.pino.graphics.canvas.Canvas;
import jp.gr.java_conf.alpius.pino.memento.IncompatibleMementoException;
import jp.gr.java_conf.alpius.pino.memento.Memento;
import jp.gr.java_conf.alpius.pino.memento.MementoBase;
import jp.gr.java_conf.alpius.pino.memento.Originator;
import jp.gr.java_conf.alpius.pino.util.Strings;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.util.List;
import java.util.Objects;

/**
 * Layerの基底となるクラスです。
 * <p>
 *     Canvasはコンストラクタが呼び出されたタイミングでは使用することが出来ません。Canvasが必要な初期化は、{@link LayerObject#init()}に記述してください。
 * </p>
 */
public abstract class LayerObject implements Disposable, Originator {
    /* -- Beans Utilities -- */
    private BeanPeer<? extends LayerObject> beanPeer;

    @SuppressWarnings("unchecked")
    <P extends LayerObject> BeanPeer<P> getPeer() {
        if (beanPeer == null) {
            beanPeer = new BeanPeer<>(this);
        }
        return (BeanPeer<P>) beanPeer;
    }

    public final List<PropertyDescriptor> getUnmodifiablePropertyList() {
        return getPeer().getUnmodifiableProperties();
    }

    public final void addListener(PropertyChangeListener listener) {
        getPeer().addListener(listener);
    }

    public final void addListener(PropertyChangeListener listener, Disposable parent) {
        getPeer().addListener(listener, parent);
    }

    public final void removeListener(PropertyChangeListener listener) {
        getPeer().removeListener(listener);
    }

    protected final <T> void firePropertyChange(String propertyName, T oldValue, T newValue) {
        getPeer().firePropertyChange(propertyName, oldValue, newValue);
    }


    /* -- Property Declarations -- */
    @Bind
    private String name = getClass().getSimpleName();

    public final String getName() {
        return name;
    }

    public final void setName(String value) {
        if (!Objects.equals(this.name, value)) {
            String old = name;
            if (Strings.isNullOrEmpty(value)) {
                name = getClass().getSimpleName();
            } else {
                name = value;
            }
            firePropertyChange("name", old, name);
        }
    }


    @Range(min = 0, max = 1)
    @Bind
    private float opacity = 1f;

    public final void setOpacity(float value) {
        if (value < 0 || 1 < value) throw new IllegalArgumentException();
        float old = opacity;
        if (old != value) {
            opacity = value;
            firePropertyChange("opacity", old, opacity);
        }
    }

    public final float getOpacity() {
        return opacity;
    }


    @Bind
    private boolean visible = true;

    public final void setVisible(boolean value) {
        if (visible != value) {
            boolean old = visible;
            visible = value;
            firePropertyChange("visible", old, visible);
        }

    }

    public final boolean isVisible() {
        return visible;
    }


    @Bind
    private boolean rough = false;

    public final void setRough(boolean value) {
        if (rough != value) {
            boolean old = rough;
            rough = value;
            firePropertyChange("rough", old, rough);
        }
    }

    public final boolean isRough() {
        return rough;
    }


    @Bind
    private CompositeFactory compositeFactory = AlphaBlend.Mode.SRC_OVER;

    public CompositeFactory getCompositeFactory() {
        return compositeFactory;
    }

    public void setCompositeFactory(CompositeFactory compositeFactory) {
        if (this.compositeFactory != compositeFactory) {
            var old = this.compositeFactory;
            this.compositeFactory = Objects.requireNonNull(compositeFactory);
            firePropertyChange("blendMode", old, compositeFactory);
        }
    }

    // キャンバス座標 X
    @Bind
    private double x = 0.0;

    /**
     * キャンバスにおけるこのLayerObjectのX座標を返します。
     * @return x
     */
    public final double getX() {
        return x;
    }

    public final void setX(double value) {
        if (x != value) {
            double old = x;
            x = value;
            firePropertyChange("x", old, x);
        }
    }

    // キャンバス座標 Y
    @Bind
    private double y = 0.0;

    /**
     * キャンバスにおけるこのLayerObjectのY座標を返します
     * @return y
     */
    public final double getY() {
        return y;
    }

    public final void setY(double value) {
        if (y != value) {
            double old = y;
            y = value;
            firePropertyChange("y", old, y);
        }
    }

    @Bind
    private double rotate = 0.0;

    public final double getRotate() {
        return rotate;
    }

    public final void setRotate(double value) {
        if (rotate != value) {
            double old = rotate;
            rotate = value;
            firePropertyChange("rotate", old, rotate);
        }
    }

    @Min(0)
    @Bind
    private double scaleX = 1.0;

    public final double getScaleX() {
        return scaleX;
    }

    public final void setScaleX(double value) {
        if (value < 0) throw new IllegalArgumentException();
        if (scaleX != value) {
            double old = scaleX;
            scaleX = value;
            firePropertyChange("scaleX", old, scaleX);
        }
    }

    @Min(0)
    @Bind
    private double scaleY = 1.0;

    public final double getScaleY() {
        return scaleY;
    }

    public final void setScaleY(double value) {
        if (value < 0) throw new IllegalArgumentException();
        if (scaleY != value) {
            double old = scaleY;
            scaleY = value;
            firePropertyChange("scaleY", old, scaleY);
        }
    }

    @Bind
    private LayerObject clip;

    public final void setClip(LayerObject clip) {
        if (this.clip != clip) {
            var old = this.clip;
            this.clip = clip;
            firePropertyChange("clip", old, clip);
        }
    }

    public final LayerObject getClip() {
        return clip;
    }

    private Parent parent;

    final void setParent(Parent parent) {
        if (this.parent != parent) {
            var old = this.parent;
            this.parent = parent;
            firePropertyChange("parent", old, parent);
        }
    }

    public final Parent getParent() {
        return parent;
    }

    /**
     * Parentの位置を原点としてこのLayerObjectのX座標を計算します。Parentが存在しない場合、{@link LayerObject#getX()}と一致します
     * @return Parentの位置を原点としたときのこのLayerObjectのX座標
     */
    public final double getXInParent() {
        if (parent == null) {
            return getX();
        } else {
            return getX() - parent.getX();
        }
    }

    /**
     * Parentの位置を原点としてこのLayerObjectのY座標を計算します。Parentが存在しない場合、{@link LayerObject#getY()}と一致します
     * @return Parentの位置を原点としたときのこのLayerObjectのY座標
     */
    public final double getYInParent() {
        if (parent == null) {
            return getY();
        } else {
            return getY() - parent.getY();
        }
    }

    private Canvas canvas;

    Canvas getCanvas() {
        return canvas;
    }

    final void setCanvas(Canvas canvas) {
        this.canvas = Objects.requireNonNull(canvas);
    }

    protected void init() {
    }

    public final void render(Graphics2D g, Shape aoi, boolean ignoreRough) {
        Rectangle aoiRect = aoi.getBounds();
        var x = getX();
        var y = getY();
        var maxX = aoiRect.x + aoiRect.width;
        var maxY = aoiRect.y + aoiRect.height;
        if (x > maxX || y > maxY) return;

        if (!visible || (ignoreRough & rough) || opacity == 0f) return;
        g.translate(x, y);
        g.rotate(getRotate() * Math.PI * 2 / 360);
        g.scale(scaleX, scaleY);
        g.setClip(aoi);
        g.setComposite(getCompositeFactory().createComposite(opacity));
        renderContent(g, aoi, ignoreRough);
        g.translate(-x, -y);
        g.rotate(-getRotate() * Math.PI * 2 / 360);
        g.scale(1 / scaleX, 1 / scaleY);
    }

    abstract void renderContent(Graphics2D g, Shape aoi, boolean ignoreRough);

    @Override
    public void dispose() {
    }

    public <R> R accept(LayerVisitor<R> visitor) {
        return visitor.visit(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Memento<?> createMemento() {
        return new MyMemento(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void restore(Memento<?> memento) {
        if (memento == null) {
            throw new NullPointerException("memento is null!");
        }
        if (memento instanceof MyMemento m) {
            name = m.name;
            opacity = m.opacity;
            visible = m.visible;
            rough = m.rough;
            compositeFactory = m.compositeFactory;
            x = m.x;
            y = m.y;
            rotate = m.rotate;
            scaleX = m.scaleX;
            scaleY = m.scaleY;
            clip = m.clip;
            parent = m.parent;
            canvas = m.canvas;
        } else {
            throw new IncompatibleMementoException("\"memento\" is not compatible to LayerObject.");
        }
    }

    private static class MyMemento extends MementoBase<LayerObject> {
        final String name;
        final float opacity;
        final boolean visible;
        final boolean rough;
        final CompositeFactory compositeFactory;
        final double x;
        final double y;
        final double rotate;
        final double scaleX;
        final double scaleY;
        final LayerObject clip;
        final Parent parent;
        final Canvas canvas;

        public MyMemento(LayerObject layer) {
            super(layer, null);
            name = layer.name;
            opacity = layer.opacity;
            visible = layer.visible;
            rough = layer.rough;
            compositeFactory = layer.compositeFactory;
            x = layer.x;
            y = layer.y;
            rotate = layer.rotate;
            scaleX = layer.scaleX;
            scaleY = layer.scaleY;
            clip = layer.clip;
            parent = layer.parent;
            canvas = layer.canvas;
        }
    }
}