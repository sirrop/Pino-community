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

package jp.gr.java_conf.alpius.pino.gui.widget.visitor;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import jp.gr.java_conf.alpius.pino.application.impl.GraphicManager;
import jp.gr.java_conf.alpius.pino.application.impl.Pino;
import jp.gr.java_conf.alpius.pino.application.util.IAliasManager;
import jp.gr.java_conf.alpius.pino.tool.Tool;
import jp.gr.java_conf.alpius.pino.tool.plugin.DrawTool;

import java.util.Map;

import static jp.gr.java_conf.alpius.pino.gui.widget.util.Nodes.slider;

public class DefaultToolEditorGraphicVisitor implements GraphicManager.ToolEditorGraphicVisitor {
    private static final Nop NOP = new Nop();
    private static final Map<Class<?>, ToolConfigurator<?>> CONSUMER_MAP = Map.of(
            DrawToolConfigurator.KEY, new DrawToolConfigurator()
    );

    @SuppressWarnings("unchecked")
    @Override
    public Node visit(Tool tool) {
        VBox container = new VBox(3);
        container.setPadding(new Insets(5));
        var aliasMgr = Pino.getApp().getService(IAliasManager.class);

        var name = new Label(aliasMgr.getAlias(tool.getClass().getName()).orElse(tool.getClass().getSimpleName()));

        container.getChildren().add(name);

        ((Configurator<Tool>) CONSUMER_MAP.getOrDefault(tool.getClass(), NOP)).configure(container, tool);

        return container;
    }

    private static abstract class ToolConfigurator<T extends Tool> implements Configurator<T> {}

    private static class Nop extends ToolConfigurator<Tool> {
        @Override
        public void configure(Pane container, Tool tool) {

        }
    }

    private static class DrawToolConfigurator extends ToolConfigurator<DrawTool> {
        public static final Class<?> KEY = DrawTool.class;

        @Override
        public void configure(Pane pane, DrawTool tool) {
            var distance = slider(it -> {
                it.setMin(1);
                it.setBlockIncrement(1);
                it.setValue(getTool().getDistance());
                it.valueProperty().addListener((observable, oldValue, newValue) -> getTool().setDistance(newValue.doubleValue()));
            });
            pane.getChildren().add(new HBox(new Label("間隔"), distance));
        }

        private static DrawTool getTool() {
            return DrawTool.getInstance();
        }
    }
}
