package jp.gr.java_conf.alpius.pino.service;

public interface ServiceContainer {
    <T> T getService(Class<T> serviceClass);
}
