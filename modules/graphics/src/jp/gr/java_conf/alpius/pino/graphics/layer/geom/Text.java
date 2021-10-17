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

package jp.gr.java_conf.alpius.pino.graphics.layer.geom;

import jp.gr.java_conf.alpius.pino.beans.Bind;
import jp.gr.java_conf.alpius.pino.graphics.layer.ShapeLayer;

import java.awt.*;
import java.util.Objects;

public class Text extends ShapeLayer {
    @Bind
    private String text = "";

    public final void setText(String value) {
        if (value == null) {
            value = "";
        }
        if (!Objects.equals(text, value)) {
            var old = text;
            text = value;
            firePropertyChange("text", old, text);
        }
    }

    public final String getText() {
        return text;
    }

    @Override
    protected void renderContent(Graphics2D g, Shape aoi, boolean ignoreRough) {
        g.addRenderingHints(new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON));
        g.setPaint(getFill());
        g.setFont(new Font(Font.DIALOG, Font.PLAIN, 50));
        g.drawString(text, 0, g.getFont().getSize());
    }
}
