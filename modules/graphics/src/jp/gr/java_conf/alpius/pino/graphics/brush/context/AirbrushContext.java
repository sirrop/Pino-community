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

package jp.gr.java_conf.alpius.pino.graphics.brush.context;

import jp.gr.java_conf.alpius.pino.graphics.brush.Airbrush;
import jp.gr.java_conf.alpius.pino.graphics.brush.BrushContextBase;
import jp.gr.java_conf.alpius.pino.graphics.brush.event.DrawEvent;
import jp.gr.java_conf.alpius.pino.graphics.layer.DrawableLayer;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.*;

public class AirbrushContext extends BrushContextBase<Airbrush> {
    private final double[] vec2 = new double[2];

    public AirbrushContext(Airbrush brush, DrawableLayer layer) {
        super(brush, layer);
        setComposite(brush.getBlendMode().createComposite(brush.getOpacity()));
        setPaint(new MyPaint(brush.getColor(), brush.getWidth(), vec2));
        setStroke(new BasicStroke(brush.getWidth(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
    }

    public void onStart(DrawEvent e) {
        onDrawing(e);
    }

    public void onDrawing(DrawEvent e) {
        vec2[0] = e.getX();
        vec2[1] = e.getY();
        drawPoint();
    }

    public void onFinished(DrawEvent e) {
        onDrawing(e);
    }

    private void drawPoint() {
        int x = (int) Math.round(vec2[0]);
        int y = (int) Math.round(vec2[1]);
        drawLine(x, y, x, y);
    }

    private static class MyPaint implements Paint {
        private final Color color;
        private final float width;
        private final double[] point;

        public MyPaint(Color color, float width, double[] point) {
            this.color = color;
            this.width = width;
            this.point = point;
        }

        @Override
        public PaintContext createContext(ColorModel cm, Rectangle deviceBounds, Rectangle2D userBounds, AffineTransform xform, RenderingHints hints) {
            return new MyPaintContext(xform, color, width, point[0], point[1]);
        }

        @Override
        public int getTransparency() {
            return Transparency.TRANSLUCENT;
        }
    }

    private static class MyPaintContext implements PaintContext {
        private final BufferedImage image;
        private final AffineTransform xform;
        private final float r;
        private final double x;
        private final double y;

        public MyPaintContext(AffineTransform xform, Color color, float width, double x, double y) {
            this.xform = xform;
            this.r = width / 2;
            this.x = x;
            this.y = y;

            int w = (int) (width + 0.5);
            image = new BufferedImage(w, w, BufferedImage.TYPE_INT_ARGB);
            var g = image.createGraphics();
            var circle = new Ellipse2D.Double(0, 0, width, width);
            g.setColor(color);
            g.addRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
            g.fill(circle);
            g.dispose();
            applyGaussian(image, width);
        }

        private static BufferedImage convolve(BufferedImage src, int kernelRange, float hardness, float width) {
            float[] array = new float[kernelRange * kernelRange];
            float sum = 0;
            GaussianFunction function = GaussianFunction.create(width * (1 - hardness));
            int center = kernelRange / 2;
            for (int y = 0, offset = 0; y < kernelRange; y++, offset += kernelRange) {
                for (int x = 0; x < kernelRange; x++) {
                    var value = function.apply(x - center, y - center);
                    sum += value;
                    array[offset + x] = value;
                }
            }
            for (int i = 0; i < array.length; i++) {
                array[i] /= sum;
            }
            Kernel kernel = new Kernel(kernelRange, kernelRange, array);
            ConvolveOp operator = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
            BufferedImage dst = new BufferedImage(src.getWidth(), src.getHeight(), src.getType());
            dst.getRaster().setDataElements(0, 0, src.getData());
            var srcARas = src.getAlphaRaster();
            var dstARas = dst.getAlphaRaster();
            var resARas = operator.filter(srcARas, dstARas);
            dst.getAlphaRaster().setDataElements(0, 0, resARas);
            return dst;
        }

        private static void applyGaussian(BufferedImage image, float width) {
            WritableRaster alpha = image.getAlphaRaster();
            int r = Math.round(width) / 2;
            if (width != 0) {
                GaussianFunction func = GaussianFunction.create(width / 6);

                // ガウス関数の最大値
                var max = func.apply(0, 0);

                for (int y = 0; y < image.getHeight(); y++) {
                    int cy = r - y;
                    for (int x = 0; x < image.getWidth(); x++) {
                        var coeff = func.apply(r - x, cy) / max;
                        var sample = alpha.getSampleFloat(x, y, 0);
                        alpha.setSample(x, y, 0, sample * coeff);
                    }
                }
            }
        }

        private static void applyLinear(BufferedImage image, float hardness, float width) {
            WritableRaster alpha = image.getAlphaRaster();
            var r = width / 2;
            for (int y = 0; y < alpha.getHeight(); y++) {
                var _y = y - r;
                var y2 = _y * _y;
                for (int x = 0; x < alpha.getWidth(); x++) {
                    var _x = x - r;
                    var x2 = _x * _x;
                    var distance = Math.sqrt(y2 + x2);
                    if (distance > r) {
                        alpha.setSample(x, y, 0, 0f);
                    } else {
                        var sample = alpha.getSampleDouble(x, y, 0);
                        var rate = (distance / r) * hardness + (r - distance) / r;
                        alpha.setSample(x, y, 0, sample * rate);
                    }
                }
            }
        }

        @Override
        public void dispose() {
        }

        @Override
        public ColorModel getColorModel() {
            return ColorModel.getRGBdefault();
        }

        @Override
        public Raster getRaster(int x, int y, int w, int h) {
            var region = new Rectangle((int) (x - this.x + r), (int) (y - this.y +  r), w, h);
            return image.getData(region).createTranslatedChild(0, 0);
        }
    }
}
