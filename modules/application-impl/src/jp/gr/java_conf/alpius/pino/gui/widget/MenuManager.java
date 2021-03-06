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

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import jp.gr.java_conf.alpius.pino.application.impl.BrushManager;
import jp.gr.java_conf.alpius.pino.application.impl.Pino;
import jp.gr.java_conf.alpius.pino.graphics.brush.*;
import jp.gr.java_conf.alpius.pino.graphics.canvas.Canvas;
import jp.gr.java_conf.alpius.pino.graphics.layer.DrawableLayer;
import jp.gr.java_conf.alpius.pino.graphics.layer.ImageLayer;
import jp.gr.java_conf.alpius.pino.graphics.layer.LayerObject;
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
        var rename = new MenuItem("レイヤー名を変更");
        rename.setOnAction(e -> {
            var p = Pino.getApp().getProject();
            var layer = p.getActiveModel().getActivatedItem();
            var dialog = new Dialog<ButtonType>();
            var textField = new TextField();
            dialog.setTitle("レイヤー名を変更");
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
                        var root = Pino.getApp().getWindow().getRootContainer();
                        root.getLayerEditor().refresh();
                        root.getLayerView().refresh();
                    });
        });
        var convertLayer = new MenuItem("描画レイヤーに変換");
        convertLayer.setOnAction(e -> {
            var p = Pino.getApp().getProject();
            var activeModel = p.getActiveModel();
            var index = activeModel.getActivatedIndex();
            var original = activeModel.getActivatedItem();
            p.getChildren().set(index, original.toDrawable(Canvas.createGeneral(p.getWidth(), p.getHeight())));
        });
        layerEditor.getItems().setAll(rename, convertLayer);
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
            drawable.setOnAction(addAction(() -> new DrawableLayer(createCanvas())));
            image.setOnAction(addAction(ImageLayer::new));
            text.setOnAction(addAction(Text::new));
            add.getItems().addAll(drawable, image, shape, text);
        }

        var delete = new MenuItem("削除");
        delete.setOnAction(e -> {
            var p = Pino.getApp().getProject();
            var index = p.getActiveModel().getActivatedIndex();
            p.getChildren().remove(index);
            if (index > 0) {
                --index;
            } else {
                if (p.getChildren().size() == 0) {
                    index = -1;
                }
            }
            syncLayerSelection(index);
        });

        var up = new MenuItem("上へ動かす");
        up.setOnAction(e -> {
            var p = Pino.getApp().getProject();
            var index = p.getActiveModel().getActivatedIndex();
            if (index != 0) {
                var layer = p.getChildren().remove(index);
                p.getChildren().add(index - 1, layer);
                syncLayerSelection(index - 1);
            }
        });

        var down = new MenuItem("下へ動かす");
        down.setOnAction(e -> {
            var p = Pino.getApp().getProject();
            var index = p.getActiveModel().getActivatedIndex();
            if (index != p.getChildren().size() - 1) {
                var layer = p.getChildren().remove(index);
                p.getChildren().add(index + 1, layer);
                syncLayerSelection(index + 1);
            }
        });
        layerCell.getItems().addAll(add, delete, up, down);
    }

    private static EventHandler<ActionEvent> addAction(Supplier<? extends LayerObject> constructor) {
        return e -> {
            var p = Pino.getApp().getProject();
            var index = p.getActiveModel().getActivatedIndex();
            p.getChildren().add(index, constructor.get());
            syncLayerSelection(index);
        };
    }

    private static void syncLayerSelection(int index) {
        Pino.getApp().getProject().getActiveModel().activate(index);
        Pino.getApp().getWindow().getRootContainer().getLayerView().getSelectionModel().clearAndSelect(index);
    }

    private static Canvas createCanvas() {
        var p = Pino.getApp().getProject();
        if (p == null) {
            throw new IllegalStateException("project == null");
        }
        return Canvas.createGeneral(p.getWidth(), p.getHeight());
    }

    private void initBrushEditor() {
        var rename = new MenuItem("ブラシ名を変更");
        rename.setOnAction(e -> {
            var brush = BrushManager.getInstance().getActiveModel().getActivatedItem();
            var dialog = new Dialog<ButtonType>();
            var textField = new TextField();
            dialog.setTitle("ブラシ名を変更");
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
                        var root = Pino.getApp().getWindow().getRootContainer();
                        root.getBrushEditor().refresh();
                        root.getBrushView().refresh();
                    });
        });
        brushEditor.getItems().addAll(rename);
    }

    private void initBrushCell() {
        var add = new Menu("追加");
        {
            var pencil = new MenuItem("鉛筆");
            var eraser = new MenuItem("消しゴム");
            var marker = new MenuItem("マーカー");
            var blur = new MenuItem("平均化ブラシ");
            var gaussianBlur = new MenuItem("ガウスぼかしブラシ");
            var airbrush = new MenuItem("エアブラシ");
            pencil.setOnAction(e -> {
                var mgr = BrushManager.getInstance();
                int index = mgr.getActiveModel().getActivatedIndex();
                mgr.getBrushList().add(index, new Pencil());
                syncBrushSelection(index);
            });
            eraser.setOnAction(e -> {
                var mgr = BrushManager.getInstance();
                int index = mgr.getActiveModel().getActivatedIndex();
                mgr.getBrushList().add(index, new Eraser());
                syncBrushSelection(index);
            });
            marker.setOnAction(e -> {
                var mgr = BrushManager.getInstance();
                int index = mgr.getActiveModel().getActivatedIndex();
                mgr.getBrushList().add(index, new Marker());
                syncBrushSelection(index);
            });
            blur.setOnAction(e -> {
                var mgr = BrushManager.getInstance();
                int index = mgr.getActiveModel().getActivatedIndex();
                mgr.getBrushList().add(index, new MeanBlur());
                syncBrushSelection(index);
            });
            gaussianBlur.setOnAction(e -> {
                var mgr = BrushManager.getInstance();
                int index = mgr.getActiveModel().getActivatedIndex();
                mgr.getBrushList().add(index, new GaussianBlur());
                syncBrushSelection(index);
            });
            airbrush.setOnAction(e -> {
                var mgr = BrushManager.getInstance();
                int index = mgr.getActiveModel().getActivatedIndex();
                mgr.getBrushList().add(index, new Airbrush());
                syncBrushSelection(index);
            });
            add.getItems().addAll(airbrush, pencil, eraser, marker, blur, gaussianBlur);
        }

        var delete = new MenuItem("削除");
        delete.setOnAction(e -> {
            var mgr = BrushManager.getInstance();
            var item = mgr.getActiveModel().getActivatedItem();
            mgr.getBrushList().remove(item);
        });

        var up = new MenuItem("上へ動かす");
        up.setOnAction(e -> {
            var p = BrushManager.getInstance();
            var index = p.getActiveModel().getActivatedIndex();
            if (index != 0) {
                var brush = p.getBrushList().remove(index);
                p.getBrushList().add(index - 1, brush);
                syncBrushSelection(index - 1);
            }
        });

        var down = new MenuItem("下へ動かす");
        down.setOnAction(e -> {
            var p = BrushManager.getInstance();
            var index = p.getActiveModel().getActivatedIndex();
            if (index != p.getBrushList().size() - 1) {
                var brush = p.getBrushList().remove(index);
                p.getBrushList().add(index + 1, brush);
                syncBrushSelection(index + 1);
            }
        });
        brushCell.getItems().addAll(add, delete, up, down);
    }

    private static void syncBrushSelection(int index) {
        BrushManager.getInstance().getActiveModel().activate(index);
        Pino.getApp().getWindow().getRootContainer().getBrushView().getSelectionModel().clearAndSelect(index);
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
