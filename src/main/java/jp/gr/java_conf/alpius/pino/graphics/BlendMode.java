package jp.gr.java_conf.alpius.pino.graphics;

import jp.gr.java_conf.alpius.pino.annotations.Bind;
import jp.gr.java_conf.alpius.pino.annotations.ResourceBundle;

@ResourceBundle(base = "jp.gr.java_conf.alpius.pino.graphics.BlendMode")
public enum BlendMode {
    @Bind
    SRC_OVER,
    SRC_ATOP,
    @Bind
    ADD,
    @Bind
    MULTIPLE,
    @Bind
    SCREEN,
    @Bind
    OVERLAY,
    @Bind
    HARD_LIGHT,
    @Bind
    SOFT_LIGHT,
    @Bind
    DODGE,
    @Bind
    BURN,
    @Bind
    DARKEN,
    @Bind
    LIGHTEN,
    @Bind
    DIFFERENCE,
    @Bind
    EXCLUSION
}
