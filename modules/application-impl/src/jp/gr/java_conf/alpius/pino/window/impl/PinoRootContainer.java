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

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ToolBar;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import jp.gr.java_conf.alpius.pino.application.impl.Pino;
import jp.gr.java_conf.alpius.pino.application.impl.ProjectManager;
import jp.gr.java_conf.alpius.pino.graphics.brush.Brush;
import jp.gr.java_conf.alpius.pino.graphics.layer.LayerObject;
import jp.gr.java_conf.alpius.pino.project.impl.SelectionManager;

import java.io.IOException;

public class PinoRootContainer implements RootContainer {
    @FXML
    private BrushEditor brushEditor;
    @FXML
    private ListView<Brush> brushView;
    @FXML
    private LayerEditor layerEditor;
    @FXML
    private ListView<LayerObject> layerView;
    @FXML
    private Canvas canvas;
    private Background background;
    @FXML
    private Pane canvasPane;
    private SelectionIndicator indicator;

    @FXML
    private HBox contentPane;
    @FXML
    private MenuBar menuBar;
    @FXML
    private ToolBar toolBar;

    public static RootContainer load() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.load(PinoRootContainer.class.getResourceAsStream("RootContainer.fxml"));
        return loader.getController();
    }

    @FXML
    private void initialize() {
        background = new Background(canvas);
        canvasPane.getChildren().add(0, background);
        layerEditor.setContextMenu(MenuManager.getInstance().getLayerEditorMenu());
        layerView.setCellFactory(listView -> new LayerCell());
        brushEditor.setContextMenu(MenuManager.getInstance().getBrushEditorMenu());
        brushView.setCellFactory(listView -> new BrushCell());
        Pino.getApp().getService(ProjectManager.class)
                .addOnChanged(project -> {
                    if (project != null) {
                        indicator = new SelectionIndicator(project.getService(SelectionManager.class));
                        indicator.prefWidthProperty().bind(canvasPane.widthProperty());
                        indicator.prefHeightProperty().bind(canvasPane.heightProperty());
                        canvasPane.getChildren().add(indicator);
                    } else {
                        if (indicator != null) {
                            canvasPane.getChildren().remove(indicator);
                        }
                    }
                });
    }

    public void setBackground(Paint fill) {
        if (fill == null) {
            fill = Background.DEFAULT_FILL;
        }
        background.setFill(fill);
    }

    public Paint getBackground() {
        return background.getFill();
    }

    @FXML
    private Parent content;

    @Override
    public Parent getContent() {
        return content;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    @Override
    public Pane getCanvasPane() {
        return canvasPane;
    }

    @Override
    public LayerEditor getLayerEditor() {
        return layerEditor;
    }

    @Override
    public BrushEditor getBrushEditor() {
        return brushEditor;
    }

    @Override
    public ListView<LayerObject> getLayerView() {
        return layerView;
    }

    @Override
    public ListView<Brush> getBrushView() {
        return brushView;
    }

    private static final class Background extends Rectangle {
        private static final Paint DEFAULT_FILL;

        static {
            var image = new WritableImage(40, 40);
            var gray = Color.LIGHTGRAY;
            var white = Color.WHITE;
            fillRect(image, 0, 0, 20, 20, gray);
            fillRect(image, 20, 20, 20, 20, gray);
            fillRect(image, 20, 0, 20, 20, white);
            fillRect(image, 0, 20, 20, 20, white);
            DEFAULT_FILL = new ImagePattern(image, 0, 0, 40, 40, false);
        }

        public Background(Canvas canvas) {
            layoutXProperty().bind(canvas.layoutXProperty());
            layoutYProperty().bind(canvas.layoutYProperty());
            widthProperty().bind(canvas.widthProperty());
            heightProperty().bind(canvas.heightProperty());
            scaleXProperty().bind(canvas.scaleXProperty());
            scaleYProperty().bind(canvas.scaleYProperty());
            translateXProperty().bind(canvas.translateXProperty());
            translateYProperty().bind(canvas.translateYProperty());
            setFill(DEFAULT_FILL);
        }

        private static void fillRect(WritableImage image, int x, int y, int w, int h, Color c) {
            for (int i = 0; i < h; ++i) {
                for (int j = 0; j < w; ++j) {
                    image.getPixelWriter().setColor(x + j, y + i, c);
                }
            }
        }
    }
}
