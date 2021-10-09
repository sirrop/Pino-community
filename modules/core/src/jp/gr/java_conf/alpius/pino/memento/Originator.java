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
 * Mementoを生成し、Mementoを元に状態を復元するクラスです。
 */
public interface Originator {
    /**
     * Mementoを作成し返します。
     * <p>
     *     このインターフェースを実装するクラスを継承する場合はこのメソッドを適切にオーバーライドする必要があります。
     *     またその際、{@code super.createMemento()}を呼び出し、スーパークラスのMementoを作成してください。スーパークラスのMementoは、
     *     {@link Memento#getParent()}を使用して返すようにしてください。
     * </p>
     * @return このクラスの状態を保持するMemento
     * @see Originator#restore(Memento)
     * @see Memento
     */
    Memento<?> createMemento();

    /**
     * Mementoから状態を復元します。
     * <p>
     *     このインターフェースを実装するクラスを継承する場合は、このメソッドを適切にオーバーライドする必要があります。
     *     またその際、{@code super.restore(memento.getParent())}を必ず実行してください。
     *     実行のタイミングはそれぞれのクラスによって指定されます。
     * </p>
     * @param memento このクラスの状態を保持するMemento
     * @throws IncompatibleMementoException 指定されたMementoがこのクラスのMementoではない場合
     * @throws NullPointerException 引数がnullであった場合
     * @see Originator#createMemento()
     * @see Memento
     */
    void restore(Memento<?> memento);
}
