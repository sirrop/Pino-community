package jp.gr.java_conf.alpius.pino.graphics.transform;

import jp.gr.java_conf.alpius.pino.graphics.geom.Point2D;
import jp.gr.java_conf.alpius.pino.graphics.geom.Shape;

public abstract class Transform {
    Transform() {
    }
    public abstract Transform concatenate(Transform transform);
    public abstract Point2D transform(Point2D p);
    public abstract Shape transform(Shape shape);
}
