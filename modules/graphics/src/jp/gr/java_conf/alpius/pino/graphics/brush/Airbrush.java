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

package jp.gr.java_conf.alpius.pino.graphics.brush;

import jp.gr.java_conf.alpius.pino.beans.Bind;
import jp.gr.java_conf.alpius.pino.beans.Min;
import jp.gr.java_conf.alpius.pino.beans.Range;
import jp.gr.java_conf.alpius.pino.graphics.AlphaBlend;
import jp.gr.java_conf.alpius.pino.graphics.CompositeFactory;
import jp.gr.java_conf.alpius.pino.graphics.brush.context.AirbrushContext;
import jp.gr.java_conf.alpius.pino.graphics.layer.DrawableLayer;

import java.awt.*;

public class Airbrush extends AbstractBrush {
    @Bind
    @Min(0)
    private float width = 30;

    public void setWidth(float value) {
        if (value < 0) {
            throw new IllegalArgumentException();
        }
        if (width != value) {
            var old = width;
            width = value;
            firePropertyChange("width", old, width);
        }
    }

    public float getWidth() {
        return width;
    }

    @Bind
    @Range(min = 0, max = 1)
    private float opacity = 0.4f;

    public void setOpacity(float value) {
        if (value < 0 || 1 < value) {
            throw new IllegalArgumentException();
        }
        if (opacity != value) {
            var old = opacity;
            opacity = value;
            firePropertyChange("opacity", old, opacity);
        }
    }

    public float getOpacity() {
        return opacity;
    }

    @Bind
    private Color color = Color.BLACK;

    public void setColor(Color value) {
        if (value == null) {
            throw new NullPointerException();
        }
        if (color != value) {
            var old = color;
            color = value;
            firePropertyChange("color", old, color);
        }
    }

    public Color getColor() {
        return color;
    }



    @Bind
    private CompositeFactory blendMode = AlphaBlend.Mode.SRC_OVER;

    public void setBlendMode(CompositeFactory value) {
        if (value == null) {
            throw new NullPointerException();
        }
        if (blendMode != value) {
            var old = blendMode;
            blendMode = value;
            firePropertyChange("blendMode", old, blendMode);
        }
    }

    public CompositeFactory getBlendMode() {
        return blendMode;
    }

    @Bind
    private boolean antialiasing = true;

    public void setAntialiasing(boolean value) {
        if (antialiasing != value) {
            var old = antialiasing;
            antialiasing = value;
            firePropertyChange("antialiasing", old, antialiasing);
        }
    }

    public boolean isAntialiasing() {
        return antialiasing;
    }

    @Override
    public BrushContext createContext(DrawableLayer target) {
        return new AirbrushContext(this, target);
    }
}
