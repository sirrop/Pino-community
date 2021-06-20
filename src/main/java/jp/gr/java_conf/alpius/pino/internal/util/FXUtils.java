package jp.gr.java_conf.alpius.pino.internal.util;

import jp.gr.java_conf.alpius.pino.application.ApplicationError;
import jp.gr.java_conf.alpius.pino.core.annotaion.Internal;
import jp.gr.java_conf.alpius.pino.graphics.BlendMode;

import java.awt.*;

@Internal
public final class FXUtils {
    private FXUtils() {
        throw new AssertionError();
    }

    public static javafx.scene.effect.BlendMode toFX(BlendMode pblend) {
        switch (pblend) {
            case SRC_OVER: return javafx.scene.effect.BlendMode.SRC_OVER;
            case SRC_ATOP: return javafx.scene.effect.BlendMode.SRC_ATOP;
            case ADD: return javafx.scene.effect.BlendMode.ADD;
            case MULTIPLE: return javafx.scene.effect.BlendMode.MULTIPLY;
            case SCREEN: return javafx.scene.effect.BlendMode.SCREEN;
            case OVERLAY: return javafx.scene.effect.BlendMode.OVERLAY;
            case HARD_LIGHT: return javafx.scene.effect.BlendMode.HARD_LIGHT;
            case DODGE: return javafx.scene.effect.BlendMode.COLOR_DODGE;
            case BURN: return javafx.scene.effect.BlendMode.COLOR_BURN;
            case DARKEN: return javafx.scene.effect.BlendMode.DARKEN;
            case LIGHTEN: return javafx.scene.effect.BlendMode.LIGHTEN;
            case DIFFERENCE: return javafx.scene.effect.BlendMode.DIFFERENCE;
            case EXCLUSION: return javafx.scene.effect.BlendMode.EXCLUSION;
            case SOFT_LIGHT: return javafx.scene.effect.BlendMode.SOFT_LIGHT;
        }
        throw new ApplicationError();
    }

    public static javafx.scene.paint.Color toFX(Color acolor) {
        return new javafx.scene.paint.Color(
                ((double) acolor.getRed()) / 255,
                ((double) acolor.getGreen()) / 255,
                ((double) acolor.getBlue()) / 255,
                ((double) acolor.getAlpha()) / 255
        );
    }
}
