package jp.gr.java_conf.alpius.pino.graphics;

import jp.gr.java_conf.alpius.pino.graphics.color.RGBColorSpace;

import java.util.Arrays;

final class CoefficientBasedComposer implements Composer {
    private final Composite.Coefficient srcCoeff;
    private final Composite.Coefficient dstCoeff;
    private final float opacity;

    public CoefficientBasedComposer(Composite.Coefficient srcCoeff, Composite.Coefficient dstCoeff, float opacity) {
        this.srcCoeff = srcCoeff;
        this.dstCoeff = dstCoeff;
        this.opacity = opacity;
    }

    private interface ColorComposer {
        void compose(float[] src, float[] dst, float[] result);
    }

    @Override
    public void compose(Image src, Image dstIn, Image dstOut) {
        int w = Math.min(src.getWidth(), dstIn.getWidth());
        int h = Math.min(src.getHeight(), dstIn.getHeight());
        var composer = get(srcCoeff, dstCoeff);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                Color srcColor = src.getColor(x, y);
                Color dstColor = dstIn.getColor(x, y);
                float[] srcRgb = srcColor.getComponents(RGBColorSpace.CS_sRGB_D65);
                float[] dstRgb = dstColor.getComponents(RGBColorSpace.CS_sRGB_D65);
                float[] result = new float[RGBColorSpace.CS_sRGB_D65.getNumComponents() + 1];
                applyOpacity(srcRgb, opacity);
                composer.compose(srcRgb, dstRgb, result);
                dstOut.setColor(x, y, new Color(RGBColorSpace.CS_sRGB_D65, new float[] { result[1], result[2], result[3] }, result[0]));
            }
        }
    }

    private static ColorComposer get(Composite.Coefficient src, Composite.Coefficient dst) {
        return new SimpleColorComposer(src, dst);
    }

    private static void applyOpacity(float[] components, float opacity) {
        if (opacity != 1) {
            components[0] *= opacity;
        }
    }

    @Override
    public void dispose() {

    }

    private static class SimpleColorComposer implements ColorComposer {
        private final Composite.Coefficient src;
        private final Composite.Coefficient dst;

        public SimpleColorComposer(Composite.Coefficient src, Composite.Coefficient dst) {
            this.src = src;
            this.dst = dst;
        }

        @Override
        public void compose(float[] src, float[] dst, float[] result) {
            float a1 = src[0] * dst[0];
            float a2 = src[0] * (1 - dst[0]);
            float a3 = (1 - src[0]) * dst[0];
            float a = a1 + a2 + a3;
            if (a == 0) {
                Arrays.fill(result, 0);
                return;
            }

            result[0] = a;
            result[1] = compute(a1, a2, a3, a, src, dst, 1);
            result[2] = compute(a1, a2, a3, a, src, dst, 2);
            result[3] = compute(a1, a2, a3, a, src, dst, 3);
        }

        private float compute(float a1, float a2, float a3, float a, float[] src, float[] dst, int idx) {
            return (a1 * (Math.min(getCoefficient(this.src, src, dst, idx) * src[idx] + getCoefficient(this.dst, src, dst, idx) * dst[idx], 1f))
                    + a2 * src[idx]
                    + a3 * dst[idx]) / a;
        }

        private static float getCoefficient(Composite.Coefficient coeff, float[] srcComp, float[] dstComp, int idx) {
            return switch (coeff) {
                case ZERO -> 0.0F;
                case ONE -> 1F;
                case SC -> srcComp[idx];
                case ISC -> 1 - srcComp[idx];
                case DC -> dstComp[idx];
                case IDC -> 1 - dstComp[idx];
                case SA -> srcComp[0];
                case ISA -> 1 - srcComp[0];
                case DA -> dstComp[0];
                case IDA -> 1 - dstComp[0];
            };
        }
    }
}
