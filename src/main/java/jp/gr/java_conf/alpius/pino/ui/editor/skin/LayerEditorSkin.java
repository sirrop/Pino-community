package jp.gr.java_conf.alpius.pino.ui.editor.skin;

import com.google.common.flogger.FluentLogger;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import jp.gr.java_conf.alpius.pino.core.util.Disposable;
import jp.gr.java_conf.alpius.pino.core.util.Disposer;
import jp.gr.java_conf.alpius.pino.layer.LayerObject;
import jp.gr.java_conf.alpius.pino.ui.editor.LayerEditor;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;

public class LayerEditorSkin extends SkinBase<LayerEditor> {
    private static final FluentLogger log = FluentLogger.forEnclosingClass();
    private final Disposable disposable = Disposer.newDisposable();
    private Disposable uiDisposable;

    public LayerEditorSkin(LayerEditor editor) {
        super(editor);
        initialize();
    }

    private void initialize() {
        ChangeListener<LayerObject> lis = (obs, old, newValue) -> {
            if (newValue != null) {
                update(newValue);
            } else {
                getChildren().clear();
            }
        };
        getSkinnable().targetProperty().addListener(lis);
        Disposer.registerDisposable(disposable, () -> getSkinnable().targetProperty().removeListener(lis));
        LayerObject target = getSkinnable().getTarget();
        if (target != null) update(target);
    }

    @Override
    public void dispose() {
        Disposer.dispose(disposable);
        if (uiDisposable != null) Disposer.dispose(uiDisposable);
    }

    private final PropertyEditorBuilder builder = new PropertyEditorBuilder();

    private void update(LayerObject object) {
        if (uiDisposable != null) {
            Disposer.dispose(uiDisposable);
        }
        uiDisposable = Disposer.newDisposable();

        StackPane nameWrapper = new StackPane();
        nameWrapper.getStyleClass().add("name-wrapper");
        Text layerName = new Text(object.getName());
        layerName.getStyleClass().add("name-text");
        nameWrapper.getChildren().add(layerName);
        nameWrapper.setAlignment(Pos.CENTER_LEFT);

        Map<String, Node> spNode= new HashMap<>();
        Node userProperty = createUserProperty(object, spNode);

        getChildren().setAll(new VBox(nameWrapper, userProperty, spNode.get("opacity"), spNode.get("blendMode"), spNode.get("visible"), spNode.get("rough")));
    }

    private Node createUserProperty(LayerObject object, Map<String, Node> spNode) {
        VBox parent = new VBox();
        for (PropertyDescriptor desc : object.getUnmodifiablePropertyList()) {
            if (desc.getName().equals("name")) continue;
            Node node = builder.createPropertyEditor(desc, object, uiDisposable);
            switch (desc.getName()) {
                case "opacity":
                    spNode.put("opacity", node);
                    break;
                case "blendMode":
                    spNode.put("blendMode", node);
                    break;
                case "visible":
                    spNode.put("visible", node);
                    break;
                case "rough":
                    spNode.put("rough", node);
                    break;
                default:
                    parent.getChildren().add(node);
            }
        }
        return parent;
    }
}
