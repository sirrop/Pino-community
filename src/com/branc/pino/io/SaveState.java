package com.branc.pino.io;

import java.nio.file.Path;

/**
 * 保存の状態を保持する単なるコンテナです。
 *
 * <p>
 *     <b>このオブジェクトが保存を担当するわけではありません。</b>実際に保存を担当するクラス、またはそのクラスを使用するクラスによって適切に管理される必要があります。
 * </p>
 */
public interface SaveState {
    Path getLastSavedPath();
    void setLastSavedPath(Path path);
}
