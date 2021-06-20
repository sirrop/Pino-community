package jp.gr.java_conf.alpius.pino.internal.brush;

import jp.gr.java_conf.alpius.pino.brush.Brush;
import jp.gr.java_conf.alpius.pino.brush.SequentialDrawingSupport;
import jp.gr.java_conf.alpius.pino.brush.event.DrawEvent;
import jp.gr.java_conf.alpius.pino.core.annotaion.Internal;

@Internal
public class Pencil extends Brush<PencilContext> {
    public Pencil(PencilContext context) {
        super(context);
    }

    private final SequentialDrawingSupport support = new SequentialDrawingSupport(this);

    @Override
    public void first(DrawEvent e) {
        support.begin();
        support.moveTo(e.getX(), e.getY());
    }

    @Override
    public void main(DrawEvent e) {
        support.lineTo(e.getX(), e.getY());
    }

    @Override
    public void last(DrawEvent e) {
        support.lineTo(e.getX(), e.getY());
    }
}
