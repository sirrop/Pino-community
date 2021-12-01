package jp.gr.java_conf.alpius.pino.graphics.color;

public abstract class ColorSpace {
    public static final Type TYPE_2CLR = typeOf("Generic 2 component color spaces.", 2);
    public static final Type TYPE_3CLR = typeOf("Generic 3 component color spaces.", 3);
    public static final Type TYPE_4CLR = typeOf("Generic 4 component color spaces.", 4);
    public static final Type TYPE_5CLR = typeOf("Generic 5 component color spaces.", 5);
    public static final Type TYPE_6CLR = typeOf("Generic 6 component color spaces.", 6);
    public static final Type TYPE_7CLR = typeOf("Generic 7 component color spaces.", 7);
    public static final Type TYPE_8CLR = typeOf("Generic 8 component color spaces.", 8);
    public static final Type TYPE_9CLR = typeOf("Generic 9 component color spaces.", 9);
    public static final Type TYPE_ACLR = typeOf("Generic 10 component color spaces.", 10);
    public static final Type TYPE_BCLR = typeOf("Generic 11 component color spaces.", 11);
    public static final Type TYPE_CCLR = typeOf("Generic 12 component color spaces.", 12);
    public static final Type TYPE_DCLR = typeOf("Generic 13 component color spaces.", 13);
    public static final Type TYPE_ECLR = typeOf("Generic 14 component color spaces.", 14);
    public static final Type TYPE_FCLR = typeOf("Generic 15 component color spaces.", 15);

    public static final Type TYPE_GRAY = typeOf("Any of the family of GRAY color spaces.", 1);
    public static final Type TYPE_CMY = typeOf("Any of the family of CMY color spaces.", 3);
    public static final Type TYPE_CMYK = typeOf("Any of the family of CMYK color spaces.", 4);
    public static final Type TYPE_HLS = typeOf("Any of the family of HLS color spaces.", 3);
    public static final Type TYPE_HSV = typeOf("Any of the family of HSV color spaces.", 3);
    public static final Type TYPE_Lab = typeOf("Any of the family of Lab color spaces.", 3);
    public static final Type TYPE_Luv = typeOf("Any of the family of Luv color spaces.", 3);
    public static final Type TYPE_RGB = typeOf("Any of the family of RGB color spaces.", 3);
    public static final Type TYPE_XYZ = typeOf("Any of the family of XYZ color spaces.", 3);
    public static final Type TYPE_YCbCr = typeOf("Any of the family of YCbCr color spaces.", 3);
    public static final Type TYPE_Yxy = typeOf("Any of the family of Yxy color spaces.", 3);

    private final Type type;

    protected ColorSpace(Type type) {
        this.type = type;
    }

    public final Type getType() {
        return type;
    }

    public abstract float[] fromCIEXYZ(float[] colorValue);
    public abstract float[] fromRGB(float[] rgbValue);
    public abstract float[] toCIEXYZ(float[] colorValue);
    public abstract float[] toRGB(float[] colorValue);

    public abstract String getName(int idx);

    public float getMaxValue(int component) {
        return 1;
    }

    public float getMinValue(int component) {
        return 0;
    }

    public int getNumComponents() {
        return type.numComponents();
    }

    private static Type typeOf(String desc, int numComponents) {
        return new Type(desc, numComponents);
    }

    public static record Type(String desc, int numComponents) {
    }
}
