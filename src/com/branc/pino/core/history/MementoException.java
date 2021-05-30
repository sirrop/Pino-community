package com.branc.pino.core.history;

/**
 * Memento関連の何かしらの例外を示します。
 */
public class MementoException extends Exception {
    public MementoException(String mes, Throwable cause) {
        super(mes, cause);
    }

    public MementoException(String mes) {
        super(mes);
    }

    public MementoException(Throwable cause) {
        super(cause);
    }

    public MementoException() {
        super();
    }
}
