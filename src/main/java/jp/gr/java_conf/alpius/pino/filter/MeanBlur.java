package jp.gr.java_conf.alpius.pino.filter;

import jp.gr.java_conf.alpius.pino.annotations.Bind;

import java.awt.image.ConvolveOp;

public class MeanBlur extends FilterContext {
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Bind
    private int width = 5;

    @Bind
    private int height = 5;

    @Override
    public Filter createFilter() {
        return new FilterAdaptor<>(new ConvolveOp(Kernels.createMeanKernel(width, height), ConvolveOp.EDGE_NO_OP, null));
    }
}
