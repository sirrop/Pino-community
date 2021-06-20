package jp.gr.java_conf.alpius.pino.ui;

import jp.gr.java_conf.alpius.pino.layer.LayerObject;
import jp.gr.java_conf.alpius.pino.project.Project;
import jp.gr.java_conf.alpius.pino.ui.skin.LayerSkin;
import javafx.beans.DefaultProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.util.Callback;

@DefaultProperty("buttons")
public class Layer extends Control {

    public Layer() {
        this(null);
    }

    public Layer(Project project) {
        if (project != null) {
            items.set(FXCollections.observableList(project.getLayer()));
        }

        getStyleClass().setAll("layer");
    }

    private final ListProperty<LayerObject> items = new SimpleListProperty<>(this, "items", FXCollections.observableArrayList());

    public final ObservableList<LayerObject> getItems() {
        return items.get();
    }

    private ObjectProperty<Node> placeholder;

    public final ObjectProperty<Node> placeholderProperty() {
        if (placeholder == null) {
            placeholder = new SimpleObjectProperty<>(this, "placeholder");
        }
        return placeholder;
    }

    public final void setPlaceholder(Node value) {
        placeholderProperty().set(value);
    }

    public final Node getPlaceholder() {
        return placeholder == null ? null : placeholder.get();
    }

    private ObjectProperty<Callback<Layer, ListCell<LayerObject>>> cellFactory;

    public final ObjectProperty<Callback<Layer, ListCell<LayerObject>>> cellFactoryProperty() {
        if (cellFactory == null) {
            cellFactory = new SimpleObjectProperty<>(this, "cellFactory");
        }
        return cellFactory;
    }

    public final void setCellFactory(Callback<Layer, ListCell<LayerObject>> cellFactory) {
        cellFactoryProperty().set(cellFactory);
    }

    public final Callback<Layer, ListCell<LayerObject>> getCellFactory() {
        return cellFactory == null ? null : cellFactory.get();
    }

    private ObjectProperty<MultipleSelectionModel<LayerObject>> selectionModel;

    public final ObjectProperty<MultipleSelectionModel<LayerObject>> selectionModelProperty() {
        if (selectionModel == null) {
            selectionModel = new SimpleObjectProperty<>(this, "selectionModel");
        }
        return selectionModel;
    }

    public final MultipleSelectionModel<LayerObject> getSelectionModel() {
        return selectionModel == null ? null : selectionModel.get();
    }

    public final void setSelectionModel(MultipleSelectionModel<LayerObject> selectionModel) {
        selectionModelProperty().set(selectionModel);
    }


    private ObjectProperty<FocusModel<LayerObject>> focusModel;

    public final ObjectProperty<FocusModel<LayerObject>> focusModelProperty() {
        if (focusModel == null) {
            focusModel = new SimpleObjectProperty<>(this, "focusModel");
        }
        return focusModel;
    }

    public final FocusModel<LayerObject> getFocusModel() {
        return focusModel == null ? null : focusModel.get();
    }

    public final void setFocusModel(FocusModel<LayerObject> value) {
        focusModelProperty().set(value);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new LayerSkin(this);
    }


    private final ObservableList<Button> buttons = FXCollections.observableArrayList();

    public final ObservableList<Button> getButtons() {
        return buttons;
    }
}
