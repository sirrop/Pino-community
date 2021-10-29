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

package jp.gr.java_conf.alpius.pino.gui.screen.options;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.Dialog;
import javafx.scene.control.Skin;
import javafx.stage.Modality;
import javafx.stage.WindowEvent;
import jp.gr.java_conf.alpius.pino.application.impl.Pino;
import jp.gr.java_conf.alpius.pino.gui.screen.options.skin.OptionScreenSkin;

import java.util.ArrayList;
import java.util.List;

public class OptionScreen extends Control {

    // デフォルトのオプションを追加したOptionScreenを返す
    public static OptionScreen create() {
        var screen = new OptionScreen();
        screen.getItems()
                .addAll(
                        new LookAndFeelOptions(), // 外観
                        new MenuOptions(),        // メニュー
                        new CursorOptions(),      // カーソル
                        new CanvasOptions()       // キャンバス
                );
        return screen;
    }

    private double prefWidth = 800, prefHeight = 600;
    private ObservableList<Option> items;

    public ObservableList<Option> getItems() {
        if (items == null) {
            items = FXCollections.observableList(new ArrayList<>());
        }
        return items;
    }

    public void setPrefWidth(double width, double height) {
        this.prefWidth = check(width, "width");
        this.prefHeight = check(height, "height");
    }

    private static double check(double value, String name) {
        if (Double.isInfinite(value)) {
            throw new IllegalArgumentException(name + " is infinite");
        }
        if (Double.isNaN(value)) {
            throw new IllegalArgumentException(name + " is NaN");
        }
        if (value <= 0) {
            throw new IllegalArgumentException(name + " <= 0");
        }
        return value;
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new OptionScreenSkin(this);
    }

    public void showAndWait() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("設定");
        dialog.getDialogPane().setContent(this);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.APPLY, ButtonType.OK);
        dialog.getDialogPane().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, e -> cancel());
        dialog.setResizable(true);
        dialog.getDialogPane().setPrefSize(prefWidth, prefHeight);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(Pino.getApp().getWindow().getStage());
        dialog.showAndWait()
                .ifPresentOrElse(

                        // on success
                        buttonType -> {
                            if (buttonType == ButtonType.OK || buttonType == ButtonType.APPLY) {
                                apply();
                            } else if (buttonType == ButtonType.CANCEL) {
                                cancel();
                            }
                        },

                        // on failed
                        this::cancel
                );
    }

    private void cancel() {
        var items = List.copyOf(getItems());
        for (var item: items) {
            item.onCancel();
        }
    }

    private void apply() {
        var items = List.copyOf(getItems());
        for (var item: items) {
            item.onApply();
        }
    }
}
