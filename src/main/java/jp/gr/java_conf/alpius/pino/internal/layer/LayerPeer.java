package jp.gr.java_conf.alpius.pino.internal.layer;

import javafx.scene.transform.Affine;
import jp.gr.java_conf.alpius.imagefx.Graphics;
import jp.gr.java_conf.alpius.pino.core.annotaion.Internal;
import jp.gr.java_conf.alpius.pino.graphics.BlendMode;
import jp.gr.java_conf.alpius.pino.internal.beans.BeenPeer;
import jp.gr.java_conf.alpius.pino.internal.ui.canvas.AutoRepaintImpl;
import jp.gr.java_conf.alpius.pino.internal.util.FXUtils;
import jp.gr.java_conf.alpius.pino.layer.LayerObject;
import jp.gr.java_conf.alpius.pino.project.ProjectManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Internal
public abstract class LayerPeer extends BeenPeer<LayerObject> {
    public enum DirtyFlag {
        CLEAN,
        TRANSLATION,
        DIRTY
    }

    private boolean visible = true;
    private boolean rough; // initialized with false
    protected DirtyFlag dirty = DirtyFlag.DIRTY;
    private @Nullable LayerPeer parent;

    private @Nullable LayerPeer clipLayer;
    private float opacity = 100f;
    protected boolean childDirty = false;
    private int dirtyChildrenAccumulated = 0;
    private Affine transform = new Affine();
    private @NotNull BlendMode blendMode = BlendMode.SRC_OVER;

    protected LayerPeer(LayerObject bean) {
        super(bean);
    }

    public final void clean() {
        dirty = DirtyFlag.CLEAN;
        childDirty = false;
    }

    public final void render(Graphics g, boolean ignoreRough) {
        clean();
        if (!visible || (ignoreRough & rough) || opacity == 0f) return;
        g.setGlobalBlendMode(FXUtils.toFX(blendMode));
        g.setGlobalAlpha(opacity / 100);
        renderContent(g);
    }

    public void markDirty() {
        dirty = DirtyFlag.DIRTY;
        ProjectManager.getInstance().getProject().getUserDataHolder().putUserData(AutoRepaintImpl.DIRTY_FLAG, Boolean.TRUE);
    }

    public void setVisible(boolean value) {
        if (visible != value) {
            visible = value;
            markDirty();
        }
    }

    public void setRough(boolean value) {
        if (rough != value) {
            rough = value;
            markDirty();
        }
    }

    public void setParent(@Nullable LayerPeer parent) {
        if (this.parent != parent) {
            this.parent = parent;
            markDirty();
        }
    }

    public boolean isClipped() {
        return clipLayer != null;
    }

    public void setClip(@Nullable LayerPeer peer) {
        if (clipLayer != peer) {
            clipLayer = peer;
            markDirty();
        }
    }

    public void setOpacity(float opacity) {
        if (opacity != this.opacity) {
            this.opacity = opacity;
            markDirty();
        }
    }

    public void setBlendMode(@NotNull BlendMode blendMode) {
        if (this.blendMode != blendMode) {
            this.blendMode = blendMode;
            markDirty();
        }
    }

    protected abstract void renderContent(Graphics g);
}
