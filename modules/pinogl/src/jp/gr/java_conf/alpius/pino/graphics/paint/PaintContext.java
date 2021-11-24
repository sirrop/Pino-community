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

import jp.gr.java_conf.alpius.pino.graphics.paint.stroke.Stroke;

import java.util.Arrays;
import java.util.List;

public final class PaintContext {
    private final Paint paint;
    private final List<Stroke> strokes;

    public PaintContext(Paint paint, Stroke... strokes) {
        this.paint = paint;
        this.strokes = Arrays.asList(strokes);
    }

    public Paint getPaint() {
        return paint;
    }

    public List<Stroke> getStrokes() {
        return strokes;
    }
}
