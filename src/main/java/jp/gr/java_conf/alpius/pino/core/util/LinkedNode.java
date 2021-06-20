package jp.gr.java_conf.alpius.pino.core.util;

public final class LinkedNode<T> {
    private T value;

    private LinkedNode<T> before;
    private LinkedNode<T> after;

    public LinkedNode(T value) {
        this.value = value;
    }

    public void append(LinkedNode<T> node) {
        this.after = node;
        node.before = this;
    }

    public void disconnectBehind() {
        if (after != null) {
            after.before = null;
            after = null;
        }
    }

    public void disconnectForward() {
        if (before != null) {
            before.after = null;
            before = null;
        }
    }

    public void removeThis() {
        if (after != null && before != null) {
            before.after = after;
            after.before = before;
            after = null;
            before = null;
        } else if (after != null) {
            after.before = null;
            after = null;
        } else if (before != null) {
            before.after = null;
            before = null;
        }
    }

    public void insertNext(LinkedNode<T> node) {
        if (after != null) {
            after.before = node;
            node.after = after;
            after = node;
        }
    }

    public void insertPrevious(LinkedNode<T> node) {
        if (before != null) {
            before.after = node;
            node.before = before;
            before = node;
        }
    }

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }

    public LinkedNode<T> next() {
        return after;
    }

    public LinkedNode<T> previous() {
        return before;
    }

    public boolean hasNext() {
        return after != null;
    }

    public boolean hasPrevious() {
        return before != null;
    }
}
