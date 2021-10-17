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

package jp.gr.java_conf.alpius.pino.ui.actionSystem.plugin;

import jp.gr.java_conf.alpius.pino.application.impl.Pino;
import jp.gr.java_conf.alpius.pino.ui.actionSystem.Action;
import jp.gr.java_conf.alpius.pino.ui.actionSystem.ActionEvent;
import jp.gr.java_conf.alpius.pino.window.impl.JFxWindow;

public class HorizontalFlip implements Action {
    @Override
    public void performAction(ActionEvent e) {
        var canvas = ((JFxWindow) Pino.getApp().getWindow()).getRootContainer().getCanvas();
        canvas.setScaleX(canvas.getScaleX() * -1);
    }
}