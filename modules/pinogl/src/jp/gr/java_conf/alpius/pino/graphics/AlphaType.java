package jp.gr.java_conf.alpius.pino.graphics;

/**
 * アルファチャネルの種類に関するヒントを表します
 */
public enum AlphaType {
    /**
     * 全てのピクセルが不透明であることを表します。
     * ピクセルがアルファチャネルを持つかどうかは実装に依存します。
     */
    OPAQUE,

    /**
     * 全てのピクセルがアルファチャネルを持ち、事前に乗算されていないことを表します
     */
    UNPREMUL,

    /**
     * 全てのピクセルがアルファチャネルを持ち、事前に乗算されていることを表します。
     */
    PREMUL
}
