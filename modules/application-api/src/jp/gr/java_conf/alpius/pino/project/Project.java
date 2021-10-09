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

package jp.gr.java_conf.alpius.pino.project;

import jp.gr.java_conf.alpius.pino.disposable.Disposable;
import jp.gr.java_conf.alpius.pino.graphics.canvas.Canvas;
import jp.gr.java_conf.alpius.pino.graphics.layer.LayerObject;
import jp.gr.java_conf.alpius.pino.service.ServiceContainer;
import jp.gr.java_conf.alpius.pino.util.ActiveModel;
import jp.gr.java_conf.alpius.pino.util.UserDataHolder;

import java.awt.color.ICC_Profile;
import java.util.List;

public interface Project extends Disposable, ServiceContainer, UserDataHolder {
    int getWidth();
    int getHeight();
    ICC_Profile getProfile();
    Canvas getCanvas();
    List<LayerObject> getLayers();
    ActiveModel<LayerObject> getActiveModel();
}
