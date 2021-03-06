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
import javafx.scene.control.ListCell;
import javafx.scene.control.Skin;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import jp.gr.java_conf.alpius.pino.annotation.Internal;
import jp.gr.java_conf.alpius.pino.application.impl.GraphicManager;
import jp.gr.java_conf.alpius.pino.application.impl.Pino;
import jp.gr.java_conf.alpius.pino.disposable.Disposable;
import jp.gr.java_conf.alpius.pino.graphics.layer.LayerObject;
import jp.gr.java_conf.alpius.pino.gui.widget.skin.LayerCellSkin;

public class LayerCell extends ListCell<LayerObject> {
    private Disposable disposable;
    private final StackPane upperLabel = new StackPane();
    private final StackPane lowerLabel = new StackPane();

    public LayerCell() {
        upperLabel.setBackground(new Background(new BackgroundFill(Color.SKYBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        lowerLabel.setBackground(new Background(new BackgroundFill(Color.SKYBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        upperLabel.setPrefHeight(2);
        lowerLabel.setPrefHeight(2);
        upperLabel.setVisible(false);
        lowerLabel.setVisible(false);
        setContextMenu(MenuManager.getInstance().getLayerCellMenu());
        focusedProperty().addListener((obs, oldValue, newValue) -> {
            LayerObject item = getItem();
            if (newValue && item != null) {
                var project = Pino.getApp().getProject();
                var index = project.getChildren().indexOf(item);
                project.getActiveModel().activate(index);
            }
        });
        var p = Pino.getApp().getProject();
        if (p != null) {
            p.getActiveModel().addListener(i -> updateActive());
        }
        updateActive();
    }

    @Override
    public void updateItem(LayerObject item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            if (disposable != null) {
                disposable.dispose();
            }
            setGraphic(null);
            setText(null);
        } else {
            var graphic = GraphicManager.getInstance().getCellGraphic(item);
            setGraphic(new VBox(upperLabel, graphic, lowerLabel));
            updateActive();
        }
    }

    @Internal
    public void showUpperLabel() {
        upperLabel.setVisible(true);
    }

    @Internal
    public void showLowerLabel() {
        lowerLabel.setVisible(true);
    }

    @Internal
    public void hideLabel() {
        upperLabel.setVisible(false);
        lowerLabel.setVisible(false);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new LayerCellSkin(this);
    }

    private void updateActive() {
        var p = Pino.getApp().getProject();
        if (getItem() == null || p == null) {
            setBackground(null);
            return;
        }
        if (p.getActiveModel().getActivatedItem() == getItem()) {
            setBackground(new Background(new BackgroundFill(Color.SKYBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        } else {
            setBackground(null);
        }
    }
}
