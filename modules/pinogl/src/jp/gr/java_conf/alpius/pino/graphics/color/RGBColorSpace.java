package jp.gr.java_conf.alpius.pino.graphics.color;

import java.util.Arrays;

public final class RGBColorSpace extends ColorSpace {
    public static final RGBColorSpace CS_sRGB_D65 = new RGBColorSpace(sRGB_D65.RGB2XYZ, sRGB_D65.XYZ2RGB);

    private interface sRGB_D65 {
        float[] RGB2XYZ = {
                0.4124f, 0.3576f, 0.1825f,
                0.2126f, 0.7152f, 0.0722f,
                0.0193f, 0.1192f, 0.9505f
        };
        float[] XYZ2RGB = {
                3.2410f, -1.5374f, -0.4986f,
                -0.9692f, 1.8760f, 0.0416f,
                0.0556f, -0.2040f, 1.0507f
        };
    }

    private final float[] rgbToXyz;
    private final float[] xyzToRgb;

    public RGBColorSpace(float[] rgbToXyz, float[] xyzToRgb) {
        super(ColorSpace.TYPE_RGB);
        this.rgbToXyz = rgbToXyz;
        this.xyzToRgb = xyzToRgb;
    }

    @Override
    public float[] fromCIEXYZ(float[] colorValue) {
        return new float[] {
                xyzToRgb[0] * colorValue[0] + xyzToRgb[1] * colorValue[1] + xyzToRgb[2] * colorValue[2],
                xyzToRgb[3] * colorValue[0] + xyzToRgb[4] * colorValue[1] + xyzToRgb[5] * colorValue[2],
                xyzToRgb[6] * colorValue[0] + xyzToRgb[7] * colorValue[1] + xyzToRgb[8] * colorValue[2]
        };
    }

    @Override
    public float[] fromRGB(float[] rgbValue) {
        return Arrays.copyOf(rgbValue, rgbValue.length);
    }

    @Override
    public float[] toCIEXYZ(float[] colorValue) {
        return new float[] {
                rgbToXyz[0] * colorValue[0] + rgbToXyz[1] * colorValue[1] + rgbToXyz[2] * colorValue[2],
                rgbToXyz[3] * colorValue[0] + rgbToXyz[4] * colorValue[1] + rgbToXyz[5] * colorValue[2],
                rgbToXyz[6] * colorValue[0] + rgbToXyz[7] * colorValue[1] + rgbToXyz[8] * colorValue[2]
        };
    }

    @Override
    public float[] toRGB(float[] colorValue) {
        return Arrays.copyOf(colorValue, colorValue.length);
    }

    @Override
    public String getName(int idx) {
        return null;
    }
}
