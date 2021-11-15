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

package jp.gr.java_conf.alpius.pino.graphics.layer;

import com.google.common.flogger.FluentLogger;
import jp.gr.java_conf.alpius.pino.beans.Bind;
import jp.gr.java_conf.alpius.pino.graphics.DelegatingGraphics2D;
import jp.gr.java_conf.alpius.pino.graphics.Graphics2DNoOp;
import jp.gr.java_conf.alpius.pino.graphics.canvas.Canvas;
import jp.gr.java_conf.alpius.pino.memento.IncompatibleMementoException;
import jp.gr.java_conf.alpius.pino.memento.Memento;
import jp.gr.java_conf.alpius.pino.memento.MementoBase;

import java.awt.*;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.nio.ByteBuffer;
import java.text.AttributedCharacterIterator;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class DrawableLayer extends LayerObject {
    private static final FluentLogger log = FluentLogger.forEnclosingClass();

    public DrawableLayer(Canvas canvas) {
        this.canvas = Objects.requireNonNull(canvas, "canvas == null");
    }

    @Bind
    private boolean opacityProtected = false;

    public final boolean isOpacityProtected() {
        return opacityProtected;
    }

    public final void setOpacityProtected(boolean value) {
        if (opacityProtected != value) {
            var old = opacityProtected;
            opacityProtected = value;
            firePropertyChange("opacityProtected", old, value);
        }
    }

    // ロック時、描画不可
    @Bind
    private volatile boolean locked = false;

    public final boolean isLocked() {
        return locked;
    }

    public final void setLocked(boolean value) {
        if (locked != value) {
            var old = locked;
            locked = value;
            firePropertyChange("locked", old, locked);
        }
    }

    private final Canvas canvas;

    public final Canvas getCanvas() {
        return canvas;
    }

    @Override
    protected void renderContent(Graphics2D g, Shape aoi, boolean ignoreRough) {
        g.drawImage(getCanvas().snapshot(), 0, 0, null);
    }

    /**
     * このレイヤに描画を行うグラフィックスコンテキストを返します。<br>
     * このメソッドを使用して返されたグラフィックスコンテキストは、レイヤの座標とキャンバスの座標を補正するアフィン変換が設定された状態になります。
     * ただし、{@link DrawableLayer#isLocked()}がtrueのとき、{@link Graphics2DNoOp}のインスタンスが返され、この場合はアフィン変換は設定されません。<br>
     * このようなオブジェクトの選択を回避したい場合、{@code getCanvas().createGraphics()}のルートを使用することが出来ます。
     * この回避ルートを使用してグラフィックスコンテキストを獲得した場合、デフォルトではレイヤーのプロパティが反映されていないことに注意する必要があります。
     * @return このレイヤーに描画するグラフィックスコンテキスト
     */
    public Graphics2D createGraphics() {
        if (isLocked()) {
            return Graphics2DNoOp.getInstance();
        }

        var g = new BackingGraphics2D(getCanvas());
        g.translate(-getX(), -getY());
        g.rotate(-getRotate() * Math.PI * 2 / 360);
        g.scale(1 / getScaleX(), 1 / getScaleY());
        g.setOpacityProtected(isOpacityProtected());
        return g;
    }

    public Memento<?> createMemento() {
        log.atFine().log("name=%s", getName());
        return new MyMemento(this, super.createMemento());
    }

    public void restore(Memento<?> memento) {
        log.atFine().log("name=%s", getName());
        if (memento instanceof MyMemento m) {
            super.restore(m.getParent());
            var g = createGraphics();
            g.setComposite(AlphaComposite.Src);
            try {
                g.drawImage(m.getData(), 0, 0, null);
            } catch (DataFormatException e) {
                log.atWarning().withCause(e).log();
            }
            g.dispose();
        } else {
            throw new IncompatibleMementoException();
        }
    }

    private static class MyMemento extends MementoBase<DrawableLayer> {
        private final int width;
        private final int height;
        private final byte[] data;

        public MyMemento(DrawableLayer layer, Memento<?> superMemento) {
            super(layer, superMemento);
            var offscreenImage = layer.getCanvas().snapshot();
            width = offscreenImage.getWidth();
            height = offscreenImage.getHeight();
            var array = offscreenImage.getRGB(0, 0, width, height, null, 0, width);
            var buffer = ByteBuffer.allocate(array.length * 4);
            for (int pixel: array) {
                buffer.putInt(pixel);
            }
            var data = buffer.array();
            this.data = compress(data);
        }


        private static byte[] compress(byte[] data) {
            byte[] res = new byte[data.length];
            Deflater compressor = new Deflater();
            compressor.setInput(data);
            compressor.finish();
            int len = compressor.deflate(res);
            compressor.end();
            return Arrays.copyOf(res, len);
        }

        private static byte[] decompress(byte[] data, int size) throws DataFormatException {
            var res = new byte[size];
            Inflater decompressor = new Inflater();
            decompressor.setInput(data, 0, data.length);
            decompressor.inflate(res);
            decompressor.end();
            return res;
        }

        public BufferedImage getData() throws DataFormatException {
            int size = width * height;
            int[] pixels = new int[size];
            var buffer = ByteBuffer.wrap(decompress(data, size * 4)).rewind();
            for (int i = 0; i < size; ++i) {
                pixels[i] = buffer.getInt();
            }
            var res = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB_PRE);
            res.setRGB(0, 0, width, height, pixels, 0, width);
            return res;
        }
    }

    private static final class BackingGraphics2D extends DelegatingGraphics2D {
        private final Canvas surface;

        public BackingGraphics2D(Canvas surface) {
            super(surface.createGraphics());
            this.surface = surface;
        }

        private void copySurface(int x, int y, int w, int h, Graphics2D g) {
            var snapshot = surface.snapshot();
            g.drawImage(snapshot, x, y, w, h, null);
        }

        private Image createImage() {
            return surface.createCompatibleImage(Transparency.TRANSLUCENT);
        }

        private int getWidth() {
            return surface.getWidth();
        }

        private int getHeight() {
            return surface.getHeight();
        }

        private void copyOptions(Graphics2D g) {
            g.setRenderingHints(getRenderingHints());
            g.setClip(g.getClip());
            g.setTransform(g.getTransform());
            g.setStroke(g.getStroke());
            g.setPaint(g.getPaint());
            g.setFont(g.getFont());
            g.setBackground(g.getBackground());
            if (xorColor != null) {
                g.setXORMode(xorColor);
            }
        }

        private Image createOffscreen(Consumer<Graphics2D> consumer) {
            Image offscreen = createImage();
            var g = (Graphics2D) offscreen.getGraphics();
            copySurface(0, 0, getWidth(), getHeight(), g);
            copyOptions(g);
            super.setPaintMode();
            consumer.accept(g);
            g.dispose();
            if (xorColor != null) {
                setXORMode(xorColor);
            }
            return offscreen;
        }

        private Composite setSrcIn() {
            var composite = getComposite();
            setComposite(AlphaComposite.SrcIn);
            return composite;
        }

        private void renderOpacityProtectMode(Consumer<Graphics2D> command) {
            var image = createOffscreen(command);
            var composite = setSrcIn();
            super.drawImage(image, 0, 0, null);
            setComposite(composite);
        }

        private boolean opacityProtected = false;

        public void setOpacityProtected(boolean opacityProtected) {
            this.opacityProtected = opacityProtected;
        }

        public boolean isOpacityProtected() {
            return opacityProtected;
        }

        @Override
        public Graphics create() {
            throw new UnsupportedOperationException("Not implemented yet.");
        }

        @Override
        public Graphics create(int x, int y, int w, int h) {
            throw new UnsupportedOperationException("Not implemented yet.");
        }

        @Override
        public void draw(Shape s) {
            if (isOpacityProtected()) {
                renderOpacityProtectMode(g -> g.draw(s));
            } else {
                super.draw(s);
            }
        }

        @Override
        public void draw3DRect(int x, int y, int width, int height, boolean raised) {
            if (isOpacityProtected()) {
                renderOpacityProtectMode(g -> g.draw3DRect(x, y, width, height, raised));
            } else {
                super.draw3DRect(x, y, width, height, raised);
            }
        }

        @Override
        public void drawGlyphVector(GlyphVector g, float x, float y) {
            if (isOpacityProtected()) {
                renderOpacityProtectMode(g2d -> g2d.drawGlyphVector(g, x, y));
            } else {
                super.drawGlyphVector(g, x, y);
            }
        }

        @Override
        public void drawImage(BufferedImage img, BufferedImageOp op, int x, int y) {
            if (isOpacityProtected()) {
                renderOpacityProtectMode(g -> g.drawImage(img, op, x, y));
            } else {
                super.drawImage(img, op, x, y);
            }
        }

        @Override
        public boolean drawImage(Image img, AffineTransform xForm, ImageObserver observer) {
            if (isOpacityProtected()) {
                var res = new boolean[1];
                renderOpacityProtectMode(g -> res[0] = g.drawImage(img, xForm, observer));
                return res[0];
            } else {
                return super.drawImage(img, xForm, observer);
            }
        }

        @Override
        public void drawRenderableImage(RenderableImage img, AffineTransform xForm) {
            if (isOpacityProtected()) {
                renderOpacityProtectMode(g -> g.drawRenderableImage(img, xForm));
            } else {
                super.drawRenderableImage(img, xForm);
            }
        }

        @Override
        public void drawRenderedImage(RenderedImage img, AffineTransform xForm) {
            if (isOpacityProtected()) {
                renderOpacityProtectMode(g -> g.drawRenderedImage(img, xForm));
            } else {
                super.drawRenderedImage(img, xForm);
            }
        }

        @Override
        public void drawString(AttributedCharacterIterator itr, float x, float y) {
            if (isOpacityProtected()) {
                renderOpacityProtectMode(g -> g.drawString(itr, x, y));
            } else {
                super.drawString(itr, x, y);
            }
        }

        @Override
        public void drawString(AttributedCharacterIterator itr, int x, int y) {
            if (isOpacityProtected()) {
                renderOpacityProtectMode(g -> g.drawString(itr, x, y));
            } else {
                super.drawString(itr, x, y);
            }
        }

        @Override
        public void drawString(String str, float x, float y) {
            if (isOpacityProtected()) {
                renderOpacityProtectMode(g -> g.drawString(str, x, y));
            } else {
                super.drawString(str, x, y);
            }
        }

        @Override
        public void drawString(String str, int x, int y) {
            if (isOpacityProtected()) {
                renderOpacityProtectMode(g -> g.drawString(str, x, y));
            } else {
                super.drawString(str, x, y);
            }
        }

        @Override
        public void fill(Shape s) {
            if (isOpacityProtected()) {
                renderOpacityProtectMode(g -> g.fill(s));
            } else {
                super.fill(s);
            }
        }

        @Override
        public void fill3DRect(int x, int y, int w, int h, boolean raised) {
            if (isOpacityProtected()) {
                renderOpacityProtectMode(g -> g.fill3DRect(x, y, w, h, raised));
            } else {
                super.fill3DRect(x, y, w, h, raised);
            }
        }

        @Override
        public void clearRect(int x, int y, int width, int height) {
            if (isOpacityProtected()) {
                renderOpacityProtectMode(g -> g.clearRect(x, y, width, height));
            } else {
                super.clearRect(x, y, width, height);
            }
        }

        @Override
        public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
            if (isOpacityProtected()) {
                renderOpacityProtectMode(g -> g.drawArc(x, y, width, height, startAngle, arcAngle));
            } else {
                super.drawArc(x, y, width, height, startAngle, arcAngle);
            }
        }

        @Override
        public void drawBytes(byte[] data, int offset, int length, int x, int y) {
            if (isOpacityProtected()) {
                renderOpacityProtectMode(g -> g.drawBytes(data, offset, length, x, y));
            } else {
                super.drawBytes(data, offset, length, x, y);
            }
        }

        @Override
        public void drawChars(char[] data, int offset, int length, int x, int y) {
            if (isOpacityProtected()) {
                renderOpacityProtectMode(g -> g.drawChars(data, offset, length, x, y));
            } else {
                super.drawChars(data, offset, length, x, y);
            }
        }

        @Override
        public boolean drawImage(Image img, int x, int y, Color bgColor, ImageObserver observer) {
            if (isOpacityProtected()) {
                boolean[] res = new boolean[1];
                renderOpacityProtectMode(g -> res[0] = g.drawImage(img, x, y, bgColor, observer));
                return res[0];
            } else {
                return super.drawImage(img, x, y, bgColor, observer);
            }
        }

        @Override
        public boolean drawImage(Image img, int x, int y, ImageObserver observer) {
            if (isOpacityProtected()) {
                boolean[] res = new boolean[1];
                renderOpacityProtectMode(g -> res[0] = g.drawImage(img, x, y, observer));
                return res[0];
            } else {
                return super.drawImage(img, x, y, observer);
            }
        }

        @Override
        public boolean drawImage(Image img, int x, int y, int width, int height, ImageObserver observer) {
            if (isOpacityProtected()) {
                boolean[] res = new boolean[1];
                renderOpacityProtectMode(g -> res[0] = g.drawImage(img, x, y, width, height, observer));
                return res[0];
            } else {
                return super.drawImage(img, x, y, width, height, observer);
            }
        }

        @Override
        public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, Color bgcolor, ImageObserver observer) {
            if (isOpacityProtected()) {
                boolean[] res = new boolean[1];
                renderOpacityProtectMode(g -> res[0] = drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, bgcolor, observer));
                return res[0];
            } else {
                return super.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, bgcolor, observer);
            }
        }

        @Override
        public boolean 	drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, ImageObserver observer) {
            if (isOpacityProtected()) {
                boolean[] res = new boolean[1];
                renderOpacityProtectMode(g -> res[0] = g.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, observer));
                return res[0];
            } else {
                return super.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, observer);
            }
        }

        @Override
        public void drawLine(int x1, int y1, int x2, int y2) {
            if (isOpacityProtected()) {
                renderOpacityProtectMode(g -> g.drawLine(x1, y1, x2, y2));
            } else {
                super.drawLine(x1, y1, x2, y2);
            }
        }

        @Override
        public void drawOval(int x, int y, int width, int height) {
            if (isOpacityProtected()) {
                renderOpacityProtectMode(g -> g.drawOval(x, y, width, height));
            } else {
                super.drawOval(x, y, width, height);
            }
        }

        @Override
        public void drawPolygon(int[] xPts, int[] yPts, int nPts) {
            if (isOpacityProtected()) {
                renderOpacityProtectMode(g -> g.drawPolygon(xPts, yPts, nPts));
            } else {
                super.drawPolygon(xPts, yPts, nPts);
            }
        }

        @Override
        public void drawPolyline(int[] xPts, int[] yPts, int nPts) {
            if (isOpacityProtected()) {
                renderOpacityProtectMode(g -> g.drawPolyline(xPts, yPts, nPts));
            } else {
                super.drawPolyline(xPts, yPts, nPts);
            }
        }

        @Override
        public void drawRect(int x, int y, int width, int height) {
            if (isOpacityProtected()) {
                renderOpacityProtectMode(g -> g.drawRect(x, y, width, height));
            } else {
                super.drawRect(x, y, width, height);
            }
        }

        @Override
        public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
            if (isOpacityProtected()) {
                renderOpacityProtectMode(g -> g.drawRoundRect(x, y, width, height, arcWidth, arcHeight));
            } else {
                super.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
            }
        }

        @Override
        public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
            if (isOpacityProtected()) {
                renderOpacityProtectMode(g -> g.fillArc(x, y, width, height, startAngle, arcAngle));
            } else {
                super.fillArc(x, y, width, height, startAngle, arcAngle);
            }
        }

        @Override
        public void fillOval(int x, int y, int width, int height) {
            if (isOpacityProtected()) {
                renderOpacityProtectMode(g -> g.fillOval(x, y, width, height));
            } else {
                super.fillOval(x, y, width, height);
            }
        }

        @Override
        public void fillPolygon(int[] xPts, int[] yPts, int nPts) {
            if (isOpacityProtected()) {
                renderOpacityProtectMode(g -> g.fillPolygon(xPts, yPts, nPts));
            } else {
                super.fillPolygon(xPts, yPts, nPts);
            }
        }

        @Override
        public void fillPolygon(Polygon p) {
            if (isOpacityProtected()) {
                renderOpacityProtectMode(g -> g.fillPolygon(p));
            } else {
                super.fillPolygon(p);
            }
        }

        @Override
        public void fillRect(int x, int y, int width, int height) {
            if (isOpacityProtected()) {
                renderOpacityProtectMode(g -> g.fillRect(x, y, width, height));
            } else {
                super.fillRect(x, y, width, height);
            }
        }

        @Override
        public void fillRoundRect(int x, int y, int w, int h, int arcWidth, int arcHeight) {
            if (isOpacityProtected()) {
                renderOpacityProtectMode(g -> g.fillRoundRect(x, y, w, h, arcWidth, arcHeight));
            } else {
                super.fillRoundRect(x, y, w, h, arcWidth, arcHeight);
            }
        }

        private Color xorColor;

        @Override
        public void setPaintMode() {
            xorColor = null;
            super.setPaintMode();
        }

        @Override
        public void setXORMode(Color c1) {
            xorColor = c1;
            super.setXORMode(c1);
        }
    }
}
