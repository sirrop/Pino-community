package pino.graphics;

import com.branc.pino.core.annotaion.Internal;
import com.branc.pino.project.Project;
import pino.layer.LayerHelper;
import pino.layer.LayerPeer;

import java.awt.*;
import java.awt.image.BufferedImage;

@Internal
public final class Renderer {
    private Renderer() {
    }

    private static final BufferedImage EMPTY = new BufferedImage(1, 1, BufferedImage.TYPE_BYTE_GRAY);

    public static BufferedImage render(Project project, boolean ignoreRough) {
        if (project == null) return EMPTY;
        int w = (int) Math.round(project.getWidth());
        int h = (int) Math.round(project.getHeight());
        BufferedImage res = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = res.createGraphics();
        project.getLayer().stream().map(LayerHelper::getPeer).forEach(it -> ((LayerPeer) it).render(g, ignoreRough));
        return res;
    }
}
