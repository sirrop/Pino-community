/*
 * Copyright [2021] [shiro]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.gr.java_conf.alpius.pino.graphics;

import jp.gr.java_conf.alpius.pino.graphics.internal.graphics.SimpleAlphaBlendContext;

import java.awt.*;
import java.awt.image.ColorModel;
import java.util.HashMap;
import java.util.Map;

public final class AlphaBlend implements Composite {
    public enum Mode implements CompositeFactory {
        // Porter-Duff blend modes
        SRC,
        DST,
        SRC_IN,
        DST_IN,
        SRC_ATOP,
        DST_ATOP,
        SRC_OVER,
        DST_OVER,
        SRC_OUT,
        DST_OUT,
        XOR,
        CLEAR,
        PLUS,

        MULTIPLY,
        SCREEN,
        OVERLAY,
        DARKEN,
        LIGHTEN,
        COLOR_DODGE,
        COLOR_BURN,
        HARD_LIGHT,
        SOFT_LIGHT,
        DIFFERENCE,
        EXCLUSION;

        public Composite createComposite(float opacity) {
            return getInstance(this, opacity);
        }
    }

    private static final Map<Mode, AlphaBlend> cache = new HashMap<>();

    private final Mode mode;
    private final float opacity;

    private AlphaBlend(Mode mode, float opacity) {
        this.mode = mode;
        this.opacity = opacity;
    }

    public static AlphaBlend getInstance(Mode mode, float opacity) {
        if (mode == null) {
            throw new IllegalArgumentException("mode is null!");
        }
        if (opacity < 0 || 1 < opacity) {
            throw new IllegalArgumentException(String.format("opacity < 0f || 1f < opacity  [value: %f]", opacity));
        }

        if (opacity == 1f) {
            return getInstance(mode);
        } else {
            return new AlphaBlend(mode, opacity);
        }
    }

    public static AlphaBlend getInstance(Mode mode) {
        if (mode == null) {
            throw new NullPointerException("mode is null!");
        }
        return cache.computeIfAbsent(mode, m -> new AlphaBlend(m, 1f));
    }

    public Mode getBlendMode() {
        return mode;
    }

    public float getOpacity() {
        return opacity;
    }

    private boolean isAlphaCompositeCompatible() {
        return mode == Mode.SRC ||
                mode == Mode.DST ||
                mode == Mode.SRC_OVER ||
                mode == Mode.DST_OVER ||
                mode == Mode.SRC_IN ||
                mode == Mode.DST_IN ||
                mode == Mode.SRC_OUT ||
                mode == Mode.DST_OUT ||
                mode == Mode.SRC_ATOP ||
                mode == Mode.DST_ATOP ||
                mode == Mode.XOR ||
                mode == Mode.CLEAR;
    }

    private AlphaComposite getAlphaComposite() {
        switch (mode) {
            case SRC:
                return AlphaComposite.getInstance(AlphaComposite.SRC, opacity);
            case DST:
                return AlphaComposite.getInstance(AlphaComposite.DST, opacity);
            case SRC_OVER:
                return AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity);
            case DST_OVER:
                return AlphaComposite.getInstance(AlphaComposite.DST_OVER, opacity);
            case SRC_IN:
                return AlphaComposite.getInstance(AlphaComposite.SRC_IN, opacity);
            case DST_IN:
                return AlphaComposite.getInstance(AlphaComposite.DST_IN, opacity);
            case SRC_OUT:
                return AlphaComposite.getInstance(AlphaComposite.SRC_OUT, opacity);
            case DST_OUT:
                return AlphaComposite.getInstance(AlphaComposite.DST_OUT, opacity);
            case SRC_ATOP:
                return AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, opacity);
            case DST_ATOP:
                return AlphaComposite.getInstance(AlphaComposite.DST_ATOP, opacity);
            case XOR:
                return AlphaComposite.getInstance(AlphaComposite.XOR, opacity);
            case CLEAR:
                return AlphaComposite.getInstance(AlphaComposite.CLEAR, opacity);
        }
        throw new IllegalStateException();
    }

    public CompositeContext createContext(ColorModel srcCM, ColorModel dstCM, RenderingHints hints) {
        if (isAlphaCompositeCompatible()) {
            return getAlphaComposite().createContext(srcCM, dstCM, hints);
        }
        return new SimpleAlphaBlendContext(this, srcCM, dstCM);
    }
}
