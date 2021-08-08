package jp.gr.java_conf.alpius.pino.internal.graphics;

import javafx.scene.image.WritableImage;
import jp.gr.java_conf.alpius.imagefx.Graphics;
import jp.gr.java_conf.alpius.pino.core.annotaion.Internal;
import jp.gr.java_conf.alpius.pino.internal.layer.LayerHelper;
import jp.gr.java_conf.alpius.pino.layer.LayerObject;
import jp.gr.java_conf.alpius.pino.project.Project;
import jp.gr.java_conf.alpius.pino.project.ProjectManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ListIterator;

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
        ListIterator<LayerObject> itr = project.getLayer().listIterator(project.getLayer().size());
        while (itr.hasPrevious()) {
            LayerHelper.getPeer(itr.previous()).render(g, ignoreRough);
        }
        return res;
    }

    @NotNull
    public static WritableImage render(@NotNull LayerObject layer, @Nullable WritableImage dst, boolean ignoreRough) {
        if (dst == null) {
            Project project = ProjectManager.getInstance().getProject();
            int w = (int) Math.floor(project.getWidth());
            int h = (int) Math.floor(project.getHeight());

            dst = new WritableImage(w, h);
        }
        var g = new Graphics(dst);
        LayerHelper.getPeer(layer).render(g, ignoreRough);
        return dst;
    }
}
