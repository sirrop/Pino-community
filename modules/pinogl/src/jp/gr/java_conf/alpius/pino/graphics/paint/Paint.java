package jp.gr.java_conf.alpius.pino.graphics.paint;

import jp.gr.java_conf.alpius.pino.graphics.Composite;
import jp.gr.java_conf.alpius.pino.graphics.utils.Checks;

import java.util.Objects;

// 描画方法に関する情報をカプセル化します
// PaintはImmutableです。Paintの作成、変更を容易にするBuilderが用意されています
public abstract class Paint {
    Paint(boolean antialias, Composite composite, float opacity) {
        Objects.requireNonNull(composite, "composite == null");
        Checks.require(0 <= opacity && opacity <= 1, "opacity < 0 || 1 < opacity");
        this.antialias = antialias;
        this.composite = composite;
        this.opacity = opacity;
    }

    private final boolean antialias;
    private final Composite composite;
    private final float opacity;

    public final boolean isAntialias() {
        return antialias;
    }

    public final Composite getComposite() {
        return composite;
    }

    public final float getOpacity() {
        return opacity;
    }

    public abstract PaintBuilder toBuilder();
}
