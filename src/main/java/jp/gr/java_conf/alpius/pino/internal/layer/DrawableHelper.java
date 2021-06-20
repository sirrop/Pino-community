package jp.gr.java_conf.alpius.pino.internal.layer;


import javafx.scene.image.WritableImage;
import jp.gr.java_conf.alpius.imagefx.Graphics;
import jp.gr.java_conf.alpius.pino.core.annotaion.Internal;
import jp.gr.java_conf.alpius.pino.internal.util.Utils;
import jp.gr.java_conf.alpius.pino.layer.DrawableLayer;
import jp.gr.java_conf.alpius.pino.layer.LayerObject;

@Internal
public class DrawableHelper extends LayerHelper {
    private static final DrawableHelper instance;
    private static DrawableAccessor accessor;

    static {
        instance = new DrawableHelper();
        Utils.forceInit(DrawableLayer.class);
    }

    public static void setDrawableAccessor(DrawableAccessor newAccessor) {
        if (accessor != null) throw new IllegalStateException();
        accessor = newAccessor;
    }

    public static DrawableAccessor getDrawableAccessor() {
        if (accessor == null) throw new IllegalStateException();
        return accessor;
    }

    private static DrawableHelper getInstance() {
        return instance;
    }

    public static void initHelper(DrawableLayer layer) {
        setHelper(layer, getInstance());
    }

    public static Graphics getGraphics(DrawableLayer layer) {
        return getDrawableAccessor().doGetGraphics(layer);
    }

    public static WritableImage getImage(DrawableLayer layer) {
        return getDrawableAccessor().getImage(layer);
    }

    @Override
    public LayerPeer newPeerImpl(LayerObject layer) {
        return getDrawableAccessor().doCreatePeer(layer);
    }

    public interface DrawableAccessor {
        LayerPeer doCreatePeer(LayerObject object);
        Graphics doGetGraphics(DrawableLayer layer);
        WritableImage getImage(DrawableLayer layer);
    }
}
