package jp.gr.java_conf.alpius.pino.core.util;

import java.util.EventListener;

public interface UpdateListener<T> extends EventListener {
    void updated(T source);
}
