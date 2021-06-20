package jp.gr.java_conf.alpius.pino.internal.layer;

import jp.gr.java_conf.alpius.pino.core.annotaion.Internal;
import jp.gr.java_conf.alpius.pino.layer.LayerObject;
import jp.gr.java_conf.alpius.pino.internal.util.Utils;

@Internal
public abstract class LayerHelper {
    private static LayerAccessor accessor;

    static {
        Utils.forceInit(LayerObject.class);
    }

    protected static void setHelper(LayerObject layer, LayerHelper helper) {
        accessor.setHelper(layer, helper);
    }

    public static LayerAccessor getAccessor() {
        if (accessor == null) throw new IllegalStateException("accessor is null");
        return accessor;
    }

    public static void setAccessor(LayerAccessor newAccessor) {
        if (accessor != null) throw new IllegalStateException("accessor has been already set.");
        accessor = newAccessor;
    }

    public static <P extends LayerPeer> P getPeer(LayerObject layer) {
        return getAccessor().getPeer(layer);
    }

    public static LayerPeer newPeer(LayerObject layer) {
        return getAccessor().getHelper(layer).newPeerImpl(layer);
    }

    public abstract LayerPeer newPeerImpl(LayerObject layer);

    public interface LayerAccessor {
        LayerHelper getHelper(LayerObject layer);

        void setHelper(LayerObject layer, LayerHelper helper);
        <P extends LayerPeer> P getPeer(LayerObject layer);
    }
}