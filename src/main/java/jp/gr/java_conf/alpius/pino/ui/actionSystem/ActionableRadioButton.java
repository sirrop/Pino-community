package jp.gr.java_conf.alpius.pino.ui.actionSystem;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.RadioButton;
import org.jetbrains.annotations.Nullable;

public class ActionableRadioButton extends RadioButton {
    public ActionableRadioButton() {
        actionIdProperty().addListener((obs, old, newAction) -> {
            try {
                Action action = ActionRegistry.getInstance().find(newAction);
                setOnAction(e -> action.performed(new ActionEvent(this)));
            } catch (ActionNotFoundException ignored) {

            }
        });
        getStyleClass().add("actionable-radio-button");
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

    @Nullable
    public final String getActionId() {
        return actionId == null ? null : actionId.get();
    }
}
