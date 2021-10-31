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
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import jp.gr.java_conf.alpius.pino.application.impl.BrushManager;
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
        Result.tryToRun(() -> Files.newInputStream(Paths.get("resources", "cursor.png")))
                .map(Image::new)
                .onSuccess(image -> pointer[0] = new CursorOptions.ImagePointer(image, image.getWidth() / 2, image.getHeight() / 2))
                .onFailed(image -> pointer[0] = CursorOptions.Pointer.NONE)
                .printStackTrace();

        return new CursorOptions.Cursor(pointer[0], new DrawToolSubPointer());
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

        public DrawToolSubPointer() {
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
                          .onSuccess(brushWidthIndicator::setBrushWidth)
                          .printStackTrace();
                }
            }
            brushWidthIndicator.setStroke(Color.GRAY);
        }

        @Override
        public Node getSubPointer() {
            return brushWidthIndicator;
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

        public void setStroke(Paint fill) {
            circle.setStroke(fill);
        }

        BrushWidthIndicator() {
            circle.setFill(Color.TRANSPARENT);
            circle.setStroke(Color.BLACK);
            getChildren().add(circle);
        }
    }
}