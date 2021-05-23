package com.branc.pino.service;

public interface ServiceContainer {
    <T> T getService(Class<T> serviceClass);
}
