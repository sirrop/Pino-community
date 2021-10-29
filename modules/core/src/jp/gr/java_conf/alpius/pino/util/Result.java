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

package jp.gr.java_conf.alpius.pino.util;

import jp.gr.java_conf.alpius.pino.annotation.Beta;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 結果を表すクラスです。ラムダを使用したメソッドチェーンによる流暢な記述を提供します。<br />
 * 失敗時に例外を投げるメソッドを使用する場合に適したクラスです。メソッドの戻り値として使用されることは想定していません。
 * <h3>安全な関数呼び出し</h3>
 * <pre>
 *     Result.tryToRun(this::doSomething)   // 例外を投げる可能性のあるメソッド
 *           .map(this::mapper)             // 結果を使用してマッピングする
 *           .onSuccess(this::useResult)    // 結果を使用する
 *           .printStackTrace();            // 失敗した場合に、スタックトレースを表示する。成功した場合はなにも行わない。
 * </pre>
 * 上記の例では、スタックトレースの出力を{@link Result#printStackTrace()}を使用して行っていますが、任意のロガーを使用したい場合は、
 * {@link Result#onFailed(Consumer)}を使用することができます。
 * @param <T> 結果の型
 */
@Beta
public sealed abstract class Result<T> {
    /**
     * 失敗を表すResultのサブクラスです。
     * @param <T> 結果の型
     */
    private static final class Failed<T> extends Result<T> {
        private final Throwable cause;
        public Failed(Throwable cause) {
            this.cause = Objects.requireNonNull(cause);
        }

        @Override
        public T getResult() {
            return null;
        }

        @Override
        public T getResultOrElse(T defaultValue) {
            return defaultValue;
        }

        @Override
        public T getResultOrElse(Supplier<? extends T> lazyDefaultValue) {
            return lazyDefaultValue.get();
        }

        @Override
        public T getOrThrow() throws Throwable {
            throw cause;
        }

        @Override
        public <X extends Throwable> T getOrThrow(Function<? super Throwable, ? extends X> exceptionFilter) throws X {
            throw exceptionFilter.apply(cause);
        }

        @Override
        public boolean isSuccess() {
            return false;
        }

        @Override
        public boolean isFailed() {
            return true;
        }

        @Override
        public Result<T> onSuccess(Consumer<? super T> action) {
            return this;
        }

        @Override
        public Result<T> onFailed(Consumer<Throwable> action) {
            action.accept(cause);
            return this;
        }

        @Override
        public Throwable getCause() {
            return cause;
        }

        @Override
        public Result<T> printStackTrace(PrintStream out) {
            cause.printStackTrace(out);
            return this;
        }

        @Override
        public Result<T> printStackTrace(PrintWriter out) {
            cause.printStackTrace(out);
            return this;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <R> Result<R> map(ThrowableFunction<? super T, ? extends R> mapper) {
            return (Result<R>) this;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <R> Result<R> flatMap(Function<? super T, Result<R>> mapper) {
            return (Result<R>) this;
        }
    }

    /**
     * 成功を表すResultのサブクラスです。
     * @param <T> 結果の型
     */
    private static final class Success<T> extends Result<T> {
        // 戻り値がvoidのメソッドを実行した際の成功時の結果として使用されます。
        public static final Success<Void> EMPTY = new Success<>(null);

        private final T result;

        public Success(T result) {
            this.result = result;
        }

        @Override
        public T getResult() {
            return result;
        }

        @Override
        public T getResultOrElse(T defaultValue) {
            return result;
        }

        @Override
        public T getResultOrElse(Supplier<? extends T> lazyDefaultValue) {
            return result;
        }

        @Override
        public T getOrThrow() {
            return result;
        }

        @Override
        public <X extends Throwable> T getOrThrow(Function<? super Throwable, ? extends X> exceptionFilter) {
            return result;
        }

        @Override
        public boolean isSuccess() {
            return true;
        }

        @Override
        public boolean isFailed() {
            return false;
        }

        @Override
        public Result<T> onSuccess(Consumer<? super T> action) {
            action.accept(result);
            return this;
        }

        @Override
        public Result<T> onFailed(Consumer<Throwable> action) {
            return this;
        }

        @Override
        public Throwable getCause() {
            return null;
        }

        @Override
        public Result<T> printStackTrace(PrintStream out) {
            return this;
        }

        @Override
        public Result<T> printStackTrace(PrintWriter out) {
            return this;
        }

        @Override
        public <R> Result<R> map(ThrowableFunction<? super T, ? extends R> mapper) {
            return Result.tryToRun(() -> mapper.apply(result));
        }

        @Override
        public <R> Result<R> flatMap(Function<? super T, Result<R>> mapper) {
            return mapper.apply(result);
        }
    }

    /**
     * 失敗する可能性のある処理を実行します。
     * @param action 失敗する可能性のある処理
     * @param <T> 結果の型
     * @return 結果
     */
    public static <T> Result<T> tryToRun(Callable<T> action) {
        try {
            return new Success<>(action.call());
        } catch (Throwable t) {
            return new Failed<>(t);
        }
    }

    public static Result<Void> tryToRun(ThrowableRunnable action) {
        try {
            action.run();
            return Success.EMPTY;
        } catch (Throwable t) {
            return new Failed<>(t);
        }
    }

    public abstract T getResult();
    public abstract T getResultOrElse(T defaultValue);
    public abstract T getResultOrElse(Supplier<? extends T> lazyDefaultValue);
    public abstract T getOrThrow() throws Throwable;
    public abstract <X extends Throwable> T getOrThrow(Function<? super Throwable, ? extends X> exceptionFilter) throws X;
    public abstract boolean isSuccess();
    public abstract boolean isFailed();

    public abstract Result<T> onSuccess(Consumer<? super T> action);
    public abstract Result<T> onFailed(Consumer<Throwable> action);

    public abstract Throwable getCause();

    public Result<T> printStackTrace() {
        return printStackTrace(System.err);
    }
    public abstract Result<T> printStackTrace(PrintStream out);
    public abstract Result<T> printStackTrace(PrintWriter out);

    public void last(Runnable action) {
        action.run();
    }

    public abstract <R> Result<R> map(ThrowableFunction<? super T, ? extends R> mapper);
    public abstract <R> Result<R> flatMap(Function<? super T, Result<R>> mapper);

    public String toString() {
        if (isSuccess()) {
            return String.format("Success[result=%s]", getResult());
        } else {
            return String.format("Failed[cause=%s, message=%s]", getCause().getClass().getSimpleName(), getCause().getCause().getMessage());
        }
    }
}
