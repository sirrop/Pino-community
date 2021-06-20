package jp.gr.java_conf.alpius.pino.core.history;

/**
 * 復元の際に例外が発生すると投げられます。
 */
public class RestoreException extends MementoException {
    public RestoreException(String mes, Throwable cause) {
        super(mes, cause);
    }

    public RestoreException(String mes) {
        super(mes);
    }

    public RestoreException(Throwable cause) {
        super(cause);
    }

    public RestoreException() {
        super();
    }
}
