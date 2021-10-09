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

package jp.gr.java_conf.alpius.pino.memento;

/**
 * Originatorの状態を保持するクラスです
 * @param <T> Originator
 */
public interface Memento<T extends Originator> {
    /**
     * @return このオブジェクトが状態を保持するOriginatorへの参照
     */
    T getOriginator();

    /**
     * @return スーパークラスのMemento。存在しない場合はnull
     */
    Memento<?> getParent();
}
