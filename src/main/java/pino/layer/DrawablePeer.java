package pino.layer;

import com.branc.pino.core.annotaion.Internal;
import com.branc.pino.layer.DrawableLayer;

import java.awt.*;
import java.awt.image.BufferedImage;

@Internal
public class DrawablePeer extends LayerPeer {
    private final BufferedImage surface;

    public DrawablePeer(DrawableLayer layer, BufferedImage surface) {
        super(layer);
        this.surface = surface;
    }

    @Override
    protected void renderContent(Graphics2D g) {
        g.drawImage(surface, 0, 0, null);
    }
}
