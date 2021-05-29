package com.branc.pino.project;

import com.branc.pino.paint.layer.LayerObject;
import com.branc.pino.project.Project;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

public final class ProjectRenderer {
    private ProjectRenderer() {}

    private static final Image EMPTY = new WritableImage(1, 1);
    private static final BufferedImage EMPTY_BUFIMG = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);

    public static Image render(Project project, RenderingOption... options) {
        if (project == null) return EMPTY;
        return SwingFXUtils.toFXImage(renderBufImg(project, options), null);
    }

    public static BufferedImage renderBufImg(Project project, RenderingOption... options) {
        if (project == null) return EMPTY_BUFIMG;
        List<RenderingOption> opts = Arrays.asList(options);

        final boolean ignoreRough = opts.contains(RenderingOption.IGNORE_ROUGH);
        final boolean ignoreInvisible = opts.contains(RenderingOption.IGNORE_INVISIBLE);

        BufferedImage res = new BufferedImage((int) project.getWidth(), (int) project.getHeight(), BufferedImage.TYPE_INT_ARGB);
        var g = res.createGraphics();
        ListIterator<LayerObject> itr = project.getLayer().listIterator(project.getLayer().size());
        while(itr.hasPrevious()) {
            LayerObject layer = itr.previous();
            if (ignoreRough && layer.isRough()) continue;
            if (ignoreInvisible && !layer.isVisible()) continue;
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, layer.getOpacity() / 100));
            g.drawImage(layer.toImage(), 0, 0, null);
        }
        g.dispose();
        return res;
    }

    public enum RenderingOption {
        IGNORE_INVISIBLE, IGNORE_ROUGH
    }

}
