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

import jp.gr.java_conf.alpius.pino.graphics.brush.event.DrawEvent;
import jp.gr.java_conf.alpius.pino.graphics.layer.DrawableLayer;
import jp.gr.java_conf.alpius.pino.graphics.layer.Layers;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class EraserTest {
    @Test
    public void doTest() throws Exception {
        DrawableLayer layer = Layers.create(DrawableLayer::new, 500, 500);
        var g = layer.createGraphics();
        g.setPaint(Color.BLACK);
        g.fillRect(0, 0, 500, 500);
        g.dispose();

        var brush = new Eraser();
        brush.setAntialiasing(true);
        brush.setOpacity(0.5f);
        brush.setWidth(50);

        var now = System.nanoTime();
        var context = brush.createContext(layer);
        context.onStart(new DrawEvent(layer, DrawEvent.Type.ON_START, 50, 50));
        context.onFinished(new DrawEvent(layer, DrawEvent.Type.ON_FINISHED, 450, 450));
        context.dispose();
        System.out.println("render time: " + (System.nanoTime() - now) + "ns");

        ImageIO.write(Layers.snapshot(layer), "png", Files.newOutputStream(Paths.get("test", "resources", "EraserTest.png")));
    }
}
