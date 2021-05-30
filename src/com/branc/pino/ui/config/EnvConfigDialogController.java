package com.branc.pino.ui.config;

import com.branc.pino.application.ApplicationManager;
import com.branc.pino.application.Pino;
import com.branc.pino.ui.KeyMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class EnvConfigDialogController {
    @FXML
    private AnchorPane main;

    private Config cfg;

    public void applyChange() {
        if (cfg != null) cfg.applyChange();
    }

    public void style() {
        Label label = new Label("スタイル");
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().add("Pino Light");
        label.setLabelFor(comboBox);
        comboBox.getSelectionModel().selectFirst();
        main.getChildren().setAll(new HBox(label, comboBox));
    }

    public void menu() {
        List<Menu> menus = ApplicationManager.getApp().getRoot().getMenuBar().getMenus();
    }

    public void shortcutKey() {
        KeyMap keyMap = ApplicationManager.getApp().getService(KeyMap.class);
    }

    public void cursor(ActionEvent actionEvent) {
    }

    public void canvas(ActionEvent actionEvent) {
        Pino pino = ApplicationManager.getApp();
        var config = new CanvasConfig(pino.getAppConfig().getCanvasFps(), pino.getAppConfig().getZoomRate());

        Label fpsLabel = new Label("フレームレート");
        TextField field = new TextField();
        TextFormatter<String> formatter = new TextFormatter<>(c -> {
            if (c.getControlNewText().matches("[1-9][0-9]*(\\.[0-9]+)")) {
                return c;
            } else {
                return null;
            }
        });
        field.setTextFormatter(formatter);
        field.textProperty().addListener((obs, oldText, newText) -> config.setFps(Double.parseDouble(newText)));
        fpsLabel.setLabelFor(field);
        field.setText(String.valueOf(pino.getAppConfig().getCanvasFps()));

        Label zoomRateLabel = new Label("ズーム係数");
        TextField zoomRateTextField = new TextField();
        TextFormatter<String> zoomRateFormatter = new TextFormatter<>(c -> {
            if (c.getControlNewText().matches("[1-9][0-9]*(\\.[0-9]+)|0?(\\.\\d+)")) {
                return c;
            } else {
                return null;
            }
        });
        zoomRateLabel.setLabelFor(zoomRateTextField);
        zoomRateTextField.setTextFormatter(zoomRateFormatter);
        zoomRateTextField.textProperty().addListener((obs, oldText, newText) -> config.setZoomRate(Double.parseDouble(newText)));
        zoomRateTextField.setText(String.valueOf(pino.getAppConfig().getZoomRate()));

        cfg = config;
        main.getChildren().setAll(
                new VBox(
                        new HBox(fpsLabel, field),
                        new HBox(zoomRateLabel, zoomRateTextField)
                )
        );
    }
}
