package jp.gr.java_conf.alpius.pino.graphics;

import jp.gr.java_conf.alpius.pino.core.annotaion.Beta;
import org.libj.util.function.BiIntConsumer;

import java.util.Arrays;
import java.util.function.IntConsumer;
import java.util.function.IntUnaryOperator;
import java.util.stream.IntStream;

@Beta
public class Context {
    private static final IntUnaryOperator INITIALIZER = it -> it;

    private IntStream stream;

    public Context(int workSize) {
        int[] array = new int[workSize];
        Arrays.parallelSetAll(array, INITIALIZER);
        stream = Arrays.stream(array).parallel();
    }

    public Context parallel() {
        if (!stream.isParallel()) {
            stream = stream.parallel();
        }
        return this;
    }

    public Context sequential() {
        if (stream.isParallel()) {
            stream = stream.sequential();
        }
        return this;
    }

    public boolean isParallel() {
        return stream.isParallel();
    }

    public void execute(IntConsumer task) {
        stream.forEach(task);
    }

    public void execute(int scansize, BiIntConsumer task) {
        stream.forEach(range2d(scansize, task));
    }

    private static IntConsumer range2d(int w, BiIntConsumer task) {
        return index -> task.accept(index % w, index / w);
    }
}
