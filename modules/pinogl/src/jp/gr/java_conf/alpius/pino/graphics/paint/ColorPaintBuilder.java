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

import jp.gr.java_conf.alpius.pino.graphics.Color;
import jp.gr.java_conf.alpius.pino.graphics.Composite;

public class ColorPaintBuilder extends PaintBuilder {
    private Color color;

    @Override
    public ColorPaintBuilder setAntialias(boolean antialias) {
        return (ColorPaintBuilder) super.setAntialias(antialias);
    }

    @Override
    public ColorPaintBuilder setComposite(Composite composite) {
        return (ColorPaintBuilder) super.setComposite(composite);
    }

    @Override
    public ColorPaintBuilder setOpacity(float opacity) {
        return (ColorPaintBuilder) super.setOpacity(opacity);
    }

    public ColorPaintBuilder setColor(Color color) {
        this.color = color;
        return this;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public ColorPaint build() {
        return new ColorPaint(isAntialias(), getComposite(), getOpacity(), color);
    }
}
