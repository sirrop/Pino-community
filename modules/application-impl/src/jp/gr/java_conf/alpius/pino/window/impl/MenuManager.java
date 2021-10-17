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

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import jp.gr.java_conf.alpius.pino.application.impl.BrushManager;
import jp.gr.java_conf.alpius.pino.application.impl.Pino;
import jp.gr.java_conf.alpius.pino.graphics.brush.Eraser;
import jp.gr.java_conf.alpius.pino.graphics.brush.Pencil;
import jp.gr.java_conf.alpius.pino.graphics.layer.DrawableLayer;
import jp.gr.java_conf.alpius.pino.graphics.layer.ImageLayer;
import jp.gr.java_conf.alpius.pino.graphics.layer.LayerObject;
import jp.gr.java_conf.alpius.pino.graphics.layer.Layers;
import jp.gr.java_conf.alpius.pino.graphics.layer.geom.Ellipse;
import jp.gr.java_conf.alpius.pino.graphics.layer.geom.Rectangle;
import jp.gr.java_conf.alpius.pino.graphics.layer.geom.Text;

import java.util.function.Supplier;

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
        initBrushEditor();
        initBrushCell();
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
            var shape = new Menu("図形");
            {
                var rect = new MenuItem("四角形");
                var ellipse = new MenuItem("楕円");
                rect.setOnAction(addAction(Rectangle::new));
                ellipse.setOnAction(addAction(Ellipse::new));
                shape.getItems().addAll(rect, ellipse);
            }
            var text = new MenuItem("テキスト");
            drawable.setOnAction(addAction(DrawableLayer::new));
            image.setOnAction(addAction(ImageLayer::new));
            text.setOnAction(addAction(Text::new));
            add.getItems().addAll(drawable, image, shape, text);
        }

        var delete = new MenuItem("削除");
        delete.setOnAction(e -> {
            var p = Pino.getApp().getProject();
            var index = p.getActiveModel().getActivatedIndex();
            p.getLayers().remove(index);
            if (index > 0) {
                --index;
            } else {
                if (p.getLayers().size() == 0) {
                    index = -1;
                }
            }
            syncLayerSelection(index);
        });
        layerCell.getItems().addAll(add, delete);
    }

    private static EventHandler<ActionEvent> addAction(Supplier<? extends LayerObject> constructor) {
        return e -> {
            var p = Pino.getApp().getProject();
            var index = p.getActiveModel().getActivatedIndex();
            p.getLayers().add(index, Layers.create(constructor, p.getWidth(), p.getHeight()));
            syncLayerSelection(index);
        };
    }

    private static void syncLayerSelection(int index) {
        Pino.getApp().getProject().getActiveModel().activate(index);
        ((JFxWindow) Pino.getApp().getWindow()).getRootContainer().getLayerView().getSelectionModel().clearAndSelect(index);
    }

    private void initBrushEditor() {
        var rename = new MenuItem("リネーム");
        rename.setOnAction(e -> {
            var brush = BrushManager.getInstance().getActiveModel().getActivatedItem();
            var dialog = new Dialog<ButtonType>();
            var textField = new TextField();
            dialog.getDialogPane().setContent(textField);
            textField.setText(brush.getName());
            dialog.getDialogPane().getButtonTypes().setAll(ButtonType.CANCEL, ButtonType.OK);

            dialog.showAndWait()
                    .filter(it -> it == ButtonType.OK)
                    .ifPresent(it -> {
                        var newName = textField.getText();
                        if (newName == null || newName.isEmpty() || newName.isBlank()) {
                            return;
                        }
                        brush.setName(newName);
                        var root = ((JFxWindow) Pino.getApp().getWindow()).getRootContainer();
                        root.getBrushEditor().refresh();
                        root.getBrushView().refresh();
                    });
        });
        brushEditor.getItems().addAll(rename);
    }

    private void initBrushCell() {
        var add = new Menu("追加");
        {
            var pencil = new MenuItem("Pencil");
            var eraser = new MenuItem("Eraser");
            pencil.setOnAction(e -> {
                var mgr = BrushManager.getInstance();
                int index = mgr.getActiveModel().getActivatedIndex();
                mgr.getBrushList().add(index, new Pencil());
            });
            eraser.setOnAction(e -> {
                var mgr = BrushManager.getInstance();
                int index = mgr.getActiveModel().getActivatedIndex();
                mgr.getBrushList().add(index, new Eraser());
            });
            add.getItems().addAll(pencil, eraser);
        }

        var delete = new MenuItem("削除");
        delete.setOnAction(e -> {
            var mgr = BrushManager.getInstance();
            var item = mgr.getActiveModel().getActivatedItem();
            mgr.getBrushList().remove(item);
        });
        brushCell.getItems().addAll(add, delete);
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

    public ContextMenu getBrushCellMenu() {
        return brushCell;
    }
}
