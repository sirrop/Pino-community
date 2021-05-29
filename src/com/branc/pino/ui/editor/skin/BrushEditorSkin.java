package com.branc.pino.ui.editor.skin;

import com.branc.pino.core.util.Disposable;
import com.branc.pino.core.util.Disposer;
import com.branc.pino.paint.brush.BrushContext;
import com.branc.pino.paint.layer.LayerObject;
import com.branc.pino.ui.editor.BrushEditor;
import javafx.beans.value.ChangeListener;
import javafx.scene.Node;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.beans.PropertyDescriptor;

public class BrushEditorSkin extends SkinBase<BrushEditor> {
    private final Disposable disposable = Disposer.newDisposable();
    private Disposable uiDisposable;

    public BrushEditorSkin(BrushEditor control) {
        super(control);
        initialize();
    }

    private void initialize() {
        ChangeListener<BrushContext> lis = (obs, old, newValue) -> {
            if (newValue != null) {
                update(newValue);
            } else {
                getChildren().clear();
            }
        };
        getSkinnable().targetProperty().addListener(lis);
        Disposer.registerDisposable(disposable, () -> getSkinnable().targetProperty().removeListener(lis));
        BrushContext target = getSkinnable().getTarget();
        if (target != null) update(target);
    }


    @Override
    public void dispose() {
        Disposer.dispose(disposable);
        if (uiDisposable != null) Disposer.dispose(uiDisposable);
    }

    private final PropertyEditorBuilder builder = new PropertyEditorBuilder();

    private void update(BrushContext brushContext) {
        if (uiDisposable != null) {
            Disposer.dispose(uiDisposable);
        }
        uiDisposable = Disposer.newDisposable();

        Text layerName = new Text(brushContext.getName());

        Node userProperty = createUserProperty(brushContext);

        Node width = builder.createPropertyEditor(brushContext.lookup(BrushContext.WIDTH), brushContext, uiDisposable);
        Node opacity = builder.createPropertyEditor(brushContext.lookup(BrushContext.OPACITY), brushContext, uiDisposable);
        Node color = builder.createPropertyEditor(brushContext.lookup(BrushContext.COLOR), brushContext, uiDisposable);

        getChildren().setAll(new VBox(layerName, userProperty, width, opacity, color));
    }

    private static final String COMMON_PROPERTIES = BrushContext.NAME + "|" + BrushContext.WIDTH + "|" + BrushContext.OPACITY + "|" + BrushContext.COLOR;

    private Node createUserProperty(BrushContext brushContext) {
        VBox parent = new VBox();
        for (PropertyDescriptor desc: brushContext.getPropertyDescList()) {
            if (desc.getName().matches(COMMON_PROPERTIES)) {
                continue;
            }
            parent.getChildren().add(builder.createPropertyEditor(desc, brushContext, uiDisposable));
        }
        return parent;
    }
}
