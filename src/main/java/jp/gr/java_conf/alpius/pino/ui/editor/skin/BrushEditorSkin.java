package jp.gr.java_conf.alpius.pino.ui.editor.skin;

import javafx.beans.value.ChangeListener;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import jp.gr.java_conf.alpius.pino.brush.BrushContext;
import jp.gr.java_conf.alpius.pino.core.util.Disposable;
import jp.gr.java_conf.alpius.pino.core.util.Disposer;
import jp.gr.java_conf.alpius.pino.ui.editor.BrushEditor;

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

        StackPane nameWrapper = new StackPane();
        nameWrapper.getStyleClass().add("name-wrapper");
        Text layerName = new Text(brushContext.getName());
        layerName.getStyleClass().add("name-text");
        nameWrapper.getChildren().add(layerName);
        nameWrapper.setAlignment(Pos.CENTER_LEFT);

        Node userProperty = createUserProperty(brushContext);

        getChildren().setAll(new VBox(nameWrapper, userProperty));
    }

    private Node createUserProperty(BrushContext brushContext) {
        VBox parent = new VBox();
        for (PropertyDescriptor p : brushContext.getUnmodifiablePropertyList()) {
            if (p.getName().equals("name")) continue;
            parent.getChildren().add(builder.createPropertyEditor(p, brushContext, uiDisposable));
        }
        return parent;
    }
}
