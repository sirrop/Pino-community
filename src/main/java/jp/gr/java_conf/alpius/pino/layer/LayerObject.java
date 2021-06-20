package jp.gr.java_conf.alpius.pino.layer;

import jp.gr.java_conf.alpius.pino.annotations.*;
import jp.gr.java_conf.alpius.pino.core.util.Disposable;
import jp.gr.java_conf.alpius.pino.core.util.Strings;
import jp.gr.java_conf.alpius.pino.graphics.BlendMode;
import jp.gr.java_conf.alpius.pino.internal.layer.LayerHelper;
import jp.gr.java_conf.alpius.pino.internal.layer.LayerPeer;
import jp.gr.java_conf.alpius.pino.ui.editor.EditorTarget;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.util.List;
import java.util.Objects;

@ResourceBundle(base = "jp.gr.java_conf.alpius.pino.layer.LayerObject")
public abstract class LayerObject implements Disposable, EditorTarget {
    private LayerHelper helper = null;

    static {
        LayerHelper.setAccessor(new LayerHelper.LayerAccessor() {
            @Override
            public LayerHelper getHelper(LayerObject layer) {
                return layer.helper;
            }

            @Override
            public void setHelper(LayerObject layer, LayerHelper helper) {
                layer.helper = helper;
            }

            @Override
            public <P extends LayerPeer> P getPeer(LayerObject layer) {
                return layer.getPeer();
            }
        });
    }

    // -- Internal APIs --
    private LayerPeer peer;

    @SuppressWarnings("unchecked")
    <P extends LayerPeer> P getPeer() {
        if (peer == null) {
            peer = LayerHelper.newPeer(this);
        }
        return (P) peer;
    }


    /* Public APIs */

    @Override
    public List<PropertyDescriptor> getUnmodifiablePropertyList() {
        return getPeer().getUnmodifiableProperties();
    }

    @Override
    public final void addListener(PropertyChangeListener listener) {
        getPeer().addListener(listener);
    }

    @Override
    public final void removeListener(PropertyChangeListener listener) {
        getPeer().removeListener(listener);
    }

    protected final <T> void firePropertyChange(String name, T oldValue, T newValue) {
        getPeer().firePropertyChange(name, oldValue, newValue);
    }


    /* -- Common properties -- */

    private LayerState state = LayerState.STABLE;

    public LayerState getState() {
        return state;
    }


    @NotNull
    @Bind
    private String name = getClass().getSimpleName();

    @NotNull
    public final String getName() {
        return name;
    }

    public final void setName(@Nullable String value) {
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


    @Range(min = 0, max = 100)
    @Bind
    private float opacity = 100f;

    public final void setOpacity(float value) {
        if (value < 0 || 100 < value) throw new IllegalArgumentException();
        float old = opacity;
        if (old != value) {
            opacity = value;
            getPeer().setOpacity(value);
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
            getPeer().setVisible(value);
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
            getPeer().setRough(value);
            firePropertyChange("rough", old, rough);
        }

    }

    public final boolean isRough() {
        return rough;
    }


    @Bind
    @NotNull
    private BlendMode blendMode = BlendMode.SRC_OVER;

    @NotNull
    public BlendMode getBlendMode() {
        return blendMode;
    }

    public void setBlendMode(@NotNull BlendMode blendMode) {
        if (this.blendMode != blendMode) {
            BlendMode old = this.blendMode;
            this.blendMode = Objects.requireNonNull(blendMode);
            getPeer().setBlendMode(blendMode);
            firePropertyChange("blendMoe", old, blendMode);
        }
    }


    @ForEx
    private double x = 0.0;

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


    @ForEx
    private double y = 0.0;

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


    @ForEx
    private double z = 0.0;

    public final double getZ() {
        return z;
    }

    public final void setZ(double value) {
        if (z != value) {
            double old = z;
            z = value;
            firePropertyChange("z", old, z);
        }

    }


    @ForEx
    private double rotateX = 0.0;

    public final double getRotateX() {
        return rotateX;
    }

    public final void setRotateX(double value) {
        if (rotateX != value) {
            double old = rotateX;
            rotateX = value;
            firePropertyChange("rotateX", old, rotateX);
        }
    }


    @ForEx
    private double rotateY = 0.0;

    public final double getRotateY() {
        return rotateY;
    }

    public final void setRotateY(double value) {
        if (rotateY != value) {
            double old = rotateY;
            rotateY = value;
            firePropertyChange("rotateY", old, rotateY);
        }
    }


    @ForEx
    private double rotateZ = 0.0;

    public final double getRotateZ() {
        return rotateZ;
    }

    public final void setRotateZ(double value) {
        if (rotateZ != value) {
            double old = rotateZ;
            rotateZ = value;
            firePropertyChange("rotateZ", old, rotateZ);
        }
    }


    @ForEx
    @Min(0)
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


    @ForEx
    @Min(0)
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


    @ForEx
    @Min(0)
    private double scaleZ = 1.0;

    public final double getScaleZ() {
        return scaleZ;
    }

    public final void setScaleZ(double value) {
        if (value < 0) throw new IllegalArgumentException();
        if (scaleZ != value) {
            double old = scaleZ;
            scaleZ = value;
            firePropertyChange("scaleZ", old, scaleZ);
        }
    }

    @Override
    public void dispose() {
    }
}
