package jp.gr.java_conf.alpius.pino.ui;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import jp.gr.java_conf.alpius.pino.brush.BrushManager;
import jp.gr.java_conf.alpius.pino.core.util.Disposable;
import jp.gr.java_conf.alpius.pino.core.util.Disposer;
import jp.gr.java_conf.alpius.pino.layer.LayerObject;
import jp.gr.java_conf.alpius.pino.project.ProjectManager;
import jp.gr.java_conf.alpius.pino.ui.canvas.CanvasBackground;
import jp.gr.java_conf.alpius.pino.ui.editor.BrushEditor;
import jp.gr.java_conf.alpius.pino.ui.editor.LayerEditor;
import org.controlsfx.control.StatusBar;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class Root implements Disposable {
    @FXML
    private BrushEditor brushEditor;
    @FXML
    private ToolBar toolBar;

    private final ToggleGroup toggleGroup = new ToggleGroup();

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

    @FXML
    private StatusBar statusBar;


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
                computeCanvasLocation();
                Platform.runLater(() -> Objects.requireNonNull(layer.getSelectionModel()).selectFirst());
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
        registerToggles();
        MultipleSelectionModel<LayerObject> selectionModel = layer.getSelectionModel();
        if (selectionModel != null) {
            selectionModel.selectedItemProperty().addListener((obs, old, newValue) -> updateEditor(newValue));
        }

        BrushManager brushManager = BrushManager.getInstance();
        brushManager.addSelectedBrushChangeListener(e -> brushEditor.setTarget(e.getNewBrush()));
        brushEditor.setTarget(brushManager.getSelectedBrush());
    }

    private void computeCanvasLocation() {
        var x = canvasPane.getWidth() / 2 - canvas.getWidth() / 2;
        var y = canvasPane.getHeight() / 2 - canvas.getHeight() / 2;
        canvas.setTranslateX(x);
        canvas.setTranslateY(y);
    }

    private void registerToggles() {
        toolBar.getItems()
                .forEach(it -> {
                    if (it instanceof Toggle) ((Toggle) it).setToggleGroup(toggleGroup);
                });
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

    public StatusBar getStatusBar() {
        return statusBar;
    }
}
