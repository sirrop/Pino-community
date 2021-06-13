package com.branc.pino.application;

public class ApplicationError extends Error {
    public ApplicationError() {
    }

    public ApplicationError(String mes) {
        super(mes);
    }

    public ApplicationError(String mes, Throwable cause) {
        super(mes, cause);
    }

    public ApplicationError(Throwable cause) {
        super(cause);
    }
}
