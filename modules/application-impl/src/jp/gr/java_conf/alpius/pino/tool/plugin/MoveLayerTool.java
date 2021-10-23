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

package jp.gr.java_conf.alpius.pino.tool.plugin;

import jp.gr.java_conf.alpius.pino.application.impl.Pino;
import jp.gr.java_conf.alpius.pino.graphics.layer.LayerObject;
import jp.gr.java_conf.alpius.pino.input.MouseEvent;
import jp.gr.java_conf.alpius.pino.tool.Tool;
import jp.gr.java_conf.alpius.pino.window.impl.JFxWindow;
import jp.gr.java_conf.alpius.pino.window.impl.LayerEditor;
import jp.gr.java_conf.alpius.pino.window.impl.RootContainer;

import java.util.Optional;

public class MoveLayerTool implements Tool {
    private double x;
    private double y;
    private final LayerObject[] layer = new LayerObject[1];

    private void init(MouseEvent e) {
        x = e.getScreenX();
        y = e.getScreenY();
    }

    private void move(MouseEvent e) {
        var layer = this.layer[0];
        layer.setX(layer.getX() + e.getScreenX() - x);
        layer.setY(layer.getY() + e.getScreenY() - y);
        getEditor().refresh();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Optional.ofNullable(Pino.getApp().getProject())
                        .ifPresent(project -> layer[0] = project.getActiveModel().getActivatedItem());
        init(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (layer[0] != null) {
            move(e);
            init(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (layer[0] != null) {
            move(e);
        }
    }

    @Override
    public void dispose() {

    }

    private static RootContainer getRootContainer() {
        return ((JFxWindow) Pino.getApp().getWindow()).getRootContainer();
    }

    private static LayerEditor getEditor() {
        return getRootContainer().getLayerEditor();
    }
}
