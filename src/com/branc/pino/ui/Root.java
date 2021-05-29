package com.branc.pino.ui;

import com.branc.pino.core.util.Disposable;
import com.branc.pino.core.util.Disposer;
import com.branc.pino.paint.brush.BrushManager;
import com.branc.pino.paint.layer.LayerObject;
import com.branc.pino.project.ProjectManager;
import com.branc.pino.ui.canvas.CanvasBackground;
import com.branc.pino.ui.editor.BrushEditor;
import com.branc.pino.ui.editor.EditorGroup;
import com.branc.pino.ui.editor.LayerEditor;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Root implements Disposable {
    @FXML
    private BrushEditor brushEditor;
    @FXML
    private ToolBar toolBar;

    @FXML
    private LayerEditor layerEditor;

    @FXML
    private Layer layer;

    @FXML
    private Pane canvasPane;

    @FXML
    private MenuBar menuBar;

    @FXML
    private Pane contentPane;

    @FXML
    private Canvas canvas;

    private CanvasBackground background;
    private final Disposable lastDisposable = Disposer.newDisposable();

    public static FXMLLoader load(Path path) throws IOException {
        var loader = new FXMLLoader();
        loader.load(Files.newInputStream(path));
        return loader;
    }

    public static FXMLLoader load() throws IOException {
        var loader = new FXMLLoader();
        loader.load(Root.class.getResourceAsStream("DefaultRoot.fxml"));
        return loader;
    }

    @FXML
    private void initialize() {
        background = CanvasBackground.bind(canvas);
        canvasPane.getChildren().add(canvasPane.getChildren().indexOf(canvas), (Node) background);
        ProjectManager.getInstance().addListener((oldValue, newValue) -> {
            if (newValue != null) {
                ((ObservableList<LayerObject>) newValue.getLayer()).addListener((Observable obs) -> layer.getItems().setAll(newValue.getLayer()));
                layer.getItems().setAll(newValue.getLayer());
                layer.getSelectionModel().selectFirst();
            } else {
                layer.getItems().clear();
                canvas.setWidth(0);
                canvas.setHeight(0);
                canvas.setTranslateX(0);
                canvas.setTranslateY(0);
                canvas.setScaleX(1);
                canvas.setScaleY(1);
                canvas.setRotate(0);
            }
        }, lastDisposable);
        layer.selectionModelProperty().addListener((obs, old, newValue) -> {
            if (newValue != null) {
                newValue.selectedItemProperty().addListener((obs2, old2, newValue2) -> updateEditor(newValue2));
            }
        });
        MultipleSelectionModel<LayerObject> selectionModel = layer.getSelectionModel();
        if (selectionModel != null) {
            selectionModel.selectedItemProperty().addListener((obs, old, newValue) -> updateEditor(newValue));
        }

        BrushManager brushManager = BrushManager.getInstance();
        brushManager.addSelectedBrushChangeListener(e -> brushEditor.setTarget(e.getNewBrush()));
        brushEditor.setTarget(brushManager.getSelectedBrush());
    }

    private void updateEditor(LayerObject object) {
        layerEditor.setTarget(object);
    }


    public MenuBar getMenuBar() {
        return menuBar;
    }

    public Pane getContentPane() {
        return contentPane;
    }

    public CanvasBackground getCanvasBackground() {
        return background;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public Pane getCanvasPane() {
        return canvasPane;
    }

    public Layer getLayer() {
        return layer;
    }

    public LayerEditor getLayerEditor() {
        return layerEditor;
    }

    public BrushEditor getBrushEditor() {
        return brushEditor;
    }

    public ToolBar getToolBar() {
        return toolBar;
    }

    @Override
    public void dispose() {
        Disposer.dispose(lastDisposable);
    }
}
