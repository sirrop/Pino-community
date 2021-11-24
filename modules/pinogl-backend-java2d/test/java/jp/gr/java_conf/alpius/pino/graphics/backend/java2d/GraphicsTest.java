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

package jp.gr.java_conf.alpius.pino.graphics.backend.java2d;

import jp.gr.java_conf.alpius.pino.graphics.*;
import jp.gr.java_conf.alpius.pino.graphics.paint.ColorPaint;
import org.junit.jupiter.api.Test;

public class GraphicsTest {
    @Test
    public void drawLine() {
        var desc = new Java2DDescriptor();
        var platform = desc.getProvider().get();

        var texture = platform.createTexture(new Image(500, 500, StandardPixelFormat.ARGB32, AlphaType.UNPREMUL));
        var g = texture.createGraphics();
        var p = new ColorPaint(true, Composite.getInstance(BlendMode.SRC_OVER), 1f, Color.RED);
    }
}
