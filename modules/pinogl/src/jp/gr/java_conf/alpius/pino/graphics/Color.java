package jp.gr.java_conf.alpius.pino.graphics;

import jp.gr.java_conf.alpius.pino.graphics.color.ColorSpace;
import jp.gr.java_conf.alpius.pino.graphics.color.RGBColorSpace;
import jp.gr.java_conf.alpius.pino.graphics.utils.Checks;

import java.util.Arrays;
import java.util.Objects;

public final class Color {

    // Built-in 140 colors
    public static final Color BLACK = sRGB(0x000000);
    public static final Color ALICEBLUE = sRGB(0xf0f8ff);
    public static final Color DARKCYAN = sRGB(0x008b8b);
    public static final Color LIGHTYELLOW = sRGB(0xffffe0);
    public static final Color CORAL = sRGB(0xff7f50);
    public static final Color DIMGRAY = sRGB(0x696969);
    public static final Color LAVENDER = sRGB(0xe6e6fa);
    public static final Color TEAL = sRGB(0x008080);
    public static final Color LIGHTGOLDENRODYELLOW = sRGB(0xfafad2);
    public static final Color TOMATO = sRGB(0xff6347);
    public static final Color GRAY = sRGB(0x808080);
    public static final Color LIGHTSTEELBLUE = sRGB(0xb0c4de);
    public static final Color DARKSLATEGRAY = sRGB(0x2f4f4f);
    public static final Color LEMONCHIFFON = sRGB(0xfffacd);
    public static final Color ORANGERED = sRGB(0xff4500);
    public static final Color DARKGRAY = sRGB(0xa9a9a9);
    public static final Color LIGHTSLATEGRAY = sRGB(0x778899);
    public static final Color DARKGREEN = sRGB(0x006400);
    public static final Color WHEAT = sRGB(0xf5deb3);
    public static final Color RED = sRGB(0xff0000);
    public static final Color SILVER = sRGB(0xc0c0c0);
    public static final Color SLATEGRAY = sRGB(0x708090);
    public static final Color GREEN = sRGB(0x008000);
    public static final Color BURLYWOOD = sRGB(0xdeb887);
    public static final Color CRIMSON = sRGB(0xdc143c);
    public static final Color LIGHTGRAY = sRGB(0xd3d3d3);
    public static final Color STEELBLUE = sRGB(0x4682b4);
    public static final Color FORESTGREEN = sRGB(0x228b22);
    public static final Color TAN = sRGB(0xd2b48c);
    public static final Color MEDIUMVIOLETRED = sRGB(0xc71585);
    public static final Color GAINSBORO = sRGB(0xdcdcdc);
    public static final Color ROYALBLUE = sRGB(0x4169e1);
    public static final Color SEAGREEN = sRGB(0x2e8b57);
    public static final Color KHAKI = sRGB(0xf0e68c);
    public static final Color DEEPPINK = sRGB(0xff1493);
    public static final Color WHITESMOKE = sRGB(0xf5f5f5);
    public static final Color MIDNIGHTBLUE = sRGB(0x191970);
    public static final Color MEDIUMSEAGREEN = sRGB(0x3cb371);
    public static final Color YELLOW = sRGB(0xffff00);
    public static final Color HOTPINK = sRGB(0xff69b4);
    public static final Color WHITE = sRGB(0xffffff);
    public static final Color NAVY = sRGB(0x000080);
    public static final Color MEDIUMAQUAMARINE = sRGB(0x66cdaa);
    public static final Color GOLD = sRGB(0xffd700);
    public static final Color PALEVIOLETRED = sRGB(0xdb7093);
    public static final Color SNOW = sRGB(0xfffafa);
    public static final Color DARKBLUE = sRGB(0x00008b);
    public static final Color DARKSEAGREEN = sRGB(0x8fbc8f);
    public static final Color ORANGE = sRGB(0xffa500);
    public static final Color PINK = sRGB(0xffc0cb);
    public static final Color GHOSTWHITE = sRGB(0xf8f8ff);
    public static final Color MEDIUMBLUE = sRGB(0x0000cd);
    public static final Color AQUAMARINE = sRGB(0x7fffd4);
    public static final Color SANDYBROWN = sRGB(0xf4a460);
    public static final Color LIGHTPINK = sRGB(0xffb6c1);
    public static final Color FLORALWHITE = sRGB(0xfffaf0);
    public static final Color BLUE = sRGB(0x0000ff);
    public static final Color PALEGREEN = sRGB(0x98fb98);
    public static final Color DARKORANGE = sRGB(0xff8c00);
    public static final Color THISTLE = sRGB(0xd8bfd8);
    public static final Color LINEN = sRGB(0xfaf0e6);
    public static final Color DODGERBLUE = sRGB(0x1e90ff);
    public static final Color LIGHTGREEN = sRGB(0x90ee90);
    public static final Color GOLDENROD = sRGB(0xdaa520);
    public static final Color MAGENTA = sRGB(0xff00ff);
    public static final Color ANTIQUEWHITE = sRGB(0xfaebd7);
    public static final Color CORNFLOWERBLUE = sRGB(0x6495ed);
    public static final Color SPRINGGREEN = sRGB(0x00ff7f);
    public static final Color PERU = sRGB(0xcd853f);
    public static final Color FUCHSIA = sRGB(0xff00ff);
    public static final Color PAPAYAWHIP = sRGB(0xffefd5);
    public static final Color DEEPSKYBLUE = sRGB(0x00bfff);
    public static final Color MEDIUMSPRINGGREEN = sRGB(0x00fa9a);
    public static final Color DARKGOLDENROD = sRGB(0xb8860b);
    public static final Color VIOLET = sRGB(0xee82ee);
    public static final Color BLANCHEDALMOND = sRGB(0xffebcd);
    public static final Color LIGHTSKYBLUE = sRGB(0x87cefa);
    public static final Color LAWNGREEN = sRGB(0x7cfc00);
    public static final Color CHOCOLATE = sRGB(0xd2691e);
    public static final Color PLUM = sRGB(0xdda0dd);
    public static final Color BISQUE = sRGB(0xffe4c4);
    public static final Color SKYBLUE = sRGB(0x87ceeb);
    public static final Color CHARTREUSE = sRGB(0x7fff00);
    public static final Color SIENNA = sRGB(0xa0522d);
    public static final Color ORCHID = sRGB(0xda70d6);
    public static final Color MOCCASIN = sRGB(0xffe4b5);
    public static final Color LIGHTBLUE = sRGB(0xadd8e6);
    public static final Color GREENYELLOW = sRGB(0xadff2f);
    public static final Color SADDLEBROWN = sRGB(0x8b4513);
    public static final Color MEDIUMORCHID = sRGB(0xba55d3);
    public static final Color NAVAJOWHITE = sRGB(0xffdead);
    public static final Color POWDERBLUE = sRGB(0xb0e0e6);
    public static final Color LIME = sRGB(0x00ff00);
    public static final Color MAROON = sRGB(0x800000);
    public static final Color DARKORCHID = sRGB(0x9932cc);
    public static final Color PEACHPUFF = sRGB(0xffdab9);
    public static final Color PALETURQUOISE = sRGB(0xafeeee);
    public static final Color LIMEGREEN = sRGB(0x32cd32);
    public static final Color DARKRED = sRGB(0x8b0000);
    public static final Color DARKVIOLET = sRGB(0x9400d3);
    public static final Color MISTYROSE = sRGB(0xffe4e1);
    public static final Color LIGHTCYAN = sRGB(0xe0ffff);
    public static final Color YELLOWGREEN = sRGB(0x9acd32);
    public static final Color BROWN = sRGB(0xa52a2a);
    public static final Color DARKMAGENTA = sRGB(0x8b008b);
    public static final Color LAVENDERBLUSH = sRGB(0xfff0f5);
    public static final Color CYAN = sRGB(0x00ffff);
    public static final Color DARKOLIVEGREEN = sRGB(0x556b2f);
    public static final Color FIREBRICK = sRGB(0xb22222);
    public static final Color PURPLE = sRGB(0x800080);
    public static final Color SEASHELL = sRGB(0xfff5ee);
    public static final Color AQUA = sRGB(0x00ffff);
    public static final Color OLIVEDRAB = sRGB(0x6b8e23);
    public static final Color INDIANRED = sRGB(0xcd5c5c);
    public static final Color INDIGO = sRGB(0x4b0082);
    public static final Color OLDLACE = sRGB(0xfdf5e6);
    public static final Color TURQUOISE = sRGB(0x40e0d0);
    public static final Color OLIVE = sRGB(0x808000);
    public static final Color ROSYBROWN = sRGB(0xbc8f8f);
    public static final Color DARKSLATEBLUE = sRGB(0x483d8b);
    public static final Color IVORY = sRGB(0xfffff0);
    public static final Color MEDIUMTURQUOISE = sRGB(0x48d1cc);
    public static final Color DARKKHAKI = sRGB(0xbdb76b);
    public static final Color DARKSALMON = sRGB(0xe9967a);
    public static final Color BLUEVIOLET = sRGB(0x8a2be2);
    public static final Color HONEYDEW = sRGB(0xf0fff0);
    public static final Color DARKTURQUOISE = sRGB(0x00ced1);
    public static final Color PALEGOLDENROD = sRGB(0xeee8aa);
    public static final Color LIGHTCORAL = sRGB(0xf08080);
    public static final Color MEDIUMPURPLE = sRGB(0x9370db);
    public static final Color MINTCREAM = sRGB(0xf5fffa);
    public static final Color LIGHTSEAGREEN = sRGB(0x20b2aa);
    public static final Color CORNSILK = sRGB(0xfff8dc);
    public static final Color SALMON = sRGB(0xfa8072);
    public static final Color SLATEBLUE = sRGB(0x6a5acd);
    public static final Color AZURE = sRGB(0xf0ffff);
    public static final Color CADETBLUE = sRGB(0x5f9ea0);
    public static final Color BEIGE = sRGB(0xf5f5dc);
    public static final Color LIGHTSALMON = sRGB(0xffa07a);
    public static final Color MEDIUMSLATEBLUE = sRGB(0x7b68ee);

    public static Color sRGB(int color) {
        return sRGB(color, false);
    }

    public static Color sRGB(int color, boolean hasAlpha) {
        int a, r, g, b;
        r = color >> 16 & 0xff;
        g = color >> 8 & 0xff;
        b = color & 0xff;
        if (hasAlpha) {
            a = color >> 24 & 0xff;
        } else {
            a = 0xff;
        }
        float[] components = {
                ((float) r) / 0xff, ((float) g) / 0xff, ((float) b) / 0xff
        };
        float opacity = ((float) a) / 0xff;
        return new Color(RGBColorSpace.CS_sRGB_D65, components, opacity);
    }

    public static Color sRGB(float r, float g, float b) {
        return sRGB(r, g, b, 1f);
    }

    public static Color sRGB(float r, float g, float b, float a) {
        Checks.require(0 <= r && r <= 1, "r < 0 || 1 < r");
        Checks.require(0 <= g && g <= 1, "g < 0 || 1 < g");
        Checks.require(0 <= b && b <= 1, "b < 0 || 1 < b");
        Checks.require(0 <= a && a <= 1, "a < 0 || 1 < a");
        return new Color(RGBColorSpace.CS_sRGB_D65, new float[] { r, g, b }, a);
    }

    private final ColorSpace cs;
    private final float[] components;
    private final float opacity;

    public Color(ColorSpace cs, float[] components, float opacity) {
        this.cs = Objects.requireNonNull(cs, "colorSpace == null");
        this.components = Objects.requireNonNull(components, "components == null");
        this.opacity = opacity;

        Checks.require(cs.getType().numComponents() == components.length, "numComponents != components.length");

        for (float component: components) {
            Checks.require(0 <= component && component <= 1, "component < 0 || 1 < component");
        }
        Checks.require(0 <= opacity && opacity <= 1, "opacity < 0 || 1 < opacity");
    }

    public ColorSpace getColorSpace() {
        return cs;
    }

    public float getOpacity() {
        return opacity;
    }

    public AlphaType getAlphaType() {
        if (opacity == 1f) {
            return AlphaType.OPAQUE;
        } else {
            return AlphaType.UNPREMUL;
        }
    }

    public float[] getColorComponents() {
        return Arrays.copyOf(components, components.length);
    }

    public float[] getColorComponents(ColorSpace colorSpace) {
        if (cs.equals(colorSpace)) {
            return getColorComponents();
        }
        var xyz = cs.toCIEXYZ(getColorComponents());
        return colorSpace.fromCIEXYZ(xyz);
    }

    public void getColorComponents(float[] array) {
        System.arraycopy(components, 0, array, 0, components.length);
    }

    public void getColorComponents(ColorSpace colorSpace, float[] array) {
        if (cs.equals(colorSpace)) {
            getColorComponents(array);
            return;
        }
        var xyz = cs.toCIEXYZ(getColorComponents());
        var res = colorSpace.fromCIEXYZ(xyz);
        System.arraycopy(res, 0, array, 0, res.length);
    }

    public float[] getComponents() {
        var res = new float[components.length + 1];
        System.arraycopy(components, 0, res, 1, components.length);
        res[0] = opacity;
        return res;
    }

    public float[] getComponents(ColorSpace colorSpace) {
        if (cs.equals(colorSpace)) {
            return getComponents();
        }
        var array = getColorComponents(colorSpace);
        var res = new float[array.length + 1];
        System.arraycopy(array, 0, res, 1, array.length);
        res[0] = opacity;
        return res;
    }

    public void getComponents(float[] array) {
        array[0] = opacity;
        System.arraycopy(components, 0, array, 1, components.length);
    }

    public void getComponents(ColorSpace colorSpace, float[] array) {
        if (cs.equals(colorSpace)) {
            getComponents(array);
            return;
        }
        var components = getColorComponents(colorSpace);
        System.arraycopy(components, 0, array, 1, components.length);
        array[0] = opacity;
    }
}
