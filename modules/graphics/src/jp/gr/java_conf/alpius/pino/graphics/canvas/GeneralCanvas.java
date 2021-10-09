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

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

final class GeneralCanvas implements Canvas {
    private Paint background = TRANSPARENT;
    private BufferedImage offscreenImage;
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
            var newData = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB_PRE);
            var g = newData.createGraphics();
            g.setComposite(AlphaComposite.Src);
            g.setPaint(background);
            g.drawRect(0, 0, newData.getWidth(), newData.getHeight());
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
        BufferedImage res = switch (transparency) {
            case Transparency.BITMASK, Transparency.TRANSLUCENT -> new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB_PRE);
            case Transparency.OPAQUE -> new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            default -> throw new IllegalArgumentException("Unknown transparency: " + transparency);
        };
        if (background != TRANSPARENT) {
            var g = res.createGraphics();
            g.setComposite(AlphaComposite.Src);
            g.setPaint(background);
            g.fillRect(0, 0, w, h);
            g.dispose();
        }
        return res;
    }

    @Override
    public BufferedImage snapshot() {
        BufferedImage result;
        result = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
        var g = result.createGraphics();
        g.drawImage(offscreenImage, 0, 0, null);
        g.dispose();
        return result;
    }

    @Override
    public void dispose() {
        offscreenImage.flush();
        background = null;
    }
}
