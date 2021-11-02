/*
 * Copyright [2021] [shiro]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.gr.java_conf.alpius.pino.io.serializable.project;

import javafx.collections.ObservableList;
import jp.gr.java_conf.alpius.pino.annotation.Beta;
import jp.gr.java_conf.alpius.pino.graphics.layer.LayerObject;
import jp.gr.java_conf.alpius.pino.io.serializable.layer.LayerProxy;
import jp.gr.java_conf.alpius.pino.project.Project;
import jp.gr.java_conf.alpius.pino.project.impl.PinoProject;

import java.awt.color.ICC_Profile;
import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

@Beta
public class ProjectProxy implements Serializable {
    @Serial
    private static final long serialVersionUID = -2677378551349971288L;

    private transient int width;
    private transient int height;
    private transient ICC_Profile iccProfile;
    private transient List<LayerProxy> layers;

    public ProjectProxy(Project project) {
        var children = project.getLayers()
                .stream()
                .map(LayerProxy::create)
                .collect(Collectors.toList());
        initialize(project.getWidth(), project.getHeight(), project.getProfile(), children);
    }

    private void initialize(int w, int h, ICC_Profile profile, List<LayerProxy> children) {
        if (w <= 0) {
            throw new IllegalArgumentException("w <= 0");
        }
        if (h <= 0) {
            throw new IllegalArgumentException("h <= 0");
        }
        if (profile == null) {
            throw new NullPointerException("profile == null");
        }
        if (children == null) {
            throw new NullPointerException("children == null");
        }
        width = w;
        height = h;
        iccProfile = profile;
        layers = children;
    }

    public Project createProject() {
        var children = layers.stream()
                .map(proxy -> proxy.createLayer(width, height))
                .collect(Collectors.toList());
        var project = new PinoProject(width, height, iccProfile);
        ((ObservableList<LayerObject>) project.getLayers()).setAll(children);
        return project;
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeInt(width);
        out.writeInt(height);
        out.writeObject(iccProfile);
        out.writeObject(layers);
    }

    @SuppressWarnings("unchecked")
    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        int width = in.readInt();
        int height = in.readInt();
        ICC_Profile profile = (ICC_Profile) in.readObject();
        List<LayerProxy> children = (List<LayerProxy>) in.readObject();
        try {
            initialize(width, height, profile, List.copyOf(children));
        } catch (Throwable t) {
            throw new InvalidObjectException(t.getMessage());
        }
    }
}
