package com.branc.pino.ui.editor;

import com.branc.pino.layer.LayerObject;
import com.branc.pino.ui.editor.skin.LayerEditorSkin;
import javafx.scene.control.Skin;

public class LayerEditor extends FxEditor<LayerObject> {
    public LayerEditor() {
        getStyleClass().add("layer-editor");
        setLabel("レイヤ");
    }


    @Override
    protected Skin<?> createDefaultSkin() {
        return new LayerEditorSkin(this);
    }
}
