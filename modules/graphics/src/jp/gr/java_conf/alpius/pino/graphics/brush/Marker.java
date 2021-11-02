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
import jp.gr.java_conf.alpius.pino.graphics.brush.event.DrawEvent;
import jp.gr.java_conf.alpius.pino.graphics.layer.DrawableLayer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

public class Marker extends AbstractBrush {
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
        return new MarkerContext(this, target);
    }

    private static class MarkerContext extends BrushContextBase<Marker> {
        public MarkerContext(Marker brush, DrawableLayer layer) {
            super(brush, layer);
        }


        @Override
        protected void initialize() {
            var pencil = getBrush();
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
            g.setComposite((srcCM, dstCM, hints) -> new Blend(srcCM, dstCM, getBrush().getOpacity()));
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

    private static class Blend implements CompositeContext {
        private final ColorModel srcCM;
        private final ColorModel dstCM;
        private final float opacity;

        public Blend(ColorModel srcCM, ColorModel dstCM, float opacity) {
            this.srcCM = srcCM;
            this.dstCM = dstCM;
            this.opacity = opacity;
        }

        @Override
        public void dispose() {

        }

        @Override
        public void compose(Raster src, Raster dstIn, WritableRaster dstOut) {
            WritableRaster srcRas;
            if (src instanceof WritableRaster) {
                srcRas = (WritableRaster) src;
            } else {
                srcRas = src.createCompatibleWritableRaster();
                srcRas.setDataElements(0, 0, src);
            }
            if (dstIn != dstOut) {
                dstOut.setDataElements(0, 0, dstIn);
            }

            BufferedImage srcSurface = new BufferedImage(srcCM, srcRas, srcCM.isAlphaPremultiplied(), null);
            BufferedImage dstSurface = new BufferedImage(dstCM, dstOut, dstCM.isAlphaPremultiplied(), null);

            blend(srcSurface, dstSurface);
        }

        private void blend(BufferedImage src, BufferedImage dst) {
            int w = Math.min(src.getWidth(), dst.getWidth());
            int h = Math.min(src.getWidth(), dst.getHeight());
            int loop = w * h;
            int[] srcBuf = src.getRGB(0, 0, w, h, null, 0, w);
            int[] dstBuf = dst.getRGB(0, 0, w, h, null, 0, w);
            int[] resBuf = new int[loop];

            for (int i = 0; i < loop; i++) {
                int srcColor = srcBuf[i];
                int dstColor = dstBuf[i];
                resBuf[i] = calc(srcColor, dstColor);
            }

            dst.setRGB(0, 0, w, h, resBuf, 0, w);
        }

        private int calc(int src, int dst) {
            final int srcAlpha = Math.round((src >> 24 & 0xff) * opacity);
            final int srcRed = src >> 16 & 0xff;
            final int srcGreen = src >> 8 & 0xff;
            final int srcBlue = src & 0xff;

            final int dstAlpha = dst >> 24 & 0xff;
            final int dstRed = dst >> 16 & 0xff;
            final int dstGreen = dst >> 8 & 0xff;
            final int dstBlue = dst & 0xff;

            float sum = srcAlpha + dstAlpha;
            float s = srcAlpha / sum;
            float d = dstAlpha / sum;

            final int red = Math.round(srcRed * s + dstRed * d);
            final int green = Math.round(srcGreen * s + dstGreen * d);
            final int blue = Math.round(srcBlue * s + dstBlue * d);

            if (srcAlpha == 255 && dstAlpha == 255) {
                return srcAlpha << 24 |
                        red << 16 |
                        green << 8 |
                        blue;
            } else {
                int a1 = srcAlpha * dstAlpha;
                int a2 = srcAlpha * (255 - dstAlpha);
                int a3 = (255 - srcAlpha) * dstAlpha;
                int a = a1 + a2 + a3;

                if (a == 0) {
                    return 0;
                }

                int r = (a1 * red + a2 * srcRed + a3 * dstRed) / a;
                int g = (a1 * green + a2 * srcGreen + a3 * dstGreen) / a;
                int b = (a1 * blue + a2 * srcBlue + a3 * dstBlue) / a;

                a /= 255;

                return a << 24 | r << 16 | g << 8 | b;
            }
        }
    }

}
