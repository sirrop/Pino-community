package jp.gr.java_conf.alpius.pino.internal.graphics;

import javafx.scene.image.WritableImage;
import jp.gr.java_conf.alpius.imagefx.Graphics;
import jp.gr.java_conf.alpius.pino.application.ApplicationManager;
import jp.gr.java_conf.alpius.pino.core.annotaion.Internal;
import jp.gr.java_conf.alpius.pino.internal.layer.LayerHelper;
import jp.gr.java_conf.alpius.pino.internal.layer.LayerPeer;
import jp.gr.java_conf.alpius.pino.project.Project;

@Internal
public final class Renderer {
    private Renderer() {
    }

    private static final WritableImage EMPTY = new WritableImage(1, 1);

    public static WritableImage render(Project project, boolean ignoreRough) {
        if (project == null) return EMPTY;
        int w = (int) Math.round(project.getWidth());
        int h = (int) Math.round(project.getHeight());
        var res = new WritableImage(w, h);
        Graphics g = new Graphics(res);
        ApplicationManager.getApp().runLater(() -> project.getLayer().stream().map(LayerHelper::getPeer).forEach(it -> ((LayerPeer) it).render(g, ignoreRough)));
        return res;
    }
}
