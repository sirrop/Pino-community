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
import jp.gr.java_conf.alpius.pino.graphics.brush.event.DrawEvent;
import jp.gr.java_conf.alpius.pino.graphics.layer.DrawableLayer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GaussianBlur extends AbstractBrush {
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
    @Min(1)
    private int kernelWidth = 1;

    public final void setKernelWidth(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("kernelWidth < 0");
        }
        if (value != kernelWidth) {
            var old = kernelWidth;
            kernelWidth = value;
            firePropertyChange("kernelWidth", old, value);
        }
    }

    public final int getKernelWidth() {
        return kernelWidth;
    }

    @Bind
    @Min(1)
    private int kernelHeight = 1;

    public final void setKernelHeight(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("kernelHeight < 0");
        }
        if (value != kernelHeight) {
            var old = kernelHeight;
            kernelHeight = value;
            firePropertyChange("kernelHeight", old, value);
        }
    }

    public final int getKernelHeight() {
        return kernelHeight;
    }

    @Bind
    @Min(0.01)
    private double deviation = 1.3;

    public final double getDeviation() {
        return deviation;
    }

    public final void setDeviation(double value) {
        if (value < 0.01) {
            throw new IllegalArgumentException("value <= 0");
        }
        if (deviation != value) {
            var old = deviation;
            deviation = value;
            firePropertyChange("deviation", old, value);
        }
    }

    @Override
    public BrushContext createContext(DrawableLayer target) {
        return new GaussianBlurContext(this, target);
    }

    private static class GaussianBlurContext extends BrushContextBase<GaussianBlur> {
        public GaussianBlurContext(GaussianBlur brush, DrawableLayer layer) {
            super(brush, layer);
        }

        private Image copy;
        private Image blurred;
        private Image mask;
        private Image paint;
        private Image offscreen;

        private Graphics2D maskG;
        private Graphics2D paintG;
        private Graphics2D offscreenG;

        @Override
        protected void initialize() {
            var brush = getBrush();
            int kernelWidth = brush.kernelWidth * 2 + 1;
            int kernelHeight = brush.kernelHeight * 2 + 1;
            float[] array = new float[kernelWidth * kernelHeight];
            fillArray(array, kernelWidth, kernelHeight, brush.deviation);
            //printMatrix(array, kernelWidth, kernelHeight);  // for test
            BufferedImageOp op = new ConvolveOp(new Kernel(kernelWidth, kernelHeight, array));

            setComposite(AlphaComposite.Src);

            var target = getTarget();
            var canvas = target.getCanvas();

            blurred = canvas.createCompatibleImage(Transparency.TRANSLUCENT);
            mask = canvas.createCompatibleImage(Transparency.BITMASK);
            paint = canvas.createCompatibleImage(Transparency.TRANSLUCENT);
            offscreen = canvas.createCompatibleImage(Transparency.TRANSLUCENT);

            var _copy = new BufferedImage(blurred.getWidth(null), blurred.getHeight(null), BufferedImage.TYPE_INT_ARGB_PRE);
            var g = (Graphics2D) _copy.getGraphics();
            g.setComposite(AlphaComposite.Src);
            target.render(g, new Rectangle(canvas.getWidth(), canvas.getHeight()), false);
            g.dispose();

            g = (Graphics2D) blurred.getGraphics();
            g.setComposite(AlphaComposite.Src);
            g.drawImage(_copy, op, 0, 0);
            g.dispose();

            copy = _copy;

            maskG = (Graphics2D) mask.getGraphics();
            paintG = (Graphics2D) paint.getGraphics();
            offscreenG = (Graphics2D) offscreen.getGraphics();

            maskG.setStroke(new BasicStroke(getBrush().getWidth(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        }

        private static void fillArray(float[] array, int width, int height, double deviation) {
            GaussianFunction func = GaussianFunction.create(deviation);
            float sum = 0;
            System.out.printf("width: %d, height: %d\n", width, height);
            for (int y = -(height / 2), offset = width / 2; y <= height / 2; ++y, offset += width) {
                for (int x = -(width / 2); x <= width / 2; ++x) {
                    float out;
                    sum += out = array[offset + x] = func.apply(x, y);
                    System.out.printf("(%d, %d) = %f\n", x, y, out);
                }
            }
            printMatrix(array, width, height); // for test
            for (int i = 0, len = array.length; i < len; ++i) {
                array[i] /= sum;
            }
        }

        // for test
        private static void printMatrix(float[] matrix, int width, int height) {
            for (int y = 0, offset = 0; y < height; ++y, offset += width) {
                for (int x = 0; x < width; ++x) {
                    System.out.print(matrix[offset + x] + " ");
                }
                System.out.println();
            }
        }

        private static int clamp(double pixel) {
            return (int) (pixel + 0.5);
        }

        private DrawEvent last;
        private void updateMask(DrawEvent e) {
            double x0, y0, x1, y1;
            if (last == null) {
                x0 = e.getX();
                y0 = e.getY();
            } else {
                x0 = last.getX();
                y0 = last.getY();
            }

            x1 = e.getX();
            y1 = e.getY();

            maskG.drawLine(clamp(x0), clamp(y0), clamp(x1), clamp(y1));

            last = e;
        }

        private void updatePaint() {
            var g = paintG;
            g.setComposite(AlphaComposite.Src);
            g.drawImage(mask, 0, 0, null);
            var comp = AlphaComposite.getInstance(AlphaComposite.SRC_IN);
            g.setComposite(comp);
            g.drawImage(blurred, 0, 0, null);
        }

        private void renderOffscreen() {
            var g = offscreenG;
            g.setComposite(AlphaComposite.Src);
            g.drawImage(copy, 0, 0, null);
            g.setComposite(AlphaComposite.DstOut);
            g.drawImage(mask, 0, 0, null);
            g.setComposite(AlphaComposite.SrcOver);
            g.drawImage(paint, 0, 0, null);
        }

        private void updateLayer() {
            drawImage(offscreen, 0, 0, null);
        }

        public void dispose() {
            try {
                BufferedImage renderedImage = new BufferedImage(paint.getWidth(null), paint.getHeight(null), BufferedImage.TYPE_INT_ARGB_PRE);
                var g = renderedImage.createGraphics();
                g.setComposite(AlphaComposite.Src);

                g.drawImage(blurred, 0, 0, null);
                ImageIO.write(renderedImage, "png", Files.newOutputStream(Paths.get("copy.png")));
                g.drawImage(mask, 0, 0, null);
                ImageIO.write(renderedImage, "png", Files.newOutputStream(Paths.get("mask.png")));
                g.drawImage(paint, 0, 0, null);
                ImageIO.write(renderedImage, "png", Files.newOutputStream(Paths.get("paint.png")));
                g.drawImage(offscreen, 0, 0, null);
                ImageIO.write(renderedImage, "png", Files.newOutputStream(Paths.get("offscreen.png")));
                g.dispose();
            } catch (Throwable t) {
                t.printStackTrace();
            }
            super.dispose();
            copy.flush();
            blurred.flush();
            mask.flush();
            paint.flush();
            offscreen.flush();
            maskG.dispose();
            paintG.dispose();
            offscreenG.dispose();
        }

        @Override
        public void onStart(DrawEvent e) {
            onDrawing(e);
        }

        @Override
        public void onDrawing(DrawEvent e) {
            updateMask(e);
            updatePaint();
            renderOffscreen();
            updateLayer();
        }

        @Override
        public void onFinished(DrawEvent e) {
            onDrawing(e);
        }
    }

    private interface GaussianFunction {
        float apply(int x, int y);
        double SQRT_2PI = Math.sqrt(2 * Math.PI);
        static GaussianFunction create(double deviation) {
            return (x, y) -> (float) (Math.exp( -(x * x  + y * y) / (2 * deviation * deviation) ) / (SQRT_2PI * deviation));
        }
    }
}
