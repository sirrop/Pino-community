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

import javafx.beans.property.DoubleProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import jp.gr.java_conf.alpius.pino.application.impl.Pino;
import jp.gr.java_conf.alpius.pino.gui.screen.options.cursor.DrawToolCursor;
import jp.gr.java_conf.alpius.pino.gui.screen.options.cursor.HandToolCursor;
import jp.gr.java_conf.alpius.pino.tool.Tool;
import jp.gr.java_conf.alpius.pino.tool.ToolChangeListener;
import jp.gr.java_conf.alpius.pino.tool.ToolManager;
import jp.gr.java_conf.alpius.pino.tool.plugin.DrawTool;
import jp.gr.java_conf.alpius.pino.tool.plugin.HandTool;

import java.awt.image.BufferedImage;
import java.util.*;
import java.util.stream.Collectors;

public class CursorOptions extends Option implements ToolChangeListener {
    public static CursorOptions create() {
        var res = new CursorOptions();
        res.register(DrawTool.class.getName(), DrawToolCursor.create());
        res.register(HandTool.class.getName(), HandToolCursor.create());
        return res;
    }

    public CursorOptions() {
        setTitle("カーソル");
        Pino.getApp().getService(ToolManager.class).addListener(this);
    }

    private final Map<String, Cursor> toolCursorMap = new HashMap<>();

    public void register(String name, Cursor cursor) {
        toolCursorMap.put(name, cursor);
    }

    @Override
    public void onApply() {

    }

    @Override
    public void onCancel() {

    }

    @Override
    public Node createNode() {
        var container = new VBox(5);
        container.getChildren().addAll(
                toolCursorMap.keySet()
                        .stream()
                        .sorted()
                        .map(Label::new)
                        .collect(Collectors.toList())
        );
        return container;
    }

    @Override
    public void changed(Tool oldTool, Tool newTool) {
        Optional.ofNullable(toolCursorMap.get(oldTool.getClass().getName()))
                .ifPresent(Cursor::disable);
        Optional.ofNullable(toolCursorMap.get(newTool.getClass().getName()))
                .ifPresent(Cursor::enable);
    }

    public static abstract class Pointer {
        public static final Pointer HORIZONTAL_RESIZE = fromFxCursor(javafx.scene.Cursor.H_RESIZE);
        public static final Pointer VERTICAL_RESIZE = fromFxCursor(javafx.scene.Cursor.V_RESIZE);

        public static final Pointer TOP_RESIZE = fromFxCursor(javafx.scene.Cursor.N_RESIZE);
        public static final Pointer RIGHT_RESIZE = fromFxCursor(javafx.scene.Cursor.E_RESIZE);
        public static final Pointer BOTTOM_RESIZE = fromFxCursor(javafx.scene.Cursor.S_RESIZE);
        public static final Pointer LEFT_RESIZE = fromFxCursor(javafx.scene.Cursor.W_RESIZE);

        public static final Pointer CLOSED_HAND = fromFxCursor(javafx.scene.Cursor.CLOSED_HAND);
        public static final Pointer OPEN_HAND = fromFxCursor(javafx.scene.Cursor.OPEN_HAND);
        public static final Pointer HAND = fromFxCursor(javafx.scene.Cursor.HAND);

        public static final Pointer CROSS_HAIR = fromFxCursor(javafx.scene.Cursor.CROSSHAIR);
        public static final Pointer DISAPPEAR = fromFxCursor(javafx.scene.Cursor.DISAPPEAR);
        public static final Pointer MOVE = fromFxCursor(javafx.scene.Cursor.MOVE);
        public static final Pointer NONE = fromFxCursor(javafx.scene.Cursor.MOVE);

        public static final Pointer TEXT = fromFxCursor(javafx.scene.Cursor.TEXT);
        public static final Pointer WAIT = fromFxCursor(javafx.scene.Cursor.WAIT);

        private static Pointer fromFxCursor(javafx.scene.Cursor cursor) {
            return new Pointer() {
                @Override
                javafx.scene.Cursor getCursor() {
                    return cursor;
                }

                @Override
                public Node createNode() {
                    return new StackPane();
                }
            };
        }

        abstract javafx.scene.Cursor getCursor();
        public abstract Node createNode();
    }

    public static class ImagePointer extends Pointer {
        private final javafx.scene.Cursor cursor;

        public ImagePointer(Image image, double x, double y) {
            cursor = new ImageCursor(image, x, y);
        }

        public ImagePointer(Image image) {
            this(image, 0, 0);
        }

        public ImagePointer(BufferedImage image) {
            this(image, 0, 0);
        }

        public ImagePointer(BufferedImage image, double x, double y) {
            this(SwingFXUtils.toFXImage(image, null), x, y);
        }

        @Override
        javafx.scene.Cursor getCursor() {
            return cursor;
        }

        @Override
        public Node createNode() {
            return new StackPane();
        }
    }

    public interface SubPointer {
        SubPointer NONE = StackPane::new;

        Node getSubPointer();
        default Node createNode() {
            return new StackPane();
        }
    }

    public static class Cursor {
        private final Pointer pointer;
        private final List<SubPointer> subPointers;
        private List<Node> subPointerCache;

        public Cursor(Pointer pointer, SubPointer... subPointers) {
            this.pointer = Objects.requireNonNull(pointer);
            this.subPointers = newCollection(subPointers);
        }

        private static List<SubPointer> newCollection(SubPointer[] pointers) {
            if (pointers == null) {
                return Collections.emptyList();
            }
            var list = new ArrayList<SubPointer>();
            for (var pointer: pointers) {
                list.add(Objects.requireNonNull(pointer));
            }
            return List.copyOf(list);
        }

        public Node createNode() {
            VBox container = new VBox(5);
            container.getChildren().add(pointer.createNode());
            container.getChildren().addAll(subPointers.stream().map(SubPointer::createNode).collect(Collectors.toList()));
            return container;
        }

        private EventHandler<MouseEvent> MOUSE_ENTERED;
        private EventHandler<MouseEvent> MOUSE_MOVED;
        private EventHandler<MouseEvent> MOUSE_DRAGGED;
        private EventHandler<MouseEvent> MOUSE_EXITED;

        public void enable() {
            subPointerCache = subPointers.stream()
                    .map(SubPointer::getSubPointer)
                    .collect(Collectors.toList());
            var pane = Pino.getApp()
                    .getWindow()
                    .getRootContainer()
                    .getCanvasPane();
            pane.setCursor(pointer.getCursor());
            MOUSE_ENTERED = e -> pane.getChildren().addAll(subPointerCache);
            MOUSE_DRAGGED = MOUSE_MOVED = e -> {
                for (var subPointer: subPointerCache) {
                    subPointer.toFront();
                    subPointer.setLayoutX(e.getX());
                    subPointer.setLayoutY(e.getY());
                }
            };
            MOUSE_EXITED = e -> pane.getChildren().removeAll(subPointerCache);
            pane.addEventHandler(MouseEvent.MOUSE_ENTERED, MOUSE_ENTERED);
            pane.addEventHandler(MouseEvent.MOUSE_MOVED, MOUSE_MOVED);
            pane.addEventHandler(MouseEvent.MOUSE_DRAGGED, MOUSE_DRAGGED);
            pane.addEventHandler(MouseEvent.MOUSE_EXITED, MOUSE_EXITED);
        }

        public void disable() {
            if (subPointerCache != null) {
                var pane = Pino.getApp()
                        .getWindow()
                        .getRootContainer()
                        .getCanvasPane();
                pane.getChildren().removeAll(subPointerCache);
                pane.removeEventHandler(MouseEvent.MOUSE_ENTERED, MOUSE_ENTERED);
                pane.removeEventHandler(MouseEvent.MOUSE_MOVED, MOUSE_MOVED);
                pane.removeEventHandler(MouseEvent.MOUSE_DRAGGED, MOUSE_DRAGGED);
                pane.removeEventHandler(MouseEvent.MOUSE_EXITED, MOUSE_EXITED);

                subPointerCache = null;
                MOUSE_ENTERED = null;
                MOUSE_MOVED = null;
                MOUSE_DRAGGED = null;
                MOUSE_EXITED = null;
            }
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
