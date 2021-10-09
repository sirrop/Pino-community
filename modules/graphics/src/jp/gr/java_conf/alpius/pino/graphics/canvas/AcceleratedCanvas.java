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

package jp.gr.java_conf.alpius.pino.graphics.canvas;

import jp.gr.java_conf.alpius.pino.annotation.Beta;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.util.Objects;

@Beta
final class AcceleratedCanvas implements Canvas {
    public AcceleratedCanvas() {
        gc = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration();
    }

    private Paint background = TRANSPARENT;
    private final GraphicsConfiguration gc;
    private VolatileImage offscreenImage;

    private final Object lock = new Object();

    @Override
    public void setBackground(Paint paint) {
        this.background = Objects.requireNonNullElse(paint, TRANSPARENT);
    }

    @Override
    public Paint getBackground() {
        return background;
    }

    @Override
    public void setSize(int w, int h) {
        if (w < 0) {
            throw new IllegalArgumentException();
        }

        if (h < 0) {
            throw new IllegalArgumentException();
        }

        synchronized (lock) {
            var newData = gc.createCompatibleVolatileImage(w, h, Transparency.TRANSLUCENT);
            var g = newData.createGraphics();
            g.setComposite(AlphaComposite.Src);
            g.setPaint(background);
            g.fillRect(0, 0, newData.getWidth(), newData.getHeight());
            if (offscreenImage != null) {
                g.setComposite(AlphaComposite.SrcOver);
                g.drawImage(offscreenImage, 0, 0, null);
                offscreenImage.flush();
            }
            g.dispose();
            offscreenImage = newData;
        }
    }

    @Override
    public int getWidth() {
        return offscreenImage != null ? offscreenImage.getWidth() : 0;
    }

    @Override
    public int getHeight() {
        return offscreenImage != null ? offscreenImage.getHeight() : 0;
    }

    @Override
    public Graphics2D createGraphics() {
        return offscreenImage.createGraphics();
    }

    @Override
    public Image createCompatibleImage(int w, int h, int transparency, Paint background) {
        var res = gc.createCompatibleVolatileImage(w, h, transparency);
        var g = res.createGraphics();
        g.setComposite(AlphaComposite.Src);
        g.setPaint(background);
        g.fillRect(0, 0, w, h);
        g.dispose();
        return res;
    }

    @Override
    public BufferedImage snapshot() {
        return offscreenImage.getSnapshot();
    }

    @Override
    public void dispose() {
        background = null;
        offscreenImage.flush();
    }
}
