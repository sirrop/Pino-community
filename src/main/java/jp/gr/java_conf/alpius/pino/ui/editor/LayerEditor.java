package jp.gr.java_conf.alpius.pino.ui.editor;

import jp.gr.java_conf.alpius.pino.layer.LayerObject;
import jp.gr.java_conf.alpius.pino.ui.editor.skin.LayerEditorSkin;
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
