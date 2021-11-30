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

package jp.gr.java_conf.alpius.pino.gui.widget.util;


import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.converter.NumberStringConverter;

import java.util.function.Consumer;

public final class Nodes {
    private Nodes() {}

    private static final double DEFAULT_LABEL_PREF_WIDTH = 60;
    private static final double DEFAULT_TEXTFIELD_PREF_WIDTH = 60;

    public static void show(Node node) {
        node.setVisible(true);
        node.setManaged(true);
    }

    public static void hide(Node node) {
        node.setVisible(false);
        node.setManaged(false);
    }

    public static Node slider(Consumer<Slider> configurator) {
        return slider(configurator, 1);
    }

    public static Node slider(Consumer<Slider> configurator, int mug) {
        var slider = new Slider();
        configurator.accept(slider);
        var incBtn = new Button("+");
        incBtn.setOnAction(e -> slider.increment());
        incBtn.getStyleClass().add("increment-button");
        var decBtn = new Button("-");
        decBtn.setOnAction(e -> slider.decrement());
        decBtn.getStyleClass().add("decrement-button");
        var textField = new TextField();
        var formatter = new TextFormatter<>(new NumberStringConverter() {
            @Override
            public String toString(Number number) {
                String result = super.toString(number);
                return drop(result, getNumDigitsUnderPoint(result) - mug);
            }
        }, slider.getValue());
        textField.setTextFormatter(formatter);
        textField.setPrefWidth(DEFAULT_TEXTFIELD_PREF_WIDTH);
        formatter.valueProperty().bindBidirectional(slider.valueProperty());
        return new HBox(slider, decBtn, incBtn, textField);
    }

    private static int getNumDigitsUnderPoint(String str) {
        int point = str.indexOf('.');
        if (point == -1) {
            return 0;
        } else {
            return str.length() - point - 1;
        }
    }

    private static String drop(String string, int num) {
        if (num <= 0) return string;
        return string.substring(0, string.length() - num);
    }


    public static Label label(String text) {
        var res = new Label(text);
        res.setPrefWidth(DEFAULT_LABEL_PREF_WIDTH);
        return res;
    }

    public static Node labeled(String text, Node labeled) {
        var label = new Label(text);
        label.setPrefWidth(DEFAULT_LABEL_PREF_WIDTH);
        return new HBox(label, labeled);
    }
}
