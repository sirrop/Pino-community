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

package jp.gr.java_conf.alpius.pino.window.impl.skin;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.StackPane;
import jp.gr.java_conf.alpius.pino.application.impl.GraphicManager;
import jp.gr.java_conf.alpius.pino.window.impl.LayerEditor;

public class LayerEditorSkin extends SkinBase<LayerEditor> {
    private final StackPane placeholder = new StackPane(new Label("No layer is selected."));

    public LayerEditorSkin(LayerEditor control) {
        super(control);
        initialize();
    }

    private void initialize() {
        getSkinnable().placeholderProperty().addListener(obs -> updateSkin());
        getSkinnable().itemProperty().addListener(obs -> updateSkin());
        updateSkin();
    }

    private void updateSkin() {
        var layer = getSkinnable().getItem();
        Node graphic;
        if (layer == null) {
            graphic = getSkinnable().getPlaceholder();
            if (graphic == null) {
                graphic = placeholder;
            }
        } else {
            graphic = GraphicManager.getInstance().getEditorGraphic(layer);
        }
        var container = new ScrollPane();
        container.setContent(graphic);
        container.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        container.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        getChildren().setAll(container);
    }
}
