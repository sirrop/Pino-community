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

package jp.gr.java_conf.alpius.pino.gui;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jp.gr.java_conf.alpius.pino.window.WindowEvent;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;
import java.util.function.Function;

public class JFxWindow implements jp.gr.java_conf.alpius.pino.window.Window {
    private final Stage peer;
    private Scene scene;

    public JFxWindow(Stage peer) {
        this.peer = peer;
        int x = (int) Math.round(peer.getX());
        int y = (int) Math.round(peer.getY());
        int w = (int) Math.round(peer.getWidth());
        int h = (int) Math.round(peer.getHeight());
        bounds = new Rectangle(x, y, w, h);
        initEvents();
    }

    private void initEvents() {
        Function<Consumer<WindowEvent>, EventHandler<javafx.stage.WindowEvent>> handlerFunction = handler -> e -> {
            if (handler == null) return;
            handler.accept(createCompatibleEvents(e));
        };
        peer.setOnShown(handlerFunction.apply(getOnShown()));
        peer.setOnShowing(handlerFunction.apply(getOnShowing()));
        peer.setOnHidden(handlerFunction.apply(getOnHidden()));
        peer.setOnHiding(handlerFunction.apply(getOnHiding()));
        peer.setOnCloseRequest(handlerFunction.apply(getOnClosing()));
    }

    @Override
    public void setTitle(String title) {
        peer.setTitle(title);
    }

    @Override
    public String getTitle() {
        return peer.getTitle();
    }

    private Image icon;

    @Override
    public void setIcon(Image image) {
        this.icon = image;
        BufferedImage bImg = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        var g = bImg.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        peer.getIcons().add(SwingFXUtils.toFXImage(bImg, null));
    }

    @Override
    public Image getIcon() {
        return icon;
    }

    private Rectangle bounds;

    @Override
    public void setBounds(int x, int y, int width, int height) {
        bounds = new Rectangle(x, y, width, height);
        peer.setX(x);
        peer.setY(y);
        peer.setWidth(width);
        peer.setHeight(height);
    }

    @Override
    public Rectangle getBounds() {
        return bounds;
    }

    @Override
    public void setFullScreen(boolean value) {
        peer.setFullScreen(value);
    }

    @Override
    public boolean isFullScreen() {
        return peer.isFullScreen();
    }

    @Override
    public void setIconified(boolean value) {
        peer.setIconified(value);
    }

    @Override
    public boolean isIconified() {
        return peer.isIconified();
    }

    @Override
    public void setResizable(boolean resizable) {
        peer.setResizable(resizable);
    }

    @Override
    public boolean isResizable() {
        return peer.isResizable();
    }

    @Override
    public boolean isShowing() {
        return peer.isShowing();
    }

    @Override
    public void show() {
        peer.show();
    }

    @Override
    public void hide() {
        peer.hide();
    }

    @Override
    public void close() {
        peer.close();
    }

    private Consumer<WindowEvent> onShown;

    @Override
    public void setOnShown(Consumer<WindowEvent> handler) {
        onShown = handler;
    }

    @Override
    public Consumer<WindowEvent> getOnShown() {
        return onShown;
    }

    private Consumer<WindowEvent> onShowing;

    @Override
    public void setOnShowing(Consumer<WindowEvent> handler) {
        onShowing = handler;
    }

    @Override
    public Consumer<WindowEvent> getOnShowing() {
        return onShowing;
    }

    private Consumer<WindowEvent> onHiding;

    @Override
    public void setOnHiding(Consumer<WindowEvent> handler) {
        onHiding = handler;
    }

    @Override
    public Consumer<WindowEvent> getOnHiding() {
        return onHiding;
    }

    private Consumer<WindowEvent> onHidden;

    @Override
    public void setOnHidden(Consumer<WindowEvent> handler) {
        onHidden = handler;
    }

    @Override
    public Consumer<WindowEvent> getOnHidden() {
        return onHidden;
    }

    private Consumer<WindowEvent> onClosing;

    @Override
    public void setOnClosing(Consumer<WindowEvent> handler) {
        onClosing = handler;
    }

    @Override
    public Consumer<WindowEvent> getOnClosing() {
        return onClosing;
    }

    private WindowEvent createCompatibleEvents(javafx.stage.WindowEvent e) {
        var source = this;
        WindowEvent.Type type;
        if (e.getEventType() == javafx.stage.WindowEvent.WINDOW_SHOWN) {
            type = WindowEvent.Type.WINDOW_SHOWN;
        } else if (e.getEventType() == javafx.stage.WindowEvent.WINDOW_SHOWING) {
            type = WindowEvent.Type.WINDOW_SHOWING;
        } else if (e.getEventType() == javafx.stage.WindowEvent.WINDOW_HIDDEN) {
            type = WindowEvent.Type.WINDOW_HIDDEN;
        } else if (e.getEventType() == javafx.stage.WindowEvent.WINDOW_HIDING) {
            type = WindowEvent.Type.WINDOW_HIDING;
        } else if (e.getEventType() == javafx.stage.WindowEvent.WINDOW_CLOSE_REQUEST) {
            type = WindowEvent.Type.WINDOW_CLOSING;
        } else {
            throw new IllegalArgumentException();
        }
        return new WindowEvent(source, type);
    }

    private RootContainer container;

    public void setRootContainer(RootContainer container) {
        this.container = container;
        if (container != null) {
            peer.setScene(scene = new Scene(container.getContent()));
        }
    }

    public RootContainer getRootContainer() {
        return container;
    }

    public Scene getScene() {
        return scene;
    }

    public Stage getStage() {
        return peer;
    }
}
