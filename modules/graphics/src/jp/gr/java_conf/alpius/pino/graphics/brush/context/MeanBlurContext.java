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

import jp.gr.java_conf.alpius.pino.graphics.brush.BrushContextBase;
import jp.gr.java_conf.alpius.pino.graphics.brush.MeanBlur;
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
import java.util.Arrays;

public class MeanBlurContext extends BrushContextBase<MeanBlur> {

    public MeanBlurContext(MeanBlur brush, DrawableLayer layer) {
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
        var kernelWidth = brush.getKernelWidth();
        var kernelHeight = brush.getKernelHeight();
        float[] array = new float[kernelWidth * kernelHeight];
        Arrays.fill(array, 1f / (kernelHeight * kernelWidth));
        BufferedImageOp op;
        op = new ConvolveOp(new Kernel(kernelWidth, kernelHeight, array));

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
