package com.branc.pino.ui.actionSystem;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Menu;

public class ActionableMenu extends Menu {
    public ActionableMenu() {
        actionId.addListener((obs, old, newAction) -> {
            try {
                Action action = ActionRegistry.getInstance().find(newAction);
                setOnAction(e -> action.performed(new ActionEvent(this)));
            } catch (ActionNotFoundException ignored) {

            }
        });
    }

    private final StringProperty actionId = new SimpleStringProperty(this, "actionId");

    public final StringProperty actionIdProperty() {
        return actionId;
    }

    public final void setActionId(String actionId) {
        actionIdProperty().set(actionId);
    }

    public final String getActionId() {
        return actionId.get();
    }
}
