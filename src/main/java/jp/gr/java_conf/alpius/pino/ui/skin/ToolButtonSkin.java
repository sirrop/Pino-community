package jp.gr.java_conf.alpius.pino.ui.skin;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.HBox;
import jp.gr.java_conf.alpius.pino.ui.actionSystem.ActionableRadioButton;

public class ToolButtonSkin extends SkinBase<ActionableRadioButton> {
    public ToolButtonSkin(ActionableRadioButton control) {
        super(control);
        initialize();
    }

    private void initialize() {
        String text = getSkinnable().getText();
        Node graphic = getSkinnable().getGraphic();

        if (text != null && graphic != null) {
            Label label = new Label(text);
            getChildren().setAll(new HBox(label, graphic));
        } else if (text != null) {
            getChildren().setAll(new Label(text));
        } else if (graphic != null) {
            getChildren().setAll(graphic);
        }
    }
}
