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

package jp.gr.java_conf.alpius.pino.window;

import java.awt.*;
import java.util.function.Consumer;

public interface Window {
    void setTitle(String title);
    String getTitle();
    void setIcon(Image image);
    Image getIcon();
    void setBounds(int x, int y, int width, int height);
    Rectangle getBounds();
    void setFullScreen(boolean value);
    boolean isFullScreen();
    void setIconified(boolean value);
    boolean isIconified();
    void setResizable(boolean resizable);
    boolean isResizable();
    boolean isShowing();
    void show();
    void hide();
    void close();
    void setOnShown(Consumer<WindowEvent> handler);
    Consumer<WindowEvent> getOnShown();
    void setOnShowing(Consumer<WindowEvent> handler);
    Consumer<WindowEvent> getOnShowing();
    void setOnHiding(Consumer<WindowEvent> handler);
    Consumer<WindowEvent> getOnHiding();
    void setOnHidden(Consumer<WindowEvent> handler);
    Consumer<WindowEvent> getOnHidden();
    void setOnClosing(Consumer<WindowEvent> handler);
    Consumer<WindowEvent> getOnClosing();
}
