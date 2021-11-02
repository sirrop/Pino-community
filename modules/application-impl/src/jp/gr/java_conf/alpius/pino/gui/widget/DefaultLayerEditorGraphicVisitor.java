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

package jp.gr.java_conf.alpius.pino.gui.widget;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.util.converter.NumberStringConverter;
import jp.gr.java_conf.alpius.pino.application.impl.BlendModeRegistry;
import jp.gr.java_conf.alpius.pino.application.impl.GraphicManager;
import jp.gr.java_conf.alpius.pino.application.impl.Pino;
import jp.gr.java_conf.alpius.pino.application.util.IAliasManager;
import jp.gr.java_conf.alpius.pino.graphics.CompositeFactory;
import jp.gr.java_conf.alpius.pino.graphics.layer.DrawableLayer;
import jp.gr.java_conf.alpius.pino.graphics.layer.ImageLayer;
import jp.gr.java_conf.alpius.pino.graphics.layer.LayerObject;
import jp.gr.java_conf.alpius.pino.graphics.layer.ShapeLayer;
import jp.gr.java_conf.alpius.pino.graphics.layer.geom.Ellipse;
import jp.gr.java_conf.alpius.pino.graphics.layer.geom.Rectangle;
import jp.gr.java_conf.alpius.pino.graphics.layer.geom.Text;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

public class DefaultLayerEditorGraphicVisitor implements GraphicManager.LayerEditorGraphicVisitor {
    private static final double DEFAULT_LABEL_PREF_WIDTH = 60;
    private static final double DEFAULT_TEXTFIELD_PREF_WIDTH = 60;

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

        var slider = slider(it -> {
            it.setMin(0);
            it.setMax(100);
            it.setBlockIncrement(1);
            it.valueProperty().addListener((obs, oldValue, newValue) -> e.setOpacity(newValue.floatValue() / 100));
            it.setValue(e.getOpacity() * 100);
        });


        var visible = new CheckBox();
        visible.setText("表示");
        visible.setSelected(e.isVisible());
        visible.selectedProperty().addListener((obs, old, newValue) -> e.setVisible(newValue));

        var rough = new CheckBox();
        rough.setText("下書き");
        rough.setSelected(e.isRough());
        rough.selectedProperty().addListener((obs, oldValue, newValue) -> e.setRough(newValue));

        var x = slider(it -> {
            it.setValue(e.getX());
            it.setBlockIncrement(1);
            it.valueProperty().addListener((obs, oldvalue, newvalue) -> e.setX(newvalue.doubleValue()));
        });

        var y = slider(it -> {
            it.setValue(e.getY());
            it.setBlockIncrement(1);
            it.valueProperty().addListener((obs, oldValue, newValue) -> e.setY(newValue.doubleValue()));
        });

        var rotate = slider(it -> {
            it.setMin(-360);
            it.setMax(360);
            it.setBlockIncrement(1);
            it.setValue(e.getRotate());
            it.valueProperty().addListener((obs, oldValue, newValue) -> e.setRotate(newValue.doubleValue()));
        });

        var scaleX = slider(it -> {
            it.setBlockIncrement(1);
            it.setValue(e.getScaleX() * 100);
            it.valueProperty().addListener((obs, oldValue, newValue) -> e.setScaleX(newValue.doubleValue() / 100));
        });

        var scaleY = slider(it -> {
            it.setBlockIncrement(1);
            it.setValue(e.getScaleY() * 100);
            it.valueProperty().addListener((obs, oldValue, newValue) -> e.setScaleY(newValue.doubleValue() / 100));
        });

        var clipping = new CheckBox();
        clipping.setText("下のレイヤーでクリッピング");
        clipping.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                var project = Pino.getApp().getProject();
                var layer = (ObservableList<LayerObject>) project.getLayers();
                ListChangeListener<? super LayerObject> listener = c -> {
                    var layerIndex = layer.indexOf(e);
                    if (layerIndex == layer.size() - 1) {
                        e.setClip(null);
                    } else {
                        e.setClip(layer.get(layerIndex + 1));
                    }
                };
                layer.addListener(listener);
                listener.onChanged(null);
            } else {
                e.setClip(null);
            }
        });

        var blendMode = new ComboBox<CompositeFactory>();
        blendMode.setItems(BlendModeRegistry.getInstance().getAvailableBlendModeList());
        blendMode.setValue(e.getCompositeFactory());
        blendMode.valueProperty().addListener((observable, oldValue, newValue) -> e.setCompositeFactory(newValue));
        blendMode.setButtonCell(new ListCell<>() {
            @Override
            public void updateItem(CompositeFactory item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(Pino.getApp().getService(IAliasManager.class).getAlias(item.toString()).orElse(item.toString()));
                }
            }
        });
        blendMode.setCellFactory(listView -> new ListCell<>() {
            @Override
            public void updateItem(CompositeFactory item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(Pino.getApp().getService(IAliasManager.class).getAlias(item.toString()).orElse(item.toString()));
                }
            }
        });

        container.getChildren().setAll(
                name,
                new HBox(label("x"), x),
                new HBox(label("y"), y),
                new HBox(label("角度"), rotate),
                new HBox(label("拡大率X"), scaleX),
                new HBox(label("拡大率Y"), scaleY),
                new HBox(label("不透明度"), slider),
                new HBox(label("合成モード"), blendMode),
                visible,
                rough,
                clipping
        );

        if (e instanceof DrawableLayer drawable) {
            CheckBox opacityProtect = new CheckBox("透明度保護※不具合あり");
            opacityProtect.setSelected(drawable.isOpacityProtected());
            opacityProtect.selectedProperty().addListener((observable, oldValue, newValue) -> drawable.setOpacityProtected(newValue));

            CheckBox locked = new CheckBox("ロック");
            locked.setSelected(drawable.isLocked());
            locked.selectedProperty().addListener((observable, oldValue, newValue) -> drawable.setLocked(newValue));
            container.getChildren().addAll(opacityProtect, locked);
        }
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

        if (e instanceof ShapeLayer shape) {
            if (e instanceof Rectangle rectangle) {
                var widthLabel = label("幅");
                var heightLabel = label("高さ");

                var widthSlider = slider(it -> {
                    it.setBlockIncrement(1);
                    it.setValue(rectangle.getWidth());
                    it.valueProperty().addListener((observable, oldValue, newValue) -> rectangle.setWidth(Math.round(newValue.floatValue())));
                });
                var heightSlider = slider(it -> {
                    it.setBlockIncrement(1);
                    it.setValue(rectangle.getHeight());
                    it.valueProperty().addListener((observable, oldValue, newValue) -> rectangle.setHeight(Math.round(newValue.floatValue())));
                });

                container.getChildren()
                        .addAll(3,
                                List.of(
                                        new HBox(widthLabel, widthSlider),
                                        new HBox(heightLabel, heightSlider))

                        );
            }
            if (e instanceof Ellipse ellipse) {
                var widthLabel = label("幅");
                var heightLabel = label("高さ");

                var widthSlider = slider(it -> {
                    it.setBlockIncrement(1);
                    it.setValue(ellipse.getWidth());
                    it.valueProperty().addListener((observable, oldValue, newValue) -> ellipse.setWidth(Math.round(newValue.floatValue())));
                });
                var heightSlider = slider(it -> {
                    it.setBlockIncrement(1);
                    it.setValue(ellipse.getHeight());
                    it.valueProperty().addListener((observable, oldValue, newValue) -> ellipse.setHeight(Math.round(newValue.floatValue())));
                });

                container.getChildren()
                        .addAll(3,
                                List.of(
                                        new HBox(widthLabel, widthSlider),
                                        new HBox(heightLabel, heightSlider))

                        );
            }
            if (e instanceof Text text) {
                var inputLabel = label("入力");
                TextField input = new TextField(text.getText());
                input.textProperty().addListener((observable, oldValue, newValue) -> text.setText(newValue));
                container.getChildren().add(7, new HBox(inputLabel, input));
            }
            ColorPicker fillPicker = new ColorPicker(toFxColor(shape.getFill()));
            fillPicker.valueProperty().addListener((observable, oldValue, newValue) -> shape.setFill(toAwtColor(newValue)));
            var label = label("色");
            container.getChildren().add(new HBox(label, fillPicker));
        }

        return container;
    }

    private static Color toFxColor(java.awt.Color awt) {
        float[] components = awt.getRGBComponents(null);
        return new Color(components[0], components[1], components[2], components[3]);
    }

    private static java.awt.Color toAwtColor(Color fx) {
        float r = (float) fx.getRed();
        float g = (float) fx.getGreen();
        float b = (float) fx.getBlue();
        float a = (float) fx.getOpacity();
        return new java.awt.Color(r, g, b, a);
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

    public static Node slider(Consumer<Slider> configurator) {
        return slider(configurator, 1);
    }

    public static Node slider(Consumer<Slider> configurator, int mug) {
        var slider = new Slider();
        configurator.accept(slider);
        var incBtn = new Button("+");
        incBtn.setOnAction(e -> slider.increment());
        incBtn.getStyleClass().add("increment-button");
        var decBtn = new Button("-");
        decBtn.setOnAction(e -> slider.decrement());
        decBtn.getStyleClass().add("decrement-button");
        var textField = new TextField();
        var formatter = new TextFormatter<>(new NumberStringConverter() {
            @Override
            public String toString(Number number) {
                String result = super.toString(number);
                return drop(result, getNumDigitsUnderPoint(result) - mug);
            }
        }, slider.getValue());
        textField.setTextFormatter(formatter);
        textField.setPrefWidth(DEFAULT_TEXTFIELD_PREF_WIDTH);
        formatter.valueProperty().bindBidirectional(slider.valueProperty());
        return new HBox(slider, decBtn, incBtn, textField);
    }

    private static int getNumDigitsUnderPoint(String str) {
        int point = str.indexOf('.');
        if (point == -1) {
            return 0;
        } else {
            return str.length() - point - 1;
        }
    }

    private static String drop(String string, int num) {
        if (num <= 0) return string;
        return string.substring(0, string.length() - num);
    }
}
