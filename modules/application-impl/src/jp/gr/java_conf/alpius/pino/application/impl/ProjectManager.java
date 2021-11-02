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

import jp.gr.java_conf.alpius.pino.disposable.Disposable;
import jp.gr.java_conf.alpius.pino.project.Project;

import java.util.function.Consumer;

public interface ProjectManager extends Disposable {
    void setAndDispose(Project project);
    Project get();
    void addBeforeChange(Consumer<Project> action);
    void removeBeforeChange(Consumer<Project> action);
    void addOnChanged(Consumer<Project> action);
    void removeOnChanged(Consumer<Project> action);
}
