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

package jp.gr.java_conf.alpius.pino.gui.widget.skin;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jp.gr.java_conf.alpius.pino.gui.widget.DrawToolEditor;
import jp.gr.java_conf.alpius.pino.tool.plugin.DrawTool;

import static jp.gr.java_conf.alpius.pino.gui.widget.util.Nodes.slider;

public class DrawToolEditorSkin extends SkinBase<DrawToolEditor> {
    protected final Label title;
    protected final Node distance;

    public DrawToolEditorSkin(DrawToolEditor control) {
        super(control);
        title = new Label("ブラシツール");
        distance = slider(it -> {
            it.setMin(1);
            it.setBlockIncrement(1);
            it.setValue(getTool().getDistance());
            it.valueProperty().addListener((observable, oldValue, newValue) -> getTool().setDistance(newValue.doubleValue()));
        });
        getChildren().add(new VBox(title, new HBox(new Label("間隔"), distance)));
    }

    protected DrawTool getTool() {
        return DrawTool.getInstance();
    }

    @Override
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return topInset + title.getPrefHeight() + distance.getBoundsInLocal().getHeight() + bottomInset;
    }
}
