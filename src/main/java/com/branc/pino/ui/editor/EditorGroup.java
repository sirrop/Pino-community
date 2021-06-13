package com.branc.pino.ui.editor;

import com.branc.pino.ui.editor.skin.EditorGroupSkin;
import javafx.beans.DefaultProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

@DefaultProperty("items")
public class EditorGroup extends Control {
    private final ObservableList<FxEditor<?>> items = FXCollections.observableArrayList();

    public final ObservableList<FxEditor<?>> getItems() {
        return items;
    }

    protected Skin<?> createDefaultSkin() {
        return new EditorGroupSkin(this);
    }
}
