package jp.gr.java_conf.alpius.pino.graphics;

import java.util.Objects;

final class CoefficientBasedComposite extends Composite {
    private final Coefficient src;
    private final Coefficient dst;

    CoefficientBasedComposite(Coefficient src, Coefficient dst) {
        this.src = Objects.requireNonNull(src, "src == null");
        this.dst = Objects.requireNonNull(dst, "dst == null");
    }

    public Coefficient getSrcCoeff() {
        return src;
    }

    public Coefficient getDstCoeff() {
        return dst;
    }

    @Override
    public Composer createComposer(float opacity) {
        return new CoefficientBasedComposer(src, dst, opacity);
    }
}
