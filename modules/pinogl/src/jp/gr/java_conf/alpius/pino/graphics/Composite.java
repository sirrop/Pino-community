package jp.gr.java_conf.alpius.pino.graphics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public abstract class Composite {
    public enum Coefficient {
        ZERO,
        ONE,
        SC,
        ISC,
        DC,
        IDC,
        SA,
        ISA,
        DA,
        IDA
    }

    private static final Collection<Composite> cache = new ArrayList<>();

    public static boolean hasCoefficient(Composite composite) {
        return composite.getClass() == CoefficientBasedComposite.class;
    }

    public static Coefficient getSrcCoefficient(Composite composite) {
        Objects.requireNonNull(composite, "composite == null");
        if (composite instanceof CoefficientBasedComposite coef) {
            return coef.getSrcCoeff();
        }
        throw new IllegalArgumentException("composite is not coefficient based.");
    }

    public static Coefficient getDstCoefficient(Composite composite) {
        Objects.requireNonNull(composite, "composite == null");
        if (composite instanceof CoefficientBasedComposite coef) {
            return coef.getDstCoeff();
        }
        throw new IllegalArgumentException("composite is not coefficient based.");
    }

    public static Coefficient getBlendMode(Composite composite) {
        throw new IllegalArgumentException("composite doesn't have BlendMode.");
    }

    public static Composite getInstance(Coefficient src, Coefficient dst) {
        Objects.requireNonNull(src, "src == null");
        Objects.requireNonNull(dst, "dst == null");

        for (var composite: cache) {
            if (composite instanceof CoefficientBasedComposite cbc) {
                if (cbc.getSrcCoeff() == src && cbc.getDstCoeff() == dst) {
                    return cbc;
                }
            }
        }

        Composite res;
        cache.add(res = new CoefficientBasedComposite(src, dst));
        return res;
    }

    public static Composite getInstance(BlendMode mode) {
        Composite res = switch (mode) {
            case CLEAR: yield getInstance(Coefficient.ZERO, Coefficient.ZERO);
            case SRC: yield getInstance(Coefficient.ONE, Coefficient.ZERO);
            case DST: yield getInstance(Coefficient.ZERO, Coefficient.ONE);
            case SRC_OVER: yield getInstance(Coefficient.ONE, Coefficient.ISA);
            case DST_OVER: yield getInstance(Coefficient.IDA, Coefficient.ONE);
            case SRC_IN: yield getInstance(Coefficient.DA, Coefficient.ZERO);
            case DST_IN: yield getInstance(Coefficient.ZERO, Coefficient.SA);
            case SRC_OUT: yield getInstance(Coefficient.IDA, Coefficient.ZERO);
            case DST_OUT: yield getInstance(Coefficient.ZERO, Coefficient.ISA);
            case SRC_ATOP: yield getInstance(Coefficient.DA, Coefficient.ISA);
            case DST_ATOP: yield getInstance(Coefficient.IDA, Coefficient.SA);
            case XOR: yield getInstance(Coefficient.IDA, Coefficient.ISA);
            case MODULATE: yield getInstance(Coefficient.DC, Coefficient.ZERO);
            case SCREEN: yield getInstance(Coefficient.IDC, Coefficient.ONE);
            case ADD: yield getInstance(Coefficient.ONE, Coefficient.ONE);
            default: yield null;
        };

        if (res != null) {
            return res;
        }

        throw new UnsupportedOperationException("Not supported yet.");
    }

    public abstract Composer createComposer(float opacity);
}
