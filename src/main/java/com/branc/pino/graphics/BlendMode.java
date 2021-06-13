package com.branc.pino.graphics;

import com.branc.pino.annotations.Bind;
import com.branc.pino.annotations.ResourceBundle;

@ResourceBundle(base = "com.branc.pino.graphics.BlendMode")
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
    EXCLUSION,
    @Bind
    SOFT_LIGHT
}
