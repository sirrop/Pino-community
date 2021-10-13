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

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.converter.NumberStringConverter;
import jp.gr.java_conf.alpius.pino.application.impl.GraphicManager;
import jp.gr.java_conf.alpius.pino.graphics.brush.Brush;
import jp.gr.java_conf.alpius.pino.graphics.brush.Eraser;
import jp.gr.java_conf.alpius.pino.graphics.brush.Pencil;

import java.util.function.Consumer;

public class DefaultBrushEditorGraphicVisitor implements GraphicManager.BrushEditorGraphicVisitor {
    private static final double DEFAULT_LABEL_PREF_WIDTH = 60;
    private static final double DEFAULT_TEXTFIELD_PREF_WIDTH = 60;

    public Node visit(Brush brush) {
        VBox container = new VBox(3);
        container.setPadding(new Insets(5));

        var name = new Label(brush.getClass().getSimpleName());
        container.getChildren().add(name);

        if (brush instanceof Pencil pencil) {
            var width = slider(slider -> {
                slider.setValue(pencil.getWidth());
                slider.valueProperty().addListener((observable, oldValue, newValue) -> pencil.setWidth(newValue.floatValue()));
            });

            var opacity = slider(slider -> {
                slider.setMin(0);
                slider.setMax(100);
                slider.setValue(pencil.getOpacity() * 100);
                slider.valueProperty().addListener((observable, oldValue, newValue) -> pencil.setOpacity(newValue.floatValue() / 100));
            });

            var colorPicker = new ColorPicker();
            colorPicker.setValue(asFxColor(pencil.getColor()));
            colorPicker.valueProperty().addListener((observable, oldValue, newValue) -> pencil.setColor(asAwtColor(newValue)));
            container.getChildren().addAll(labeled("幅", width), labeled("不透明度", opacity), colorPicker);
        } else if (brush instanceof Eraser eraser) {
            var width = slider(it -> {
                it.setValue(eraser.getWidth());
                it.valueProperty().addListener((observable, oldValue, newValue) -> eraser.setWidth(newValue.floatValue()));
            });

            var opacity = slider(it -> {
                it.setMin(0);
                it.setMax(100);
                it.setValue(eraser.getOpacity() * 100);
                it.valueProperty().addListener((observable, oldValue, newValue) -> eraser.setOpacity(newValue.floatValue() / 100));
            });

            container.getChildren().addAll(labeled("幅", width), labeled("不透明度", opacity));
        }
        return container;
    }

    private static Node labeled(String text, Node labeled) {
        var label = new Label(text);
        label.setPrefWidth(DEFAULT_LABEL_PREF_WIDTH);
        return new HBox(label, labeled);
    }

    private static Color asFxColor(java.awt.Color awt) {
        double a = awt.getAlpha();
        double r = awt.getRed();
        double g = awt.getGreen();
        double b = awt.getBlue();
        a /= 255;
        r /= 255;
        g /= 255;
        b /= 255;
        return new Color(r, g, b, a);
    }

    private static java.awt.Color asAwtColor(Color color) {
        int alpha = (int) (color.getOpacity() * 255);
        int red = (int) (color.getRed() * 255);
        int green = (int) (color.getGreen() * 255);
        int blue = (int) (color.getBlue() * 255);
        return new java.awt.Color(red, green, blue, alpha);
    }

    public String toString() {
        return "default";
    }

    public static Node slider(Consumer<Slider> configurator) {
        var slider = new Slider();
        configurator.accept(slider);
        var textField = new TextField();
        var formatter = new TextFormatter<>(new NumberStringConverter(), slider.getValue());
        textField.setTextFormatter(formatter);
        textField.setPrefWidth(DEFAULT_TEXTFIELD_PREF_WIDTH);
        formatter.valueProperty().bindBidirectional(slider.valueProperty());
        return new HBox(slider, textField);
    }
}
