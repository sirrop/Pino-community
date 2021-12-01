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

package jp.gr.java_conf.alpius.pino.gui.widget.skin;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SkinBase;
import jp.gr.java_conf.alpius.pino.application.impl.GraphicManager;
import jp.gr.java_conf.alpius.pino.gui.widget.ToolEditor;
import jp.gr.java_conf.alpius.pino.tool.Tool;

public class ToolEditorSkin extends SkinBase<ToolEditor> {
    public ToolEditorSkin(ToolEditor editor) {
        super(editor);
        getSkinnable().itemProperty().addListener((observable, oldValue, newValue) -> update(newValue));
    }

    protected void update(Tool tool) {
        Node graphic = GraphicManager.getInstance().getEditorGraphic(tool);
        var container = new ScrollPane();
        container.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        container.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        container.setContent(graphic);
        getChildren().setAll(container);
    }
}
