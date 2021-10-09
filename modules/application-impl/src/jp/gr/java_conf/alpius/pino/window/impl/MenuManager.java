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

package jp.gr.java_conf.alpius.pino.window.impl;

import javafx.scene.control.*;
import jp.gr.java_conf.alpius.pino.application.impl.Pino;
import jp.gr.java_conf.alpius.pino.graphics.layer.DrawableLayer;
import jp.gr.java_conf.alpius.pino.graphics.layer.ImageLayer;
import jp.gr.java_conf.alpius.pino.graphics.layer.Layers;

/**
 * Menuを管理します
 */
public final class MenuManager {
    public static MenuManager getInstance() {
        return Pino.getApp().getService(MenuManager.class);
    }

    private final ContextMenu layerEditor = new ContextMenu();
    private final ContextMenu layerCell   = new ContextMenu();
    private final ContextMenu brushEditor = new ContextMenu();
    private final ContextMenu brushCell   = new ContextMenu();

    public MenuManager() {
        initLayerEditor();
        initLayerCell();
    }

    private void initLayerEditor() {
        var rename = new MenuItem("リネーム");
        rename.setOnAction(e -> {
            var p = Pino.getApp().getProject();
            var layer = p.getActiveModel().getActivatedItem();
            var dialog = new Dialog<ButtonType>();
            var textField = new TextField();
            dialog.getDialogPane().setContent(textField);
            textField.setText(layer.getName());
            dialog.getDialogPane().getButtonTypes().setAll(ButtonType.CANCEL, ButtonType.OK);

            dialog.showAndWait()
                    .filter(it -> it == ButtonType.OK)
                    .ifPresent(it -> {
                        var newName = textField.getText();
                        if (newName == null || newName.isEmpty() || newName.isBlank()) {
                            return;
                        }
                        layer.setName(newName);
                        var root = ((JFxWindow) Pino.getApp().getWindow()).getRootContainer();
                        root.getLayerEditor().refresh();
                        root.getLayerView().refresh();
                    });
        });

        layerEditor.getItems().setAll(rename);
    }

    private void initLayerCell() {
        var add = new Menu("追加");
        {
            var drawable = new MenuItem("描画レイヤー");
            var image = new MenuItem("画像レイヤー");
            drawable.setOnAction(e -> {
                var p = Pino.getApp().getProject();
                p.getLayers().add(p.getActiveModel().getActivatedIndex(), Layers.create(DrawableLayer::new, p.getWidth(), p.getHeight()));
            });
            image.setOnAction(e -> {
                var p = Pino.getApp().getProject();
                p.getLayers().add(p.getActiveModel().getActivatedIndex(), Layers.create(ImageLayer::new, p.getWidth(), p.getHeight()));
            });
            add.getItems().addAll(drawable, image);
        }

        var delete = new MenuItem("削除");
        delete.setOnAction(e -> {
            var p = Pino.getApp().getProject();
            p.getLayers().remove(p.getActiveModel().getActivatedIndex());
        });
        layerCell.getItems().addAll(add, delete);
    }

    public ContextMenu getLayerEditorMenu() {
        return layerEditor;
    }


    public ContextMenu getLayerCellMenu() {
        return layerCell;
    }

    public ContextMenu getBrushEditorMenu() {
        return brushEditor;
    }
}
