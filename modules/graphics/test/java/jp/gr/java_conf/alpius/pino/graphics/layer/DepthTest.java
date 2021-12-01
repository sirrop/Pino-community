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

package jp.gr.java_conf.alpius.pino.graphics.layer;

import jp.gr.java_conf.alpius.pino.graphics.canvas.Canvas;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DepthTest {
    @Test
    public void dotest() {
        var layer = new DrawableLayer(Canvas.createGeneral());
        var folder = new Folder(new ArrayList<>());
        var parent = new Folder(new ArrayList<>());
        assertEquals(0, layer.getDepth());
        folder.getChildren().add(layer);
        layer.setParent(folder);
        assertEquals(1, layer.getDepth());
        assertEquals(0, folder.getDepth());
        parent.getChildren().add(folder);
        folder.setParent(parent);
        assertEquals(2, layer.getDepth());
        assertEquals(1, folder.getDepth());
        assertEquals(0, parent.getDepth());
    }
}
