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

package jp.gr.java_conf.alpius.pino.gui.widget;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import jp.gr.java_conf.alpius.pino.application.impl.BrushManager;
import jp.gr.java_conf.alpius.pino.application.impl.GraphicManager;
import jp.gr.java_conf.alpius.pino.graphics.brush.Brush;

public class BrushCell extends ListCell<Brush> {
    public BrushCell() {
        setContextMenu(MenuManager.getInstance().getBrushCellMenu());
        focusedProperty().addListener((obs, oldValue, newValue) -> {
            Brush item = getItem();
            if (newValue && item != null) {
                var manager = BrushManager.getInstance();
                var index = manager.getBrushList().indexOf(item);
                manager.getActiveModel().activate(index);
            }
        });
        BrushManager.getInstance()
                .getActiveModel()
                .addListener(i -> updateActive());
    }

    public void updateItem(Brush item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            setGraphic(null);
            setText(null);
        } else {
            var graphic = GraphicManager.getInstance().getCellGraphic(item);
            initGraphic(graphic);
            setGraphic(graphic);
        }
        updateActive();
    }

    private void updateActive() {
        if (BrushManager.getInstance().getActiveModel().getActivatedItem() == getItem()) {
            setBackground(new Background(new BackgroundFill(Color.SKYBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        } else {
            setBackground(null);
        }
    }

    private void initGraphic(Node graphic) {

    }
}
