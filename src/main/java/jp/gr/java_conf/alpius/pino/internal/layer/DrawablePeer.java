package jp.gr.java_conf.alpius.pino.internal.layer;

import javafx.scene.image.WritableImage;
import jp.gr.java_conf.alpius.pino.core.annotaion.Internal;
import jp.gr.java_conf.alpius.pino.layer.DrawableLayer;

import jp.gr.java_conf.alpius.imagefx.Graphics;

@Internal
public class DrawablePeer extends LayerPeer {
    private final WritableImage surface;

    public DrawablePeer(DrawableLayer layer, WritableImage surface) {
        super(layer);
        this.surface = surface;
    }

    @Override
    protected void renderContent(Graphics g) {
        g.drawImage(surface, 0, 0);
    }
}
