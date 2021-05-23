package com.branc.pino.graphics;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

class BufImgPinoGraphics extends GraphicStateBase implements PinoGraphics{
    private static final Antialias DEFAULT_ANTIALIAS = Antialias.AUTO;
    private static final Interpolation DEFAULT_INTERPOLATION = Interpolation.NEAREST_NEIGHBOR;
    private static final Composite DEFAULT_COMPOSITE = AlphaComposite.Src;
    private static final Paint DEFAULT_PAINT = Color.BLACK;
    private static final Color COLOR_WHITE = Color.WHITE;
    private static final Color TRANSPARENT = new Color(0, 0, 0, 0);
    private static final Stroke DEFAULT_STROKE = new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    private static final Font DEFAULT_FONT = Font.getFont(Font.SANS_SERIF);

    private final BufferedImage target;

    private final List<DrawingApi> children = new ArrayList<>();

    private BufImgPinoGraphics(
            BufferedImage target,
            Antialias antialias,
            Interpolation interpolation,
            Composite composite,
            Paint paint,
            Color background,
            Stroke stroke,
            AffineTransform affine,
            Shape clip,
            Font font
    ) {
        super(
                antialias,
                interpolation,
                composite,
                paint,
                background,
                stroke,
                affine,
                clip,
                font
        );
        this.target = target;
    }

    public BufImgPinoGraphics(BufferedImage target) {
        this(
                target,
                DEFAULT_ANTIALIAS,
                DEFAULT_INTERPOLATION,
                DEFAULT_COMPOSITE,
                DEFAULT_PAINT,
                target.getColorModel().hasAlpha() ? TRANSPARENT : COLOR_WHITE,
                DEFAULT_STROKE,
                null,
                null,
                DEFAULT_FONT
        );
    }

    @Override
    public void dispose() {
        while (children.size() != 0) {
            children.get(0).dispose();
        }
    }

    @Override
    public DrawingApi fill() {
        var res = new FillApi(
                this,
                target,
                getAntialias(),
                getInterpolation(),
                getComposite(),
                getPaint(),
                getBackground(),
                getStroke(),
                getTransform(),
                getClip(),
                getFont()
        );
        children.add(res);
        return res;
    }

    // todo
    @Override
    public DrawingApi outline() {
        var res = new OutlineApi(
                this,
                target,
                getAntialias(),
                getInterpolation(),
                getComposite(),
                getPaint(),
                getBackground(),
                getStroke(),
                getTransform(),
                getClip(),
                getFont()
        );
        children.add(res);
        return res;
    }

    private interface Cache {
        void undo();
        void cache(Raster ras);
        Graphics2D getGraphics();
        WritableRaster getRaster();
        void dispose();
    }

    private static class CacheImpl implements Cache {
        private final BufferedImage cache;
        private WritableRaster cacheRas;
        private final Graphics2D g;
        public CacheImpl(BufferedImage img) {
            cache = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
            cacheRas = cache.getRaster();
            g = cache.createGraphics();
        }

        @Override
        public void dispose() {
            g.dispose();
        }

        @Override
        public void undo() {
            cache.setData(cacheRas);
        }

        @Override
        public void cache(Raster ras) {
            cacheRas = ras.createCompatibleWritableRaster();
        }

        @Override
        public Graphics2D getGraphics() {
            return g;
        }

        @Override
        public WritableRaster getRaster() {
            return cache.getRaster();
        }
    }

    private static class FillApi extends GraphicStateBase implements DrawingApi {
        private final BufImgPinoGraphics parent;
        private final BufferedImage target;
        private final Graphics2D g;

        public FillApi(
                BufImgPinoGraphics parent,
                BufferedImage target,
                Antialias antialias,
                Interpolation interpolation,
                Composite composite,
                Paint paint,
                Color background,
                Stroke stroke,
                AffineTransform affine,
                Shape clip,
                Font font
        ) {
            super(antialias, interpolation, composite, paint, background, stroke, affine, clip, font);
            this.parent = parent;
            this.target = target;
            g = target.createGraphics();
            if (affine == null) {
                current = new Point2D.Double(0, 0);
            } else {
                current = affine.transform(new Point2D.Double(0, 0), null);
            }
            cache = target.getRaster().createCompatibleWritableRaster();
            cacher = new CacheImpl(target);
        }

        @Override
        public void dispose() {
            parent.children.remove(this);
            g.dispose();
            cacher.dispose();
        }

        private boolean underPathOperation = false;
        private final WritableRaster cache;
        private Point2D current;
        private Path2D ever;

        private final Cache cacher;

        private Path2D getEver() {
            if (ever == null) {
                ever = new GeneralPath(Path2D.WIND_EVEN_ODD);
                ever.moveTo(current.getX(), current.getY());
            }
            return ever;
        }

        private void undoOrCache() {
            if (underPathOperation) {
                target.setData(cache);
            } else {
                target.copyData(cache);
            }
        }

        private void pathOperation(Consumer<Path2D> c, double x, double y) {
            pathOperationWithNoCache(c, x, y);
        }

        private void pathOperationWithNoCache(Consumer<Path2D> c, double x, double y) {
            initState(g);
            undoOrCache();
            c.andThen(g::draw).accept(getEver());
            current.setLocation(x, y);
            underPathOperation = true;
        }

        // FIXME : ひとつ前の操作の結果が保持されません
        private void pathOperationWithCache(Consumer<Path2D> c, double x, double y) {
            var g = cacher.getGraphics();
            initState(g);
            if (underPathOperation) cacher.undo(); else cacher.cache(target.getRaster());
            c.andThen(g::draw).accept(getEver());
            target.setData(cacher.getRaster());
            current.setLocation(x, y);
            underPathOperation = true;
        }

        private void finishPathOperation() {
            underPathOperation = false;
        }

        @Override
        public DrawingApi lineTo(double x, double y) {
            pathOperation(it -> it.lineTo(x, y), x, y);
            return this;
        }

        @Override
        public DrawingApi curveTo(double x1, double y1, double x2, double y2, double x3, double y3) {
            pathOperation(it -> it.curveTo(x1, y1, x2, y2, x3, y3), x3, y3);
            return this;
        }

        @Override
        public DrawingApi quadTo(double x1, double y1, double x2, double y2) {
            pathOperation(it -> it.quadTo(x1, y1, x2, y2), x2, y2);
            return this;
        }

        @Override
        public DrawingApi moveTo(double x, double y) {
            finishPathOperation();
            current = getTransform() == null ? new Point2D.Double(x, y) : getTransform().transform(new Point2D.Double(x, y), current);
            return this;
        }

        @Override
        public DrawingApi lineRelative(double dx, double dy) {
            return lineTo(getCurrentX() + dx, getCurrentY() + dy);
        }

        @Override
        public DrawingApi moveRelative(double dx, double dy) {
            return moveTo(getCurrentX() + dx, getCurrentY() + dy);
        }

        @Override
        public double getCurrentX() {
            return current.getX();
        }

        @Override
        public double getCurrentY() {
            return current.getY();
        }

        @Override
        public DrawingApi drawString(String str, double x, double y) {
            initState(g);
            finishPathOperation();
            var p = getTransform() == null ? new Point2D.Double(x, y) : getTransform().transform(new Point2D.Double(x, y), null);
            g.drawString(str, (float) p.getX(), (float) p.getY());
            return this;
        }

        @Override
        public DrawingApi draw(Shape s) {
            initState(g);
            finishPathOperation();
            s = AffineTransform.getTranslateInstance(current.getX(), current.getY()).createTransformedShape(s);
            g.fill(s);
            return this;
        }
    }

    private static class OutlineApi extends GraphicStateBase implements DrawingApi {
        private final BufImgPinoGraphics parent;
        private final BufferedImage target;
        private final Graphics2D g;

        public OutlineApi(
            BufImgPinoGraphics parent,
            BufferedImage target,
            Antialias antialias,
            Interpolation interpolation,
            Composite composite,
            Paint paint,
            Color background,
            Stroke stroke,
            AffineTransform affine,
            Shape clip,
            Font font
        ) {
            super(antialias, interpolation, composite, paint, background, stroke, affine, clip, font);
            this.parent = parent;
            this.target = target;
            g = target.createGraphics();
        }

        @Override
        public void dispose() {
            parent.children.remove(this);
            g.dispose();
        }

        @Override
        public DrawingApi lineTo(double x, double y) {
            return null;
        }

        @Override
        public DrawingApi curveTo(double x1, double y1, double x2, double y2, double x3, double y3) {
            return null;
        }

        @Override
        public DrawingApi quadTo(double x1, double y1, double x2, double y2) {
            return null;
        }

        @Override
        public DrawingApi moveTo(double x, double y) {
            return null;
        }

        @Override
        public double getCurrentX() {
            return 0;
        }

        @Override
        public double getCurrentY() {
            return 0;
        }

        @Override
        public DrawingApi drawString(String str, double x, double y) {
            return null;
        }

        @Override
        public DrawingApi draw(Shape s) {
            return null;
        }
    }
}
