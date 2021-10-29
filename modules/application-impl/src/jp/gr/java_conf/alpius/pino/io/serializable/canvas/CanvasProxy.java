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

package jp.gr.java_conf.alpius.pino.io.serializable.canvas;

import jp.gr.java_conf.alpius.pino.annotation.Beta;
import jp.gr.java_conf.alpius.pino.graphics.canvas.Canvas;
import jp.gr.java_conf.alpius.pino.graphics.canvas.internal.GeneralCanvas;

import java.io.Serial;
import java.io.Serializable;

@Beta
public abstract class CanvasProxy implements Serializable {
    public static CanvasProxy create(Canvas canvas) {
        if (canvas instanceof GeneralCanvas general) {
            return new GeneralCanvasProxy(general);
        }
        throw new IllegalArgumentException("Unknown canvas");
    }

    @Serial
    private static final long serialVersionUID = 7162674507003193956L;

    public abstract Canvas createCanvas(int w, int h);
}
