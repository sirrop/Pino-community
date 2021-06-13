package com.branc.pino.graphics;

import com.branc.pino.graphics.geom.Path;
import com.branc.pino.graphics.geom.Point2D;
import org.jetbrains.annotations.NotNull;

/**
 * このオブジェクトは、図形、テキスト、及びイメージを描画するための基本クラスです。
 */
public abstract class GraphicsContext {
    GraphicsContext() {}

    public abstract GraphicsContext drawPoint(float x, float y, @NotNull Paint paint);
    public abstract GraphicsContext drawLines(@NotNull Point2D[] coords, @NotNull Paint paint);
    public abstract GraphicsContext drawPolygon(@NotNull Point2D[] coords, @NotNull Paint paint);
    public abstract GraphicsContext drawCircle(float x, float y, float radius, @NotNull Paint paint);
    public abstract GraphicsContext drawRect(float x, float y, float w, float h, @NotNull Paint paint);
    public abstract GraphicsContext drawPath(@NotNull Path path, @NotNull Paint paint);

}
