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

package jp.gr.java_conf.alpius.pino.tool.plugin;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import jp.gr.java_conf.alpius.pino.application.impl.Pino;
import jp.gr.java_conf.alpius.pino.input.MouseEvent;
import jp.gr.java_conf.alpius.pino.project.impl.SelectionManager;
import jp.gr.java_conf.alpius.pino.tool.Tool;
import jp.gr.java_conf.alpius.pino.window.impl.JFxWindow;
import jp.gr.java_conf.alpius.pino.window.impl.RootContainer;

import java.awt.*;
import java.util.Arrays;
import java.util.Optional;

/**
 * 投げ縄ツールです。
 */
public final class LassoTool implements Tool {
    private final IntArrayList xPts = new IntArrayList();
    private final IntArrayList yPts = new IntArrayList();

    @Override
    public void mousePressed(MouseEvent e) {
        Optional.ofNullable(Pino.getApp().getProject())
                .ifPresent(it -> it.getService(SelectionManager.class).set(null));
        xPts.add(round(e.getX()));
        yPts.add(round(e.getY()));
        addIndicator();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        xPts.add(round(e.getX()));
        yPts.add(round(e.getY()));
        updateIndicator();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        xPts.add(round(e.getX()));
        yPts.add(round(e.getY()));
        removeIndicator();
        Optional.ofNullable(Pino.getApp().getProject())
                .ifPresent(it -> it.getService(SelectionManager.class)
                                    .updateAndGet(shape -> new Polygon(xPts.toArray(), yPts.toArray(), xPts.size())));
        xPts.clear();
        yPts.clear();
    }

    private Path indicator;

    private void addIndicator() {
        indicator = new Path();
        var canvas = getCanvas();
        indicator.layoutXProperty().bind(canvas.layoutYProperty());
        indicator.layoutYProperty().bind(canvas.layoutYProperty());
        indicator.scaleXProperty().bind(canvas.scaleXProperty());
        indicator.scaleYProperty().bind(canvas.scaleYProperty());
        indicator.translateXProperty().bind(canvas.translateXProperty());
        indicator.translateYProperty().bind(canvas.translateYProperty());
        indicator.rotateProperty().bind(canvas.rotateProperty());

        // FIXME : これらのパラメータをユーザーが変更できるようにしてください
        indicator.getStrokeDashArray().setAll(3d, 5d);
        indicator.setStrokeWidth(1);
        indicator.setStroke(Color.web("#222"));

        indicator.getElements().add(new MoveTo(xPts.getLast(), yPts.getLast()));

        getCanvasPane().getChildren().add(indicator);
    }

    private void updateIndicator() {
        indicator.getElements().add(new LineTo(xPts.getLast(), yPts.getLast()));
    }

    private void removeIndicator() {
        indicator.layoutXProperty().unbind();
        indicator.layoutYProperty().unbind();
        indicator.scaleXProperty().unbind();
        indicator.scaleYProperty().unbind();
        indicator.translateXProperty().unbind();
        indicator.translateYProperty().unbind();
        indicator.rotateProperty().unbind();
        getCanvasPane().getChildren().remove(indicator);
        indicator = null;
    }

    private static RootContainer getRootContainer() {
        return ((JFxWindow) Pino.getApp().getWindow()).getRootContainer();
    }

    private static Pane getCanvasPane() {
        return getRootContainer().getCanvasPane();
    }

    private static Canvas getCanvas() {
        return getRootContainer().getCanvas();
    }

    private static int round(double value) {
        return (int) (value + 0.5);
    }
    
    private static class IntArrayList {
        private int[] data;
        private int size = 0;

        public IntArrayList() {
            this(10);
        }

        public IntArrayList(int initialCapacity) {
            data = new int[initialCapacity];
        }

        public int size() {
            return size;
        }

        public void add(int value) {
            if (data.length == size) {
                var newArray = new int[size + 1];
                System.arraycopy(data, 0, newArray, 0, size);
                data = newArray;
            }
            data[size] = value;
            size += 1;
        }

        public void clear() {
            data = new int[10];
            size = 0;
        }

        public int getLast() {
            int index;
            if (size == 0) {
                index = 0;
            } else {
                index = size - 1;
            }
            return data[index];
        }

        public int[] toArray() {
            if (data.length == size) {
                return data.clone();
            }
            var res = new int[size];
            System.arraycopy(data, 0, res, 0, size);
            return res;
        }

        public String toString() {
            return Arrays.toString(data);
        }
    }
}
