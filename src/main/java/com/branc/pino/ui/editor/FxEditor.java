package com.branc.pino.ui.editor;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Control;

public class FxEditor<T extends EditorTarget> extends Control implements Editor<T> {
    public FxEditor() {
        getStyleClass().setAll("editor");
    }

    private StringProperty label;

    public final StringProperty labelProperty() {
        if (label == null) {
            label = new SimpleStringProperty(this, "label");
        }
        return label;
    }

    public final String getLabel() {
        return label == null ? null : label.get();
    }

    public final void setLabel(String value) {
        labelProperty().set(value);
    }

    private ObjectProperty<T> target;

    public final ObjectProperty<T> targetProperty() {
        if (target == null) {
            target = new SimpleObjectProperty<>(this, "target");
        }
        return target;
    }

    @Override
    public final void setTarget(T target) {
        targetProperty().set(target);
    }

    @Override
    public final T getTarget() {
        return target == null ? null : target.get();
    }
}
