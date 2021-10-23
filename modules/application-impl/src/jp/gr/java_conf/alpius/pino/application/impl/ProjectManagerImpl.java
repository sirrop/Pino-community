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

package jp.gr.java_conf.alpius.pino.application.impl;

import jp.gr.java_conf.alpius.pino.disposable.Disposer;
import jp.gr.java_conf.alpius.pino.project.Project;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

final class ProjectManagerImpl implements ProjectManager {
    private volatile Project project;
    private volatile List<Consumer<Project>> listenerList;
    private volatile List<Consumer<Project>> beforeChange;

    @Override
    public synchronized Project get() {
        return project;
    }

    private List<Consumer<Project>> getBeforeChange() {
        if (beforeChange == null) {
            beforeChange = new ArrayList<>();
        }
        return beforeChange;
    }

    @Override
    public void addBeforeChange(Consumer<Project> action) {
        getBeforeChange().add(action);
    }

    @Override
    public void removeBeforeChange(Consumer<Project> action) {
        getBeforeChange().remove(action);
    }

    private List<Consumer<Project>> getListenerList() {
        if (listenerList == null) {
            listenerList = new ArrayList<>();
        }
        return listenerList;
    }

    @Override
    public synchronized void addOnChanged(Consumer<Project> action) {
        getListenerList().add(action);
    }

    @Override
    public void removeOnChanged(Consumer<Project> action) {
        getListenerList().remove(action);
    }

    private void fire(Project project, List<Consumer<Project>> listenerList) {
        for (var listener: listenerList) {
            listener.accept(project);
        }
    }

    @Override
    public synchronized void setAndDispose(Project project) {
        fire(project, getBeforeChange());
        var old = this.project;
        this.project = project;
        if (old != null) {
            Disposer.dispose(old);
        }
        fire(project, getListenerList());
    }

    @Override
    public void dispose() {
        if (project != null) {
            Disposer.dispose(project);
        }
    }
}
