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

import com.google.common.flogger.FluentLogger;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import jp.gr.java_conf.alpius.pino.application.impl.GraphicManager;
import jp.gr.java_conf.alpius.pino.application.impl.Pino;
import jp.gr.java_conf.alpius.pino.graphics.layer.LayerObject;
import jp.gr.java_conf.alpius.pino.graphics.layer.Parent;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;

public class DefaultLayerViewGraphicVisitor implements GraphicManager.LayerViewGraphicVisitor {
    private static final FluentLogger log = FluentLogger.forEnclosingClass();

    public Node visit(LayerObject layer) {
        var container = new HBox();
        var wrapper = new HBox(7);

        var pImage = new WritableImage[1];
        var image = new ImageView();
        image.setPreserveRatio(true);
        image.setSmooth(true);
        image.setFitHeight(35);
        image.setFitWidth(35);

        getImageAndSetAsync(image, layer, pImage);

        var pThread = new Thread[1];

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
                default -> updateImage(image, layer, pThread, pImage);
            }
        };

        layer.addListener(new WeakPropertyChangeListener(container, layer, listener));
        low.getChildren().setAll(visible, opacity, rough);
        wrapper.getChildren().addAll(image, new VBox(name, low));
        container.getChildren().setAll(wrapper);

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

    private static void getImageAndSetAsync(ImageView image, LayerObject layer, WritableImage[] pImage) {
        Pino.getApp().runLater(() -> {
            var offscreen = pImage[0] = getImage(layer, null);
            image.setImage(offscreen);
        });
    }

    /**
     * ImageViewのイメージを更新します.
     */
    private static void updateImage(ImageView image, LayerObject layer, Thread[] pThread, WritableImage[] pImage) {
        Thread t = pThread[0];
        if (t != null && t.isAlive()) {
            // 前回の更新を中断
            t.interrupt();
        }
        pThread[0] = new ImageUpdateThread(image, layer, pImage, () -> {
            pThread[0] = null;
            log.atInfo().log("%sのイメージを更新", layer.getName());
        });
        pThread[0].start();
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

    private static WritableImage getImage(LayerObject layer, WritableImage wimg) {
        var p = Pino.getApp().getProject();
        assert p != null;
        BufferedImage image = new BufferedImage(p.getWidth(), p.getHeight(), BufferedImage.TYPE_INT_ARGB);
        var g = image.createGraphics();
        layer.render(g, new Rectangle(p.getWidth(), p.getHeight()), false);
        g.setPaint(Color.GRAY);
        g.dispose();
        return SwingFXUtils.toFXImage(image, wimg);
    }

    @Override
    public String toString() {
        return "default";
    }

    private static class ImageUpdateThread extends Thread {
        private final ImageView view;
        private final LayerObject layer;
        private final WritableImage[] pImage;
        private final Runnable onFinished;

        public ImageUpdateThread(ImageView view, LayerObject layer, WritableImage[] pImage, Runnable onFinished) {
            this.view = view;
            this.layer = layer;
            this.pImage = pImage;
            this.onFinished = onFinished;

            // このスレッドで起こった例外は無視されます.
            setUncaughtExceptionHandler((t, e) -> {});
        }

        @Override
        public void run() {
            pImage[0] = getImage(layer, pImage[0]);
            view.setImage(pImage[0]);
            onFinished.run();
        }
    }

}
