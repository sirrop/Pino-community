package jp.gr.java_conf.alpius.pino.graphics;

/**
 * 標準で用意されている合成モードを表します。
 * {@link Composite#getInstance(BlendMode)}を使用してCompositeを取得できます。
 */
public enum BlendMode {
    CLEAR,
    SRC,
    DST,
    SRC_OVER,
    DST_OVER,
    SRC_IN,
    DST_IN,
    SRC_OUT,
    DST_OUT,
    SRC_ATOP,
    DST_ATOP,
    XOR,

    ADD,
    MODULATE,
    SCREEN,
    OVERLAY,
    DARKEN,
    LIGHTEN,
    COLOR_DODGE,
    COLOR_BURN,
    HARD_LIGHT,
    SOFT_LIGHT,
    DIFFERENCE,
    EXCLUSION,
    LINEAR_BURN,
    VIVID_LIGHT,
    LINEAR_LIGHT,
    PIN_LIGHT,
    DIVIDE
}
