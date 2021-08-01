package jp.gr.java_conf.alpius.pino.ui.skin;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SkinBase;
import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;
import jp.gr.java_conf.alpius.commons.base.Validate;
import jp.gr.java_conf.alpius.pino.core.util.Disposable;
import jp.gr.java_conf.alpius.pino.core.util.Disposer;
import jp.gr.java_conf.alpius.pino.internal.graphics.Renderer;
import jp.gr.java_conf.alpius.pino.layer.LayerObject;
import jp.gr.java_conf.alpius.pino.ui.Layer;

import java.beans.PropertyChangeEvent;

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

    private static class DefaultCell extends ListCell<LayerObject> implements Disposable {
        private final Node graphic;
        private final ImageView imageView = new ImageView();
        private WritableImage image;
        private boolean requireInit = true;
        private final Text label = new Text();
        private final Text opacity = new Text();
        private final Text visible = new Text();
        private final Text rough = new Text();
        private LayerObject oldItem;
        private static double fitHeight = 50;

        public static void setFitHeight(double value) {
            Validate.require(value > 0);
            fitHeight = value;
        }

        public DefaultCell() {
            var parent = new HBox();
            var hbox = new HBox(opacity, visible, rough);
            parent.getChildren().addAll(imageView, new VBox(label, hbox));
            getStyleClass().add("layer-cell");
            graphic = parent;
        }

        private Disposable disposable = Disposer.newDisposable();

        @Override
        public void updateItem(LayerObject item, boolean empty) {
            super.updateItem(item, empty);
            if (item == null || empty) {
                setText(null);
                setGraphic(null);
                if (disposable != null) {
                    Disposer.dispose(disposable);
                    disposable = Disposer.newDisposable();
                    oldItem = null;
                }
            } else {
                if (oldItem != item) {
                    initializeCell(item);
                    oldItem = item;
                }
                setGraphic(graphic);
            }
        }

        private void initializeCell(LayerObject item) {
            image = Renderer.render(item, image, false);
            if (requireInit) {
                requireInit = false;
                imageView.setImage(image);
                imageView.setFitHeight(fitHeight);
                imageView.setFitWidth(image.getWidth() * fitHeight / image.getHeight());
            }

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
            item.addListener(this::receiveUpdate, this);
        }

        private void receiveUpdate(PropertyChangeEvent e) {
            switch (e.getPropertyName()) {
                case "opacity":
                    float opacityValue = (float) e.getNewValue();
                    int integer = (int) (opacityValue);
                    int floatingPoint = (((int) (opacityValue) * 10)) - integer * 10;
                    opacity.setText(integer + "." + floatingPoint + "%");
                    break;
                case "visible":
                    if ((boolean) e.getNewValue()) {
                        visible.setText("表示");
                    } else {
                        visible.setText("非表示");
                    }
                    break;
                case "rough":
                    if ((boolean) e.getNewValue()) {
                        rough.setText("下書きモード");
                    } else {
                        rough.setText(null);
                    }
                    break;
            }
        }

        @Override
        public void dispose() {
        }
    }

    @Override
    public void dispose() {
        Disposer.dispose(disposable);
    }
}
