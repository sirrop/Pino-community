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

package jp.gr.java_conf.alpius.pino.graphics.brush;

import jp.gr.java_conf.alpius.pino.beans.Bind;
import jp.gr.java_conf.alpius.pino.beans.Min;
import jp.gr.java_conf.alpius.pino.beans.Range;
import jp.gr.java_conf.alpius.pino.graphics.AlphaBlend;
import jp.gr.java_conf.alpius.pino.graphics.CompositeFactory;
import jp.gr.java_conf.alpius.pino.graphics.brush.event.DrawEvent;
import jp.gr.java_conf.alpius.pino.graphics.layer.DrawableLayer;

import java.awt.*;

public final class Pencil extends AbstractBrush {
    @Bind
    @Min(0)
    private float width = 5;

    public void setWidth(float value) {
        if (value < 0) {
            throw new IllegalArgumentException();
        }
        if (width != value) {
            var old = width;
            width = value;
            firePropertyChange("width", old, width);
        }
    }

    public float getWidth() {
        return width;
    }

    @Bind
    @Range(min = 0, max = 1)
    private float opacity = 1;

    public void setOpacity(float value) {
        if (value < 0 || 1 < value) {
            throw new IllegalArgumentException();
        }
        if (opacity != value) {
            var old = opacity;
            opacity = value;
            firePropertyChange("opacity", old, opacity);
        }
    }

    public float getOpacity() {
        return opacity;
    }

    @Bind
    private Color color = Color.BLACK;

    public void setColor(Color value) {
        if (value == null) {
            throw new NullPointerException();
        }
        if (color != value) {
            var old = color;
            color = value;
            firePropertyChange("color", old, color);
        }
    }

    public Color getColor() {
        return color;
    }

    @Bind
    private CompositeFactory blendMode = AlphaBlend.Mode.SRC_OVER;

    public void setBlendMode(CompositeFactory value) {
        if (value == null) {
            throw new NullPointerException();
        }
        if (blendMode != value) {
            var old = blendMode;
            blendMode = value;
            firePropertyChange("blendMode", old, blendMode);
        }
    }

    public CompositeFactory getBlendMode() {
        return blendMode;
    }

    @Bind
    private boolean antialiasing = true;

    public void setAntialiasing(boolean value) {
        if (antialiasing != value) {
            var old = antialiasing;
            antialiasing = value;
            firePropertyChange("antialiasing", old, antialiasing);
        }
    }

    public boolean isAntialiasing() {
        return antialiasing;
    }

    @Override
    public BrushContext createContext(DrawableLayer target) {
        return new PencilContext(this, target);
    }
}

final class PencilContext extends BrushContextBase<Pencil> {
    public PencilContext(Pencil pencil, DrawableLayer layer) {
        super(pencil, layer);
    }

    @Override
    protected void initialize() {
        Pencil pencil = getBrush();
        float width = pencil.getWidth();
        setComposite(AlphaComposite.Src);

        var canvas = getTarget().getCanvas();
        var w = canvas.getWidth();
        var h = canvas.getHeight();

        copy = canvas.createCompatibleImage(Transparency.TRANSLUCENT);
        offscreen = canvas.createCompatibleImage(Transparency.TRANSLUCENT);
        var g = (Graphics2D) copy.getGraphics();
        g.drawImage(canvas.snapshot(), 0, 0, null);
        g.dispose();

        offscreenG = (Graphics2D) offscreen.getGraphics();

        offscreenG.setStroke(new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        offscreenG.setPaint(pencil.getColor());

        if (pencil.isAntialiasing()) {
            offscreenG.addRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
        } else {
            offscreenG.addRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF));
        }
    }

    private Image copy;
    private Image offscreen;
    private Graphics2D offscreenG;

    @Override
    public void dispose() {
        super.dispose();
        copy.flush();
        offscreen.flush();
        offscreenG.dispose();
    }

    private static int clamp(double value) {
        return (int) (value + 0.5);
    }

    private final IntArrayList xPts = new IntArrayList();
    private final IntArrayList yPts = new IntArrayList();

    private void push(DrawEvent e) {
        xPts.add(clamp(e.getX()));
        yPts.add(clamp(e.getY()));
    }

    private void renderOffscreen() {
        var g = offscreenG;
        g.setComposite(AlphaComposite.Src);
        g.drawImage(copy, 0, 0, null);
        g.setComposite(AlphaComposite.SrcOver.derive(getBrush().getOpacity()));
        g.drawPolyline(xPts.toArray(), yPts.toArray(), xPts.size());
    }

    @Override
    public void onDrawing(DrawEvent e) {
        push(e);
        renderOffscreen();
        drawImage(offscreen, 0, 0, null);
    }

    private static class IntArrayList {
        private int[] data;
        private int size = 0;

        public IntArrayList() {
            this(10);
        }

        public IntArrayList(int initialCapacity) {
            data = new int[initialCapacity];
        }

        public int size() {
            return size;
        }

        public void add(int value) {
            if (data.length == size) {
                var newArray = new int[size + 1];
                System.arraycopy(data, 0, newArray, 0, size);
                data = newArray;
            }
            data[size] = value;
            size += 1;
        }

        public int[] toArray() {
            if (data.length == size) {
                return data.clone();
            }
            var res = new int[size];
            System.arraycopy(data, 0, res, 0, size);
            return res;
        }
    }
}
