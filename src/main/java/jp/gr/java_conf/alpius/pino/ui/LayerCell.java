package jp.gr.java_conf.alpius.pino.ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import jp.gr.java_conf.alpius.commons.base.Validate;
import jp.gr.java_conf.alpius.pino.core.util.Disposable;
import jp.gr.java_conf.alpius.pino.core.util.Disposer;
import jp.gr.java_conf.alpius.pino.graphics.BlendMode;
import jp.gr.java_conf.alpius.pino.internal.graphics.Renderer;
import jp.gr.java_conf.alpius.pino.internal.util.Utils;
import jp.gr.java_conf.alpius.pino.layer.LayerObject;
import org.kordamp.ikonli.javafx.FontIcon;

import java.beans.PropertyChangeEvent;
import java.util.ResourceBundle;

public class LayerCell extends ListCell<LayerObject> implements Disposable {
    private final Node graphic;
    private final VBox iconArea = new VBox();
    private final FontIcon eyeIcon = new FontIcon();
    private final FontIcon roughIcon = new FontIcon();
    private final Region roughMarker = new Region();
    private final StackPane imageWrapper = new StackPane();
    private final ImageView imageView = new ImageView();
    private WritableImage image;
    private boolean requireInit = true;
    private final Text label = new Text();
    private final Text blendMode = new Text();
    private final Text opacity = new Text();
    private LayerObject oldItem;
    private static double fitHeight = 50;
    private static final StringProperty visibleIconLiteral = new SimpleStringProperty("mdi2e-eye-outline");
    private static final StringProperty invisibleIconLiteral = new SimpleStringProperty("mdi2e-eye-off-outline");

    public static void setFitHeight(double value) {
        Validate.require(value > 0);
        fitHeight = value;
    }

    public static StringProperty visibleIconLiteralProperty() {
        return visibleIconLiteral;
    }

    public static void setVisibleIconLiteral(String literal) {
        visibleIconLiteral.set(literal);
    }

    public static String getVisibleIconLiteral() {
        return visibleIconLiteral.get();
    }

    public static StringProperty invisibleIconLiteralProperty() {
        return invisibleIconLiteral;
    }

    public static void setInvisibleIconLiteral(String value) {
        invisibleIconLiteral.set(value);
    }

    public static String getInvisibleIconLiteral() {
        return invisibleIconLiteral.get();
    }

    private Node createGraphic() {
        iconArea.getStyleClass().add("icon-area");
        eyeIcon.getStyleClass().add("eye-icon");
        roughIcon.getStyleClass().add("rough-icon");
        roughMarker.getStyleClass().add("rough-marker");
        imageWrapper.getStyleClass().add("image-area");
        label.getStyleClass().add("name");
        blendMode.getStyleClass().add("blend-mode");
        opacity.getStyleClass().add("opacity");

        iconArea.getChildren().addAll(eyeIcon, roughIcon);

        var parent = new HBox();
        var hbox = new HBox(blendMode, opacity);
        hbox.getStyleClass().add("label-area2");
        var vbox = new VBox(label, hbox);
        vbox.getStyleClass().add("label-area");
        parent.getChildren().addAll(iconArea, imageWrapper, vbox);
        return parent;
    }

    public LayerCell() {
        getStyleClass().add("layer-cell");
        graphic = createGraphic();
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
            imageWrapper.getChildren().add(imageView);
        }

        label.setText(item.getName());
        int integer = (int) (item.getOpacity());
        int floatingPoint = ((int) (item.getOpacity() * 10)) - integer * 10;
        opacity.setText(integer + "." + floatingPoint + "%");
        ResourceBundle blendModeBundle = Utils.findBundle(BlendMode.class);
        assert blendModeBundle != null;
        blendMode.setText(blendModeBundle.getString(item.getBlendMode().name() + ".name"));

        if (item.isVisible()) {
            eyeIcon.setIconLiteral(getVisibleIconLiteral());
        } else {
            eyeIcon.setIconLiteral(getInvisibleIconLiteral());
        }
        if (item.isRough()) {
            roughIcon.setIconLiteral("mdi2f-format-color-marker-cancel");
        } else {
            roughIcon.setIconLiteral("mdi2f-format-color-highlight");
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
                    eyeIcon.setIconLiteral(getVisibleIconLiteral());
                } else {
                    eyeIcon.setIconLiteral(getInvisibleIconLiteral());
                }
                break;
            case "rough":
                if ((boolean) e.getNewValue()) {
                    roughIcon.setIconLiteral("mdi2f-format-color-marker-cancel");
                } else {
                    roughIcon.setIconLiteral("mdi2f-format-color-highlight");
                }
                break;
            case "blendMode":
                ResourceBundle blendModeBundle = Utils.findBundle(BlendMode.class);
                assert blendModeBundle != null;
                blendMode.setText(blendModeBundle.getString(e.getNewValue().toString() + ".name"));
                break;
        }
    }

    private void reverseVisibility() {
        LayerObject item = getItem();
        if (item != null) {
            item.setVisible(!item.isVisible());
        }
    }

    @Override
    public void dispose() {
    }
}
