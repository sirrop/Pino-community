/*
 * Copyright [2021] [shiro]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.gr.java_conf.alpius.pino.window.impl;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import jp.gr.java_conf.alpius.pino.application.impl.GraphicManager;
import jp.gr.java_conf.alpius.pino.graphics.layer.ImageLayer;
import jp.gr.java_conf.alpius.pino.graphics.layer.LayerObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DefaultLayerEditorGraphicVisitor implements GraphicManager.LayerEditorGraphicVisitor {
    private static final double DEFAULT_LABEL_PREF_WIDTH = 60;

    private static Label label(String text) {
        var res = new Label(text);
        res.setPrefWidth(DEFAULT_LABEL_PREF_WIDTH);
        return res;
    }

    @Override
    public Node visit(LayerObject e) {
        VBox container = new VBox(3);
        container.setPadding(new Insets(5));

        var name = new Label(e.getName());

        var slider = new Slider();
        slider.setMin(0);
        slider.setMax(100);
        slider.setBlockIncrement(1);
        slider.valueProperty().addListener((obs, oldValue, newValue) -> e.setOpacity(newValue.floatValue() / 100));
        slider.setValue(e.getOpacity() * 100);

        var visible = new CheckBox();
        visible.setText("表示");
        visible.setSelected(e.isVisible());
        visible.selectedProperty().addListener((obs, old, newValue) -> e.setVisible(newValue));

        var rough = new CheckBox();
        rough.setText("下書き");
        rough.setSelected(e.isRough());
        rough.selectedProperty().addListener((obs, oldValue, newValue) -> e.setRough(newValue));

        var x = new Slider();
        x.setValue(e.getX());
        x.valueProperty().addListener((obs, oldvalue, newvalue) -> e.setX(newvalue.doubleValue()));

        var y = new Slider();
        y.setValue(e.getY());
        y.valueProperty().addListener((obs, oldValue, newValue) -> e.setY(newValue.doubleValue()));

        var rotate = new Slider();
        rotate.setMin(-360);
        rotate.setMax(360);
        rotate.setValue(e.getRotate());
        rotate.valueProperty().addListener((obs, oldValue, newValue) -> e.setRotate(newValue.doubleValue()));

        var scaleX = new Slider();
        scaleX.setValue(e.getScaleX() * 100);
        scaleX.valueProperty().addListener((obs, oldValue, newValue) -> e.setScaleX(newValue.doubleValue() / 100));

        var scaleY = new Slider();
        scaleY.setValue(e.getScaleY() * 100);
        scaleY.valueProperty().addListener((obs, oldValue, newValue) -> e.setScaleY(newValue.doubleValue() / 100));

        var clipping = new CheckBox();
        clipping.setText("下のレイヤーでクリッピング");

        container.getChildren().setAll(
                name,
                new HBox(label("x"), x),
                new HBox(label("y"), y),
                new HBox(label("角度"), rotate),
                new HBox(label("拡大率X"), scaleX),
                new HBox(label("拡大率Y"), scaleY),
                new HBox(label("不透明度"), slider),
                visible,
                rough,
                clipping
        );

        if (e instanceof ImageLayer image) {
            Button button = new Button("画像を選択");
            button.setOnAction(event -> {
                FileChooser fc = new FileChooser();
                var file = fc.showOpenDialog(null);
                if (file != null) {
                    image.setImage(readImage(file));
                }
            });
            container.getChildren().add(button);
        }

        return container;
    }

    private static BufferedImage readImage(File input) {
        try {
            return ImageIO.read(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String toString() {
        return "default";
    }
}
