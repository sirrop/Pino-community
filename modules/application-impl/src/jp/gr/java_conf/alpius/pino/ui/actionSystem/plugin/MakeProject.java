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

package jp.gr.java_conf.alpius.pino.ui.actionSystem.plugin;

import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.converter.IntegerStringConverter;
import jp.gr.java_conf.alpius.pino.application.impl.Pino;
import jp.gr.java_conf.alpius.pino.project.impl.PinoProject;
import jp.gr.java_conf.alpius.pino.ui.actionSystem.Action;
import jp.gr.java_conf.alpius.pino.ui.actionSystem.ActionEvent;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.color.ICC_Profile;

import static jp.gr.java_conf.alpius.pino.internal.InternalLogger.log;

public class MakeProject implements Action {
    @Override
    public void performAction(ActionEvent e) {
        makeDialog().showAndWait()
                .ifPresent(info -> {
                    log("success: make project! [width: %d, height: %d, profile: %s, accel: %b]", info.width, info.height, info.profile, info.accel);
                    var project = new PinoProject(info.width, info.height);
                    project.getCanvas().setBackground(info.background());
                    Pino.getApp().setProjectAndDispose(project);
                });
    }

    private static Dialog<ProjectInfo> makeDialog() {
        Dialog<ProjectInfo> dialog = new Dialog<>();
        var display = new Display();
        dialog.getDialogPane()
                .getChildren()
                .add(display);
        dialog.getDialogPane()
                .getButtonTypes()
                .setAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.setTitle("新規プロジェクトを作成する");
        dialog.setResizable(false);
        dialog.getDialogPane().setPrefWidth(250);
        dialog.getDialogPane().setPrefHeight(130);
        dialog.setResultConverter(type -> {
            if (type == ButtonType.OK) {
                return display.getResult();
            } else {
                return null;
            }
        });
        return dialog;
    }

    private static record ProjectInfo(
            int width, int height,
            ICC_Profile profile,
            Paint background,
            boolean accel
    ) {
    }

    private static class Display extends Group {
        private static final double LABEL_PREF_WIDTH = 40;

        private int width = 500,
                    height = 500;

        private final Label widthLabel                          = new Label("幅");
        private final Label heightLabel                         = new Label("高さ");
        private final TextField widthInput                      = new TextField();
        private final TextField heightInput                     = new TextField();
        private final TextFormatter<Integer> widthFormatter     = new TextFormatter<>(new IntegerStringConverter(), width);
        private final TextFormatter<Integer> heightFormatter    = new TextFormatter<>(new IntegerStringConverter(), height);
        private final Label px0                                 = new Label("px");
        private final Label px1                                 = new Label("px");

        // 背景色選択用
        private final Label backgroundPickerLabel = new Label("背景色");
        private final ColorPicker backgroundPicker = new ColorPicker();

        public Display() {
            var container = new Pane();
            var hbox0 = new HBox(widthLabel, widthInput, px0);
            var hbox1 = new HBox(heightLabel, heightInput, px1);
            var vbox = new VBox(hbox0, hbox1, new HBox(backgroundPickerLabel, backgroundPicker));
            hbox0.setSpacing(3);
            hbox1.setSpacing(3);
            vbox.setPadding(new Insets(5));
            vbox.setSpacing(3);
            container.getChildren().setAll(vbox);
            widthInput.textProperty().addListener((obs, oldValue, newValue) -> width = Integer.parseInt(newValue));
            heightInput.textProperty().addListener((obs, oldValue, newValue) -> height = Integer.parseInt(newValue));
            widthInput.setTextFormatter(widthFormatter);
            heightInput.setTextFormatter(heightFormatter);
            widthLabel.setPrefWidth(LABEL_PREF_WIDTH);
            heightLabel.setPrefWidth(LABEL_PREF_WIDTH);
            backgroundPickerLabel.setPrefWidth(LABEL_PREF_WIDTH);
            backgroundPicker.setValue(javafx.scene.paint.Color.TRANSPARENT);
            getChildren().setAll(container);
        }


        public ProjectInfo getResult() {
            return new ProjectInfo(width, height, ICC_Profile.getInstance(ColorSpace.CS_sRGB), asAwtColor(backgroundPicker.getValue()), false);
        }

        private static Color asAwtColor(javafx.scene.paint.Color c) {
            float a = (float) c.getOpacity();
            float r = (float) c.getRed();
            float g = (float) c.getGreen();
            float b = (float) c.getBlue();
            return new Color(r, g, b, a);
        }
    }
}
