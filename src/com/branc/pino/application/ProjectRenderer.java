package com.branc.pino.application;

import com.branc.pino.paint.layer.LayerObject;
import com.branc.pino.project.Project;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

final class ProjectRenderer {
    private ProjectRenderer() {}

    public static Image render(Project project, RenderingOption... options) {
        List<RenderingOption> opts = Arrays.asList(options);

        final boolean ignoreRough = opts.contains(RenderingOption.IGNORE_ROUGH);
        final boolean ignoreInvisible = opts.contains(RenderingOption.IGNORE_INVISIBLE);

        BufferedImage res = new BufferedImage((int) project.getWidth(), (int) project.getHeight(), BufferedImage.TYPE_INT_ARGB);
        var g = res.createGraphics();
        for (LayerObject layer: project.getLayer()) {
            if (ignoreRough && layer.isRough()) continue;
            if (ignoreInvisible && !layer.isVisible()) continue;
            g.drawImage(layer.toImage(), 0, 0, null);
        }
        g.dispose();
        return SwingFXUtils.toFXImage(res, null);
    }

    public enum RenderingOption {
        IGNORE_INVISIBLE, IGNORE_ROUGH
    }

}
