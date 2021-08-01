package jp.gr.java_conf.alpius.pino.ui.actionSystem;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;

public class ActionableButton extends Button {
    public ActionableButton() {
        actionIdProperty().addListener((obs, old, newAction) -> {
            try {
                Action action = ActionRegistry.getInstance().find(newAction);
                setOnAction(e -> action.performed(new ActionEvent(this)));
            } catch (ActionNotFoundException ignored) {

            }
        });

        getStyleClass().add("actionable-button");
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
