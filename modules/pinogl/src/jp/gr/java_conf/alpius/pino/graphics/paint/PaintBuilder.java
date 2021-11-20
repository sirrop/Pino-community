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

package jp.gr.java_conf.alpius.pino.graphics.paint;

import jp.gr.java_conf.alpius.pino.graphics.BlendMode;
import jp.gr.java_conf.alpius.pino.graphics.Composite;
import jp.gr.java_conf.alpius.pino.graphics.utils.Checks;

import java.util.Objects;

// PaintのBuilderです
public abstract class PaintBuilder {
    private boolean antialias = true;
    private Composite composite = Composite.getInstance(BlendMode.SRC_OVER);
    private float opacity = 1f;

    public PaintBuilder setAntialias(boolean antialias) {
        this.antialias = antialias;
        return this;
    }

    public boolean isAntialias() {
        return antialias;
    }

    public PaintBuilder setComposite(Composite composite) {
        Objects.requireNonNull(composite, "composite == null");
        this.composite = composite;
        return this;
    }

    public Composite getComposite() {
        return composite;
    }

    public PaintBuilder setOpacity(float opacity) {
        Checks.require(0 <= opacity && opacity <= 1, "opacity < 0 || 1 <  opacity");
        this.opacity = opacity;
        return this;
    }

    public float getOpacity() {
        return opacity;
    }

    public abstract Paint build();
}
