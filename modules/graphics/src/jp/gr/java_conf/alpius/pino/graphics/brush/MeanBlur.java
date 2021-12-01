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
import jp.gr.java_conf.alpius.pino.graphics.brush.context.MeanBlurContext;
import jp.gr.java_conf.alpius.pino.graphics.layer.DrawableLayer;

public class MeanBlur extends AbstractBrush {
    @Bind
    @Min(0)
    private float width = 5;

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
    @Min(0)
    private int kernelWidth = 3;

    public final void setKernelWidth(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("kernelWidth < 0");
        }
        if (value != kernelWidth) {
            var old = kernelWidth;
            kernelWidth = value;
            firePropertyChange("kernelWidth", old, value);
        }
    }

    public final int getKernelWidth() {
        return kernelWidth;
    }

    @Bind
    @Min(0)
    private int kernelHeight = 3;

    public final void setKernelHeight(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("kernelHeight < 0");
        }
        if (value != kernelHeight) {
            var old = kernelHeight;
            kernelHeight = value;
            firePropertyChange("kernelHeight", old, value);
        }
    }

    public final int getKernelHeight() {
        return kernelHeight;
    }

    public BrushContext createContext(DrawableLayer target) {
        return new MeanBlurContext(this, target);
    }
}
