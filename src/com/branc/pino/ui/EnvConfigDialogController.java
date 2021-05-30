package com.branc.pino.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

public class EnvConfigDialogController {
    @FXML
    private AnchorPane main;

    public void applyChange() {}

    public void style() {
        Label label = new Label("スタイル");
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().add("Pino Light");
        label.setLabelFor(comboBox);
        comboBox.getSelectionModel().selectFirst();
        main.getChildren().setAll(new HBox(label, comboBox));
    }

    public void menu(ActionEvent actionEvent) {
    }

    public void shotCutKey(ActionEvent actionEvent) {
    }

    public void cursor(ActionEvent actionEvent) {
    }

    public void canvas(ActionEvent actionEvent) {
    }
}
