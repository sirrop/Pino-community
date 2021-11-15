package jp.gr.java_conf.alpius.pino.graphics.paint;

public interface Stroke {
    boolean isAntialias();
    Stroke setAntialias(boolean value);

    float getStrokeWidth();
    Stroke setStrokeWidth(float width);

    Stroke setCap(StrokeCap cap);
    StrokeCap getCap();
    Stroke setJoin(StrokeJoin join);
    StrokeJoin getJoin();

    Stroke setStrokeMiter(float miter);
    float getStrokeMiter();
}
