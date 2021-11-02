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

package jp.gr.java_conf.alpius.pino.project.impl;

import jp.gr.java_conf.alpius.pino.disposable.Disposable;

import java.awt.*;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

/**
 * 投げ縄ツールなどにより編集される「選択機能」を補助するProject用のServiceです
 */
public interface SelectionManager extends Disposable {
    Shape get();
    void set(Shape shape);
    Shape updateAndGet(UnaryOperator<Shape> operator);
    void addListener(Consumer<Shape> listener);
    void removeListener(Consumer<Shape> listener);
}
