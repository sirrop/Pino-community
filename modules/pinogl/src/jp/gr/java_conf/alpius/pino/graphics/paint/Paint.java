package jp.gr.java_conf.alpius.pino.graphics.paint;

import jp.gr.java_conf.alpius.pino.graphics.BlendMode;
import jp.gr.java_conf.alpius.pino.graphics.Composite;
import jp.gr.java_conf.alpius.pino.graphics.utils.Checks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

// 描画方法に関する情報をカプセル化します
public abstract class Paint {
    Paint() {
    }

    private boolean antialias = false;
    private Composite composite = Composite.getInstance(BlendMode.SRC_OVER);
    private float opacity = 1f;
    private final List<Stroke> strokes = new ArrayList<>();

    public final boolean isAntialias() {
        return antialias;
    }

    public Paint setAntialias(boolean value) {
        this.antialias = value;
        return this;
    }

    public final Composite getComposite() {
        return composite;
    }

    public Paint setComposite(Composite composite) {
        this.composite = Objects.requireNonNull(composite, "composite == null");
        return this;
    }

    public final float getOpacity() {
        return opacity;
    }

    public Paint setOpacity(float opacity) {
        Checks.require(0 <= opacity && opacity <= 1, "opacity < 0 || 1 < opacity");
        this.opacity = opacity;
        return this;
    }

    public Paint addStroke(Stroke stroke) {
        if (stroke != null) {
            strokes.add(stroke);
        }
        return this;
    }

    public Paint addStrokes(Collection<? extends Stroke> c) {
        if (c != null) {
            for (var s: c) {
                if (s != null) {
                    strokes.add(s);
                }
            }
        }
        return this;
    }

    public final List<? extends Stroke> getStrokes() {
        return List.copyOf(strokes);
    }

    public Paint removeStroke(Stroke stroke) {
        strokes.remove(stroke);
        return this;
    }

    public Paint removeStrokes(Collection<? extends Stroke> c) {
        if (c != null) {
            strokes.removeAll(c);
        }
        return this;
    }

    public Paint clearStrokes() {
        strokes.clear();
        return this;
    }
}
