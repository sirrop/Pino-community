package jp.gr.java_conf.alpius.pino.ui.editor;

import javafx.scene.control.Skin;
import jp.gr.java_conf.alpius.pino.brush.BrushContext;
import jp.gr.java_conf.alpius.pino.ui.editor.skin.BrushEditorSkin;

public class BrushEditor extends FxEditor<BrushContext> {
    public BrushEditor() {
        getStyleClass().add("brush-editor");
        setLabel("ブラシ");
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new BrushEditorSkin(this);
    }
}
