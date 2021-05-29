package com.branc.pino.io;

import com.branc.pino.paint.layer.LayerObject;
import com.branc.pino.project.Project;

import java.awt.color.ICC_Profile;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public final class ProjectIO {
    private ProjectIO() {}

    public static void write(Project project, Path path) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(path));
        oos.writeDouble(project.getWidth());
        oos.writeDouble(project.getHeight());
        oos.writeObject(project.getColorProfile());
        oos.writeInt(project.getLayer().size());
        for (LayerObject layerObject: project.getLayer()) {
            oos.writeObject(layerObject);
        }
        project.getService(SaveState.class).setLastSavedPath(path);
    }

    public static Project read(Path path) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(path));
        double width = ois.readDouble();
        double height = ois.readDouble();
        if (width <= 0) throw new InvalidObjectException("width is negative");
        if (height <= 0) throw new InvalidObjectException("height is negative");
        ICC_Profile profile = (ICC_Profile) ois.readObject();
        int numLayers = ois.readInt();
        List<LayerObject> layer = new LinkedList<>();
        for (int i = 0; i < numLayers; i++) {
            layer.add((LayerObject) ois.readObject());
        }
        Project res = Project.create(width, height, profile);
        res.getLayer().addAll(layer);
        res.getService(SaveState.class).setLastSavedPath(path);
        return res;
    }
}
