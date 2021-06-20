package jp.gr.java_conf.alpius.pino.ui.event;

public interface EventType<E extends Event> {
    EventType<? extends Event> getParent();
}
