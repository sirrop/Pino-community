package com.branc.pino.ui.editor;

import com.branc.pino.brush.BrushContext;
import com.branc.pino.ui.editor.skin.BrushEditorSkin;
import javafx.scene.control.Skin;

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
