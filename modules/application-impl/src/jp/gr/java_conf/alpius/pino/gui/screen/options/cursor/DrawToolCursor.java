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

package jp.gr.java_conf.alpius.pino.gui.screen.options.cursor;

import javafx.beans.property.DoubleProperty;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.converter.NumberStringConverter;
import jp.gr.java_conf.alpius.pino.application.impl.BrushManager;
import jp.gr.java_conf.alpius.pino.application.impl.Pino;
import jp.gr.java_conf.alpius.pino.disposable.Disposable;
import jp.gr.java_conf.alpius.pino.gui.screen.options.CursorOptions;
import jp.gr.java_conf.alpius.pino.util.Result;

import java.beans.PropertyChangeListener;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.IntConsumer;

public final class DrawToolCursor {
    private DrawToolCursor() {
        throw new AssertionError();
    }

    public static CursorOptions.Cursor create() {
        CursorOptions.Pointer[] pointer = new CursorOptions.Pointer[1];
        Result.tryToRun(() -> Files.newInputStream(Paths.get("resources", "images", "cursor.png")))
                .map(Image::new)
                .onSuccess(image -> pointer[0] = new CursorOptions.ImagePointer(image, image.getWidth() / 2, image.getHeight() / 2))
                .onFailed(image -> pointer[0] = CursorOptions.Pointer.NONE)
                .printStackTrace();

        var subPointer = new DrawToolSubPointer();

        return new CursorOptions.Cursor(pointer[0], subPointer) {
            @Override
            public void onApply() {
                subPointer.onApply();
            }

            @Override
            public void onCancel() {
                subPointer.onCancel();
            }
        };
    }

    private static class DrawToolSubPointer implements CursorOptions.SubPointer {
        private final BrushWidthIndicator brushWidthIndicator = new BrushWidthIndicator();

        // リスナーを除去するDisposable
        private Disposable listenerDisposable;

        private final PropertyChangeListener widthListener = e -> {
             if (e.getPropertyName().equals("width")) {
                brushWidthIndicator.setBrushWidth(Double.parseDouble(e.getNewValue().toString()) / 2);
            }
        };

        // canvasの拡大率
        private final DoubleProperty scale;

        public DrawToolSubPointer() {
            scale = Pino.getApp().getWindow().getRootContainer().getCanvas().scaleXProperty();

            // ブラシが変更されたときにリスナーを更新する
            IntConsumer updateListener = i -> {
                if (listenerDisposable != null) {
                    listenerDisposable.dispose();
                }

                var activeBrush = BrushManager.getInstance().getActiveModel().getActivatedItem();
                activeBrush.addListener(widthListener);

                listenerDisposable = () -> activeBrush.removeListener(widthListener);
            };
            BrushManager.getInstance()
                    .getActiveModel()
                    .addListener(updateListener);
            updateListener.accept(BrushManager.getInstance().getActiveModel().getActivatedIndex());
            var brush = BrushManager.getInstance().getActiveModel().getActivatedItem();

            for (var desc: brush.getUnmodifiablePropertyList()) {
                if (desc.getName().equals("width")) {
                    Result.tryToRun(desc::getReadMethod)
                          .map(getter -> getter.invoke(brush))
                          .map(value -> Double.parseDouble(value.toString()) / 2)
                          .map(value -> value * scale.get())
                          .onSuccess(brushWidthIndicator::setBrushWidth)
                          .printStackTrace();
                }
            }
            brushWidthIndicator.setStroke(Color.GRAY);

            scale.addListener((observable, oldValue, newValue) -> {
                var b = BrushManager.getInstance().getActiveModel().getActivatedItem();
                for (var desc: b.getUnmodifiablePropertyList()) {
                    if (desc.getName().equals("width")) {
                        Result.tryToRun(desc::getReadMethod)
                                .map(getter -> getter.invoke(b))
                                .map(value -> Double.parseDouble(value.toString()) / 2)
                                .map(value -> value * scale.get())
                                .onSuccess(brushWidthIndicator::setBrushWidth)
                                .printStackTrace();
                    }
                }
            });
        }

        @Override
        public Node getSubPointer() {
            return brushWidthIndicator;
        }

        private boolean widthDirty = false;
        private boolean colorDirty = false;

        private float width;
        private Color color;

        private void markWidthDirty(float value) {
            widthDirty = true;
            width = value;
        }

        private void markColorDirty(Color value) {
            colorDirty = true;
            color = value;
        }

        public void onApply() {
            if (widthDirty) {
                brushWidthIndicator.setStrokeWidth(width);
            }
            if (colorDirty) {
                brushWidthIndicator.setStroke(color);
            }
        }

        public void onCancel() {

        }

        @Override
        public Node createNode() {
            VBox container = new VBox(5);

            Label strokeLabel = new Label("ストロークの幅");
            TextField field = new TextField();
            TextFormatter<Number> formatter = new TextFormatter<>(new NumberStringConverter(), brushWidthIndicator.getStrokeWidth());
            formatter.valueProperty().addListener((observable, oldValue, newValue) -> markWidthDirty(newValue.floatValue()));
            field.setTextFormatter(formatter);

            Label strokeColor = new Label("ストロークの色");

            ColorPicker picker = new ColorPicker();
            picker.setValue(brushWidthIndicator.getStroke());
            picker.valueProperty().addListener((observable, oldValue, newValue) -> markColorDirty(newValue));

            container.getChildren().addAll(new HBox(strokeLabel, field), new HBox(strokeColor, picker));

            return container;
        }
    }

    private static final class BrushWidthIndicator extends Region {
        private final Circle circle = new Circle();

        public void setBrushWidth(double value) {
            brushWidthProperty().set(value);
        }

        public double getBrushWidth() {
            return brushWidthProperty().get();
        }

        public DoubleProperty brushWidthProperty() {
            return circle.radiusProperty();
        }

        public void setStroke(Color fill) {
            circle.setStroke(fill);
        }

        public Color getStroke() {
            return (Color) circle.getStroke();
        }

        public void setStrokeWidth(double value) {
            circle.setStrokeWidth(value);
        }

        public double getStrokeWidth() {
            return circle.getStrokeWidth();
        }

        BrushWidthIndicator() {
            circle.setFill(Color.TRANSPARENT);
            circle.setStroke(Color.BLACK);
            getChildren().add(circle);
        }
    }
}