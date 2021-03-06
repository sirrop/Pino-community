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

package jp.gr.java_conf.alpius.pino.gui;

import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import jp.gr.java_conf.alpius.pino.graphics.brush.Brush;
import jp.gr.java_conf.alpius.pino.graphics.layer.LayerObject;
import jp.gr.java_conf.alpius.pino.gui.widget.BrushEditor;
import jp.gr.java_conf.alpius.pino.gui.widget.LayerEditor;
import jp.gr.java_conf.alpius.pino.gui.widget.ToolEditor;
import org.controlsfx.control.StatusBar;

public interface RootContainer {
    Parent getContent();
    Canvas getCanvas();
    Pane getCanvasPane();
    ToolEditor getToolEditor();
    LayerEditor getLayerEditor();
    BrushEditor getBrushEditor();
    ListView<LayerObject> getLayerView();
    ListView<Brush> getBrushView();
    StatusBar getStatusBar();
}
