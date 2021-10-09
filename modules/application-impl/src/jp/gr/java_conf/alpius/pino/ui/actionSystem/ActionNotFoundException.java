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

package jp.gr.java_conf.alpius.pino.ui.actionSystem;

public class ActionNotFoundException extends Exception {
    private final String actionId;

    public ActionNotFoundException(String actionId) {
        super(String.format("An action[id: %s] was not found.", actionId));
        this.actionId = actionId;
    }

    public String getActionId() {
        return actionId;
    }
}
