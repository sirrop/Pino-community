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

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import jp.gr.java_conf.alpius.pino.application.impl.GraphicManager;
import jp.gr.java_conf.alpius.pino.application.impl.Pino;
import jp.gr.java_conf.alpius.pino.graphics.layer.LayerObject;
import jp.gr.java_conf.alpius.pino.graphics.layer.Parent;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;

public class DefaultLayerViewGraphicVisitor implements GraphicManager.LayerViewGraphicVisitor {
    public Node visit(LayerObject layer) {
        var container = new HBox();
        var low = new HBox(3);

        var name = new Label(layer.getName());

        var visible = new Label();

        var rough = new Label();

        var opacity = new Label();

        setVisibleText(visible, layer.isVisible());
        setRoughText(rough, layer.isRough());
        setOpacityText(opacity, layer.getOpacity());

        PropertyChangeListener listener = e -> {
            switch (e.getPropertyName()) {
                case "visible" -> setVisibleText(visible, layer.isVisible());
                case "rough" -> setRoughText(rough, layer.isRough());
                case "opacity" -> setOpacityText(opacity, layer.getOpacity());
            }
        };

        layer.addListener(new WeakPropertyChangeListener(container, layer, listener));
        low.getChildren().setAll(visible, opacity, rough);
        container.getChildren().setAll(new VBox(name, low));

        for (int i = 0, depth = layer.getDepth(); i < depth; i++) {
            var indent = new StackPane();
            indent.setMaxWidth(15);
            container.getChildren().add(0, indent);
        }

        if (layer instanceof Parent parent) {
            var vBox = new VBox();
            vBox.getChildren().add(container);
            var gmgr = Pino.getApp().getService(GraphicManager.class);
            for (var l: parent.getChildren()) {
                vBox.getChildren().add(gmgr.getCellGraphic(l));
            }
            return vBox;
        }

        return container;
    }

    private static void setVisibleText(Label label, boolean isVisible) {
        if (isVisible) {
            label.setText("表示");
        } else {
            label.setText("非表示");
        }
    }

    private static void setRoughText(Label label, boolean isRough) {
        if (isRough) {
            label.setText("下書き");
        } else {
            label.setText(null);
        }
    }

    private static void setOpacityText(Label label, float opacity) {
        int integer = (int) (opacity * 100);
        int floating = (int) (opacity * 1000) - integer * 10;
        label.setText(String.format("%d.%d%%", integer, floating));
    }

    @Override
    public String toString() {
        return "default";
    }

    private static class WeakPropertyChangeListener implements PropertyChangeListener {
        public WeakPropertyChangeListener(Object parent, LayerObject layer, PropertyChangeListener listener) {
            ref = new WeakReference<>(parent);
            this.layer = layer;
            this.listener = listener;
        }

        private final WeakReference<?> ref;
        private final LayerObject layer;
        private final PropertyChangeListener listener;


        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (ref.get() == null) {
                layer.removeListener(this);
            } else {
                listener.propertyChange(evt);
            }
        }
    }
}
