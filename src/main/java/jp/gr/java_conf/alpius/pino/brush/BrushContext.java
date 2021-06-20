package jp.gr.java_conf.alpius.pino.brush;

import jp.gr.java_conf.alpius.pino.annotations.Bind;
import jp.gr.java_conf.alpius.pino.annotations.Min;
import jp.gr.java_conf.alpius.pino.annotations.Range;
import jp.gr.java_conf.alpius.pino.annotations.ResourceBundle;
import jp.gr.java_conf.alpius.pino.core.util.Disposable;
import jp.gr.java_conf.alpius.pino.graphics.BlendMode;
import jp.gr.java_conf.alpius.pino.internal.beans.BeenPeer;
import jp.gr.java_conf.alpius.pino.ui.editor.EditorTarget;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.util.List;

@ResourceBundle(base = "jp.gr.java_conf.alpius.pino.brush.BrushContext")
public abstract class BrushContext implements Disposable, EditorTarget {
    private BeenPeer<BrushContext> peer;

    @NotNull
    BeenPeer<BrushContext> getPeer() {
        if (peer == null) {
            peer = new BeenPeer<>(this);
        }
        return peer;
    }

    @Override
    public final List<PropertyDescriptor> getUnmodifiablePropertyList() {
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

    @Bind
    private String name = getClass().getSimpleName();

    public final void setName(String name) {
        this.name = name;
    }

    public final String getName() {
        return name;
    }


    @Bind
    @Min(0)
    private float width = 5;

    public final void setWidth(float value) {
        if (width < 0) throw new IllegalArgumentException();
        width = value;
    }

    public final float getWidth() {
        return width;
    }


    @Bind
    @Range(min = 0d, max = 100d)
    private float opacity = 100f;

    public final void setOpacity(float value) {
        if (value < 0f || 100f < value) throw new IllegalArgumentException();
        opacity = value;
    }

    public final float getOpacity() {
        return opacity;
    }

    @Bind
    private BlendMode blendMode = BlendMode.SRC_OVER;

    public final void setBlendMode(@NotNull BlendMode blendMode) {
        this.blendMode = blendMode;
    }

    @NotNull
    public final BlendMode getBlendMode() {
        return this.blendMode;
    }


    @Bind
    private Color color = Color.BLACK;

    public final void setColor(@NotNull Color value) {
        color = value;
    }

    @NotNull
    public final Color getColor() {
        return color;
    }

    public abstract Brush<? extends BrushContext> createBrush();

    @Override
    public void dispose() {
    }
}
