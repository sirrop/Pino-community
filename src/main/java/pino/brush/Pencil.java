package pino.brush;

import com.branc.pino.brush.Brush;
import com.branc.pino.brush.event.DrawEvent;
import com.branc.pino.core.annotaion.Internal;
import com.google.common.flogger.FluentLogger;

@Internal
public class Pencil extends Brush<PencilContext> {
    private static final FluentLogger log = FluentLogger.forEnclosingClass();

    public Pencil(PencilContext context) {
        super(context);
    }

    private double x;
    private double y;

    @Override
    public void first(DrawEvent e) {
        x = e.getX();
        y = e.getY();
        drawLine((int) x, (int) y, (int) x, (int) y);

    }

    @Override
    public void main(DrawEvent e) {
        drawLine((int) x, (int) y, (int) e.getX(), (int) e.getY());

        x = e.getX();
        y = e.getY();
    }

    @Override
    public void last(DrawEvent e) {
        drawLine((int) x, (int) y, (int) e.getX(), (int) e.getY());
    }
}
