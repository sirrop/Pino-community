package jp.gr.java_conf.alpius.pino.filter;

import jp.gr.java_conf.alpius.pino.core.util.Disposable;

import java.awt.image.BufferedImageOp;
import java.awt.image.RasterOp;

public abstract class Filter implements Disposable, BufferedImageOp, RasterOp {
    @Override
    public void dispose() {

    }
}
