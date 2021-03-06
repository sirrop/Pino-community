/*
 * Copyright [2021] [shiro]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.gr.java_conf.alpius.pino.application.impl;

import java.util.Objects;

public final class Version implements Comparable<Version> {
    /**
     * 実装予定の機能について、未実装が存在する状態
     */
    public static final String VS_ALPHA = "alpha";

    /**
     * 実装予定の機能について、実装は完了しているがテストが不十分である状態
     */
    public static final String VS_BETA = "beta";

    /**
     * 十分なテストが行われ、安定に動作する状態
     */
    public static final String VS_RC = "RC";

    /**
     * 公開版であることを表す状態
     */
    public static final String VS_GA = "";

    public static final Version CURRENT_VERSION = new Version(0, 5, 1, VS_ALPHA);

    private final int major;
    private final int minor;
    private final int patch;
    private final String state;

    Version(int major, int minor, int patch, String state) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.state = state;
    }

    public int getMajorVersion() {
        return major;
    }

    public int getMinorVersion() {
        return minor;
    }

    public int getPatchVersion() {
        return patch;
    }

    public String getState() {
        return state;
    }

    @Override
    public int compareTo(Version o) {
        if (major > o.major) {
            return 1;
        } else if (major < o.major) {
            return -1;
        }

        if (minor > o.minor) {
            return 1;
        } else if (minor < o.minor) {
            return -1;
        }

        if (patch > o.patch) {
            return 1;
        } else if (patch < o.patch) {
            return -1;
        }

        return 0;
    }

    public String toString() {
        if (Objects.equals(state, VS_GA)) {
            return String.format("%d.%d.%d", major, minor, patch);
        } else {
            return String.format("%d.%d.%d-%s", major, minor, patch, state);
        }
    }
}
