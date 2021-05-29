package com.branc.pino.ui.editor;

import com.branc.pino.paint.layer.LayerObject;
import com.branc.pino.ui.editor.skin.LayerEditorSkin;
import javafx.beans.DefaultProperty;
import javafx.beans.property.ListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
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
