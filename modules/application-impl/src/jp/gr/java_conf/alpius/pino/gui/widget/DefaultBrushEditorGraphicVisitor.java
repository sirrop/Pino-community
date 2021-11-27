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
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.converter.NumberStringConverter;
import jp.gr.java_conf.alpius.pino.application.impl.GraphicManager;
import jp.gr.java_conf.alpius.pino.graphics.brush.*;

import java.util.function.Consumer;

public class DefaultBrushEditorGraphicVisitor implements GraphicManager.BrushEditorGraphicVisitor {
    private static final double DEFAULT_LABEL_PREF_WIDTH = 60;
    private static final double DEFAULT_TEXTFIELD_PREF_WIDTH = 60;

    public Node visit(Brush brush) {
        VBox container = new VBox(3);
        container.setPadding(new Insets(5));

        var name = new Label(brush.getName());
        container.getChildren().add(name);

        if (brush instanceof Pencil pencil) {
            var width = slider(slider -> {
                slider.setBlockIncrement(1);
                slider.setValue(pencil.getWidth());
                slider.valueProperty().addListener((observable, oldValue, newValue) -> pencil.setWidth(newValue.floatValue()));
            });

            var opacity = slider(slider -> {
                slider.setMin(0);
                slider.setMax(100);
                slider.setBlockIncrement(1);
                slider.setValue(pencil.getOpacity() * 100);
                slider.valueProperty().addListener((observable, oldValue, newValue) -> pencil.setOpacity(newValue.floatValue() / 100));
            });

            var colorPicker = new ColorPicker();
            colorPicker.setValue(asFxColor(pencil.getColor()));
            colorPicker.valueProperty().addListener((observable, oldValue, newValue) -> pencil.setColor(asAwtColor(newValue)));
            container.getChildren().addAll(labeled("幅", width), labeled("不透明度", opacity), colorPicker);
        } else if (brush instanceof Eraser eraser) {
            var width = slider(it -> {
                it.setBlockIncrement(1);
                it.setValue(eraser.getWidth());
                it.valueProperty().addListener((observable, oldValue, newValue) -> eraser.setWidth(newValue.floatValue()));
            });

            var opacity = slider(it -> {
                it.setMin(0);
                it.setMax(100);
                it.setBlockIncrement(1);
                it.setValue(eraser.getOpacity() * 100);
                it.valueProperty().addListener((observable, oldValue, newValue) -> eraser.setOpacity(newValue.floatValue() / 100));
            });

            container.getChildren().addAll(labeled("幅", width), labeled("不透明度", opacity));
        } else if (brush instanceof Marker watercolor) {
            var width = slider(slider -> {
                slider.setBlockIncrement(1);
                slider.setValue(watercolor.getWidth());
                slider.valueProperty().addListener((observable, oldValue, newValue) -> watercolor.setWidth(newValue.floatValue()));
            });

            var opacity = slider(slider -> {
                slider.setMin(0);
                slider.setMax(100);
                slider.setBlockIncrement(1);
                slider.setValue(watercolor.getOpacity() * 100);
                slider.valueProperty().addListener((observable, oldValue, newValue) -> watercolor.setOpacity(newValue.floatValue() / 100));
            });

            var colorPicker = new ColorPicker();
            colorPicker.setValue(asFxColor(watercolor.getColor()));
            colorPicker.valueProperty().addListener((observable, oldValue, newValue) -> watercolor.setColor(asAwtColor(newValue)));
            container.getChildren().addAll(labeled("幅", width), labeled("不透明度", opacity), colorPicker);
        } else if (brush instanceof MeanBlur blur) {
            var width = slider(it -> {
                it.setBlockIncrement(1);
                it.setValue(blur.getWidth());
                it.valueProperty().addListener((observable, oldValue, newValue) -> blur.setWidth(newValue.floatValue()));
            });
            var kernelWidth = slider(it -> {
                it.setMin(0);
                it.setMax(100);
                it.setBlockIncrement(1);
                it.setValue(blur.getKernelWidth());
                it.valueProperty().addListener(((observable, oldValue, newValue) -> blur.setKernelWidth(Math.round(newValue.floatValue()))));
            }, 0);
            var kernelHeight = slider(it -> {
                it.setMin(0);
                it.setMax(100);
                it.setBlockIncrement(1);
                it.setValue(blur.getKernelHeight());
                it.valueProperty().addListener(((observable, oldValue, newValue) -> blur.setKernelHeight(Math.round(newValue.floatValue()))));
            });
            container.getChildren().addAll(labeled("幅", width), labeled("ぼかしの強さX", kernelWidth), labeled("ぼかしの強さY", kernelHeight));
        } else if (brush instanceof GaussianBlur blur) {
            var width = slider(it -> {
                it.setBlockIncrement(1);
                it.setValue(blur.getWidth());
                it.valueProperty().addListener((observable, oldValue, newValue) -> blur.setWidth(newValue.floatValue()));
            });
            var kernelWidth = slider(it -> {
                it.setMin(1);
                it.setMax(99);
                it.setBlockIncrement(2);
                it.setValue(blur.getKernelWidth());
                it.valueProperty().addListener(((observable, oldValue, newValue) -> blur.setKernelWidth(Math.round(newValue.floatValue()))));
            }, 0);
            var kernelHeight = slider(it -> {
                it.setMin(1);
                it.setMax(99);
                it.setBlockIncrement(2);
                it.setValue(blur.getKernelHeight());
                it.valueProperty().addListener(((observable, oldValue, newValue) -> blur.setKernelHeight(Math.round(newValue.floatValue()))));
            }, 0);
            var deviation = slider(it -> {
                it.setMin(0.01);
                it.setMax(5);
                it.setBlockIncrement(0.01);
                it.setValue(blur.getDeviation());
                it.valueProperty().addListener((observable, oldValue, newValue) -> blur.setDeviation(newValue.doubleValue()));
            }, 2);
            container.getChildren().addAll(labeled("幅", width), labeled("ぼかしの強さX", kernelWidth), labeled("ぼかしの強さY", kernelHeight), labeled("偏差", deviation));
        } else if (brush instanceof Airbrush basic) {
            var width = slider(slider -> {
                slider.setBlockIncrement(1);
                slider.setValue(basic.getWidth());
                slider.valueProperty().addListener((observable, oldValue, newValue) -> basic.setWidth(newValue.floatValue()));
            });

            var opacity = slider(slider -> {
                slider.setMin(0);
                slider.setMax(100);
                slider.setBlockIncrement(1);
                slider.setValue(basic.getOpacity() * 100);
                slider.valueProperty().addListener((observable, oldValue, newValue) -> basic.setOpacity(newValue.floatValue() / 100));
            });

            var colorPicker = new ColorPicker();
            colorPicker.setValue(asFxColor(basic.getColor()));
            colorPicker.valueProperty().addListener((observable, oldValue, newValue) -> basic.setColor(asAwtColor(newValue)));
            container.getChildren().addAll(labeled("幅", width), labeled("不透明度", opacity) , colorPicker);
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
                result = drop(result, getNumDigitsUnderPoint(result) - mug);
                if (mug == 0 && result.lastIndexOf('.') != -1) {
                    result = drop(result, 1);
                }
                return result;
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
}
