package jp.gr.java_conf.alpius.pino.filter;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.*;

public class FilterAdaptor<F extends BufferedImageOp & RasterOp> extends Filter{
    private final F delegate;

    @Override
    public BufferedImage filter(BufferedImage src, BufferedImage dest) {
        return delegate.filter(src, dest);
    }

    @Override
    public Rectangle2D getBounds2D(BufferedImage src) {
        return delegate.getBounds2D(src);
    }

    @Override
    public BufferedImage createCompatibleDestImage(BufferedImage src, ColorModel destCM) {
        return delegate.createCompatibleDestImage(src, destCM);
    }

    @Override
    public Point2D getPoint2D(Point2D srcPt, Point2D dstPt) {
        return delegate.getPoint2D(srcPt, dstPt);
    }

    @Override
    public RenderingHints getRenderingHints() {
        return delegate.getRenderingHints();
    }

    @Override
    public WritableRaster filter(Raster src, WritableRaster dest) {
        return delegate.filter(src, dest);
    }

    @Override
    public Rectangle2D getBounds2D(Raster src) {
        return delegate.getBounds2D(src);
    }

    @Override
    public WritableRaster createCompatibleDestRaster(Raster src) {
        return delegate.createCompatibleDestRaster(src);
    }

    public FilterAdaptor(F delegate) {
        this.delegate = delegate;
    }
}
