package jp.gr.java_conf.alpius.pino.core.util;

public final class Disposer {
    private Disposer() {}

    public static Disposable newDisposable() {
        return newDisposable("newDisposable");
    }

    public static Disposable newDisposable(String name) {
        if (name == null || name.isBlank() || name.isEmpty()) {
            throw new IllegalArgumentException("name is null, empty or blank.");
        }
        return new Disposable() {
            @Override
            public void dispose() {

            }

            @Override
            public String toString() {
                return name;
            }
        };
    }

    private static final ObjectTree tree = new ObjectTree();

    public static void registerDisposable(Disposable parent, Disposable child) {
        tree.register(parent, child);
    }

    public static boolean isDisposed(Disposable disposable) {
        return tree.isDisposed(disposable);
    }

    public static void dispose(Disposable disposable) {
        tree.executeAll(disposable);
    }
}
