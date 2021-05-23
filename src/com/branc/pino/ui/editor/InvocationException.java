package com.branc.pino.ui.editor;


/**
 * JavaBeans Propertyのメソッド呼び出しで発生した例外をラップします。
 * 多くの場合は、{@link java.lang.reflect.InvocationTargetException}または{@link IllegalAccessException}をラップします
 */
public class InvocationException extends RuntimeException {
    public InvocationException() {
        super();
    }

    public InvocationException(Throwable cause) {
        super(cause);
    }

    public InvocationException(String mes) {
        super(mes);
    }

    public InvocationException(String mes, Throwable cause) {
        super(mes, cause);
    }
}
