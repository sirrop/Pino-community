package com.branc.pino.ui;

import com.branc.pino.ui.actionSystem.ActionEvent;
import com.branc.pino.ui.actionSystem.ActionNotFoundException;
import com.branc.pino.ui.actionSystem.ActionRegistry;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;

public class PinoButton extends Button {
    public PinoButton() {
        actionIdProperty().addListener((obs, old, newAction) -> {
            setOnAction(e -> {
                try {
                    ActionRegistry.getInstance().find(newAction).performed(new ActionEvent(PinoButton.this));
                } catch (ActionNotFoundException ignored) {
                    ;
                }
            });
        });
    }

    private StringProperty actionId;

    public final StringProperty actionIdProperty() {
        if (actionId == null) {
            actionId = new SimpleStringProperty(this, "actionId");
        }
        return actionId;
    }

    public final void setActionId(String value) {
        actionIdProperty().set(value);
    }

    public final String getActionId() {
        return actionId == null ? null : actionId.get();
    }
}
