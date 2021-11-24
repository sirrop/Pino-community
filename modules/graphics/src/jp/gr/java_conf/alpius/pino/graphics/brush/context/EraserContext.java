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
import jp.gr.java_conf.alpius.pino.graphics.brush.Eraser;
import jp.gr.java_conf.alpius.pino.graphics.brush.event.DrawEvent;
import jp.gr.java_conf.alpius.pino.graphics.layer.DrawableLayer;

import java.awt.*;

public final class EraserContext extends BrushContextBase<Eraser> {
    public EraserContext(Eraser eraser, DrawableLayer layer) {
        super(eraser, layer);
    }

    @Override
    protected void initialize() {
        Eraser eraser = getBrush();

        setComposite(AlphaComposite.Src);

        final var canvas = getTarget().getCanvas();
        final var w = canvas.getWidth();
        final var h = canvas.getHeight();

        copy   = canvas.createCompatibleImage(Transparency.TRANSLUCENT);
        mask   = canvas.createCompatibleImage(Transparency.BITMASK);
        paint = canvas.createCompatibleImage(Transparency.TRANSLUCENT);
        offscreen = canvas.createCompatibleImage(Transparency.TRANSLUCENT);

        var g = (Graphics2D) copy.getGraphics();
        getTarget().render(g, new Rectangle(w, h), false);
        g.dispose();

        maskG = (Graphics2D) mask.getGraphics();
        paintG  = (Graphics2D) paint.getGraphics();
        offscreenG = (Graphics2D) offscreen.getGraphics();

        maskG.setStroke(new BasicStroke(eraser.getWidth(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        if (eraser.isAntialiasing()) {
            maskG.addRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
        } else {
            maskG.addRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF));
        }
    }

    private Image copy;         // 初期化時のLayerの状態のコピー
    private Image mask;         // 描画領域を決定するマスク
    private Image paint;        // paint
    private Image offscreen;    // オフスクリーンイメージ

    private Graphics2D paintG;
    private Graphics2D maskG;
    private Graphics2D offscreenG;

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
        var comp = AlphaComposite.getInstance(AlphaComposite.SRC_IN, 1 - getBrush().getOpacity());
        g.setComposite(comp);
        g.drawImage(copy, 0, 0, null);
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
        super.dispose();
        copy.flush();
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