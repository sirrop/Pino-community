package com.branc.pino.ui.editor.skin;

import com.branc.pino.core.util.Disposable;
import com.branc.pino.core.util.Disposer;
import com.branc.pino.paint.layer.LayerObject;
import com.branc.pino.ui.editor.LayerEditor;
import javafx.beans.value.ChangeListener;
import javafx.scene.Node;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.beans.PropertyDescriptor;

public class LayerEditorSkin extends SkinBase<LayerEditor> {
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
        System.out.println(object);

        Text layerName = new Text(object.getName());

        Node userProperty = createUserProperty(object);

        Node opacity = builder.createPropertyEditor(object.lookup(LayerObject.OPACITY), object, uiDisposable);
        Node visible = builder.createPropertyEditor(object.lookup(LayerObject.VISIBLE), object, uiDisposable);
        Node rough = builder.createPropertyEditor(object.lookup(LayerObject.ROUGH), object, uiDisposable);

        getChildren().setAll(new VBox(layerName, userProperty, opacity, visible, rough));
    }

    private static final String COMMON_PROPERTIES = LayerObject.NAME + "|" + LayerObject.OPACITY + "|" + LayerObject.VISIBLE + "|" + LayerObject.ROUGH;

    private Node createUserProperty(LayerObject object) {
        VBox parent = new VBox();
        for (PropertyDescriptor desc: object.getPropertyDescList()) {
            if (desc.getName().matches(COMMON_PROPERTIES)) {
                continue;
            }
            parent.getChildren().add(builder.createPropertyEditor(desc, object, uiDisposable));
        }
        return parent;
    }
}
