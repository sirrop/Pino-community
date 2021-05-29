package com.branc.pino.ui.skin;

import com.branc.pino.core.util.Disposable;
import com.branc.pino.core.util.Disposer;
import com.branc.pino.paint.layer.LayerObject;
import com.branc.pino.ui.Layer;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.embed.swing.SwingNode;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SkinBase;
import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Callback;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class LayerSkin extends SkinBase<Layer> {
    private final Disposable disposable = Disposer.newDisposable();

    public LayerSkin(Layer control) {
        super(control);
        initialize();
    }

    private void initialize() {
        ToolBar toolBar = new ToolBar();
        toolBar.getItems().setAll(getSkinnable().getButtons());
        getSkinnable().getButtons().addListener((Observable obs) -> toolBar.getItems().setAll(getSkinnable().getButtons()));

        ListView<LayerObject> view = new ListView<>();
        VBox.setVgrow(view, Priority.ALWAYS);
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
        getChildren().setAll(new VBox(toolBar, view));

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
        private final Text label = new Text();
        private final Text opacity = new Text();
        private final Text visible = new Text();
        private final Text rough = new Text();

        public DefaultCell() {
            var parent = new HBox();
            var hbox = new HBox(opacity, visible, rough);
            parent.getChildren().addAll(new VBox(label, hbox));
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
