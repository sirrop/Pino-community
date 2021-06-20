package jp.gr.java_conf.alpius.pino.core.history;

import java.util.Optional;

public interface History<T> {
    /**
     * 履歴を追加します。
     * 一般的にnullは許容されず、NullPointerExceptionを投げる場合があります。
     * 詳しい仕様は、実装のドキュメントを参照してください
     * @param instance 追加する履歴
     */
    void add(T instance);
    T current();
    T undo();
    T redo();
    boolean canUndo();
    boolean canRedo();
    Optional<T> undoIfCan();
    Optional<T> redoIfCan();
    int size();
    int capacity();
}
