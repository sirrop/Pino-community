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

import javafx.scene.SnapshotParameters;
import javafx.scene.paint.Color;
import jp.gr.java_conf.alpius.pino.application.impl.BrushManager;
import jp.gr.java_conf.alpius.pino.application.impl.Pino;
import jp.gr.java_conf.alpius.pino.input.MouseEvent;
import jp.gr.java_conf.alpius.pino.tool.Tool;
import jp.gr.java_conf.alpius.pino.ui.actionSystem.ActionEvent;
import jp.gr.java_conf.alpius.pino.ui.actionSystem.plugin.ActivateDrawTool;
import jp.gr.java_conf.alpius.pino.util.Result;

/**
 * スポイト
 */
public class EyedropperTool implements Tool {
    @Override
    public void mouseReleased(MouseEvent e) {
        int x = round(e.getX());
        int y = round(e.getY());
        var img = Pino.getApp().getWindow().getRootContainer().getCanvas().snapshot(new SnapshotParameters(), null);
        var reader = img.getPixelReader();
        Color c = reader.getColor(x, y);
        setColor(c);
        Pino.getApp().getWindow().getRootContainer().getBrushEditor().refresh();
        Pino.getApp().getWindow().getRootContainer().getBrushView().refresh();
        (new ActivateDrawTool()).performAction(new ActionEvent(this));
    }

    private static int round(double value) {
        return (int) (value + 0.5);
    }

    private static void setColor(Color c) {
        var brush = BrushManager.getInstance().getActiveModel().getActivatedItem();
        for (var desc: brush.getUnmodifiablePropertyList()) {
            if (desc.getName().equals("color")) {
                Result.tryToRun(() ->desc.getWriteMethod().invoke(brush, toAwtColor(c)))
                        .printStackTrace();
                break;
            }
        }
    }

    private static java.awt.Color toAwtColor(Color c) {
        float r = (float) c.getRed();
        float g = (float) c.getGreen();
        float b = (float) c.getBlue();
        float a = (float) c.getOpacity();
        return new java.awt.Color(r, g, b, a);
    }
}