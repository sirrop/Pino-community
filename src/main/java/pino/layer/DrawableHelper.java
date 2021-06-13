package pino.layer;


import com.branc.pino.core.annotaion.Internal;
import com.branc.pino.layer.DrawableLayer;
import com.branc.pino.layer.LayerObject;
import pino.util.Utils;

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

    private static DrawableHelper getInstance() {
        return instance;
    }

    public static void initHelper(DrawableLayer layer) {
        setHelper(layer, getInstance());
    }

    @Override
    public LayerPeer newPeerImpl(LayerObject layer) {
        return accessor.doCreatePeer(layer);
    }

    public interface DrawableAccessor {
        LayerPeer doCreatePeer(LayerObject object);
    }
}
