package jp.gr.java_conf.alpius.pino.ui.skin;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SkinBase;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import jp.gr.java_conf.alpius.pino.core.annotaion.Beta;
import jp.gr.java_conf.alpius.pino.core.util.Disposable;
import jp.gr.java_conf.alpius.pino.core.util.Disposer;
import jp.gr.java_conf.alpius.pino.layer.LayerObject;
import jp.gr.java_conf.alpius.pino.ui.Layer;
import jp.gr.java_conf.alpius.pino.ui.LayerCell;

@Beta
public class LayerSkin extends SkinBase<Layer> {
    private final Disposable disposable = Disposer.newDisposable();

    public LayerSkin(Layer control, BooleanProperty dirty) {
        super(control);
        initialize(dirty);
    }

    private void initialize(BooleanProperty dirty) {
        ToolBar toolBar = new ToolBar();
        toolBar.getItems().setAll(getSkinnable().getButtons());
        getSkinnable().getButtons().addListener((Observable obs) -> toolBar.getItems().setAll(getSkinnable().getButtons()));

        ListView<LayerObject> view = new ListView<>();
        StackPane placeholder = new StackPane();
        placeholder.getStyleClass().add("placeholder");
        view.setPlaceholder(placeholder);
        dirty.addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                dirty.set(false);
                view.refresh();
            }
        });
        VBox.setVgrow(view, Priority.ALWAYS);
        getSkinnable().selectionModelProperty().bindBidirectional(view.selectionModelProperty());
        getSkinnable().focusModelProperty().bindBidirectional(view.focusModelProperty());
        getSkinnable().placeholderProperty().bindBidirectional(view.placeholderProperty());

        InvalidationListener itemsListener = (Observable obs) -> view.setItems(getSkinnable().getItems());
        getSkinnable().getItems().addListener(itemsListener);
        view.setCellFactory(listView -> new LayerCell());

        ChangeListener<Callback<Layer, ListCell<LayerObject>>> changeListener = (obs, old, newValue) -> {
            if (newValue == null) {
                view.setCellFactory(listView -> new LayerCell());
            } else {
                view.setCellFactory(listView -> newValue.call(getSkinnable()));
            }
        };
        getSkinnable().cellFactoryProperty().addListener(changeListener);
        getChildren().setAll(new VBox(toolBar, view));

        Disposer.registerDisposable(disposable, () -> {
            getSkinnable().selectionModelProperty().unbindBidirectional(view.selectionModelProperty());
            getSkinnable().focusModelProperty().unbindBidirectional(view.focusModelProperty());
            getSkinnable().placeholderProperty().unbindBidirectional(view.placeholderProperty());
            getSkinnable().getItems().removeListener(itemsListener);
            getSkinnable().cellFactoryProperty().removeListener(changeListener);
        });
    }

    @Override
    public void dispose() {
        Disposer.dispose(disposable);
    }
}
