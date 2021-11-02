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

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jp.gr.java_conf.alpius.pino.graphics.CompositeFactory;

import static jp.gr.java_conf.alpius.pino.graphics.AlphaBlend.Mode.*;


public class BlendModeRegistry {
    public static BlendModeRegistry getInstance() {
        return Pino.getApp().getService(BlendModeRegistry.class);
    }

    public BlendModeRegistry() {
        register(SRC_OVER);
        register(PLUS);
        register(MULTIPLY);
        register(OVERLAY);
        register(SCREEN);
        register(DARKEN);
        register(LIGHTEN);
        register(COLOR_DODGE);
        register(COLOR_BURN);
        register(HARD_LIGHT);
        register(SOFT_LIGHT);
        register(DIFFERENCE);
        register(EXCLUSION);
        register(LINEAR_BURN);
        register(VIVID_LIGHT);
        register(PIN_LIGHT);
        register(DIVIDE);
    }

    private final ObservableList<CompositeFactory> registeredBlendMode = FXCollections.observableArrayList();

    private void register(CompositeFactory factory) {
        if (factory != null && !registeredBlendMode.contains(factory)) {
            registeredBlendMode.add(factory);
        }
    }

    public ObservableList<CompositeFactory> getAvailableBlendModeList() {
        return registeredBlendMode;
    }
}
