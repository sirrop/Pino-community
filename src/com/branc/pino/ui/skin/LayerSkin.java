package com.branc.pino.ui.skin;

import com.branc.pino.core.util.Disposable;
import com.branc.pino.core.util.Disposer;
import com.branc.pino.paint.layer.LayerObject;
import com.branc.pino.ui.Layer;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SkinBase;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class LayerSkin extends SkinBase<Layer> {
    private final Disposable disposable = Disposer.newDisposable();

    public LayerSkin(Layer control) {
        super(control);
        initialize();
    }

    private void initialize() {
        ListView<LayerObject> view = new ListView<>();
        getSkinnable().selectionModelProperty().bindBidirectional(view.selectionModelProperty());
        getSkinnable().focusModelProperty().bindBidirectional(view.focusModelProperty());
        getSkinnable().placeholderProperty().bindBidirectional(view.placeholderProperty());

        InvalidationListener itemsListener = (Observable obs) -> view.setItems(getSkinnable().getItems());
        getSkinnable().getItems().addListener(itemsListener);
        view.setCellFactory(listView -> new DefaultCell());

        ChangeListener<Callback<Layer, ListCell<LayerObject>>> changeListener = (obs, old, newValue) -> {
            if (newValue == null) {
                view.setCellFactory(listView -> new DefaultCell());
            } else {
                view.setCellFactory(listView -> newValue.call(getSkinnable()));
            }
        };
        getSkinnable().cellFactoryProperty().addListener(changeListener);
        getChildren().setAll(view);

        Disposer.registerDisposable(disposable, () -> {
            getSkinnable().selectionModelProperty().unbindBidirectional(view.selectionModelProperty());
            getSkinnable().focusModelProperty().unbindBidirectional(view.focusModelProperty());
            getSkinnable().placeholderProperty().unbindBidirectional(view.placeholderProperty());
            getSkinnable().getItems().removeListener(itemsListener);
            getSkinnable().cellFactoryProperty().removeListener(changeListener);
        });
    }

    private static class DefaultCell extends ListCell<LayerObject> implements PropertyChangeListener {
        private final Node graphic;
        private final ImageView image = new ImageView();
        private final Text label = new Text();
        private final Text opacity = new Text();
        private final Text visible = new Text();
        private final Text rough = new Text();

        public DefaultCell() {
            var parent = new HBox();
            image.setFitHeight(40);
            var hbox = new HBox(opacity, visible, rough);
            parent.getChildren().addAll(image, new VBox(label, hbox));
            graphic = parent;
        }

        private Disposable disposable;

        @Override
        public void updateItem(LayerObject item, boolean empty) {
            super.updateItem(item, empty);
            if (item == null || empty) {
                setText(null);
                setGraphic(null);
                if (disposable != null) {
                    Disposer.dispose(disposable);
                    disposable = null;
                }
            } else {
                if (disposable == null) {
                    disposable = Disposer.newDisposable();
                    item.addListener(this);
                    initializeCell(item);
                }
                setGraphic(graphic);
            }
        }

        private void initializeCell(LayerObject item) {
            label.setText(item.getName());
            int integer = (int) (item.getOpacity());
            int floatingPoint = ((int) (item.getOpacity() * 10)) - integer * 10;
            opacity.setText(integer + "." + floatingPoint + "%");
            if (item.isVisible()) {
                visible.setText("表示");
            } else {
                visible.setText("非表示");
            }
            if (item.isRough()) {
                rough.setText("下書きモード");
            } else {
                rough.setText(null);
            }
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            switch (evt.getPropertyName()) {
                case LayerObject.NAME: {
                    label.setText((String) evt.getNewValue());
                    break;
                }
                case LayerObject.OPACITY: {
                    int integer = (int) (((float) evt.getNewValue()));
                    int floatingPoint = ((int) ((float) evt.getNewValue()) * 10) - integer * 10;
                    opacity.setText(integer + "." + floatingPoint + "%");
                    break;
                }
                case LayerObject.VISIBLE: {
                    if ((boolean) evt.getNewValue()) {
                        visible.setText("表示");
                    } else {
                        visible.setText("非表示");
                    }
                    break;
                }
                case LayerObject.ROUGH: {
                    if ((boolean) evt.getNewValue()) {
                        rough.setText("下書きモード");
                    } else {
                        rough.setText(null);
                    }
                }
            }
        }
    }

    @Override
    public void dispose() {
        Disposer.dispose(disposable);
    }
}
