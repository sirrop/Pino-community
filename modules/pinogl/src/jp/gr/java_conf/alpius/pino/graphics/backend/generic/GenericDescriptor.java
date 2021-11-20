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

package jp.gr.java_conf.alpius.pino.graphics.backend.generic;

import jp.gr.java_conf.alpius.pino.graphics.backend.AbstractPlatformDescriptor;
import jp.gr.java_conf.alpius.pino.graphics.backend.Platform;
import jp.gr.java_conf.alpius.pino.graphics.shader.ShaderType;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class GenericDescriptor extends AbstractPlatformDescriptor {
    public static final String BACKEND_NAME = "Generic";
    public static final String VERSION = "0.2.0-alpha";
    public static final String VENDOR = "shiro";

    public static final List<ShaderType> SUPPORTED_SHADER_TYPES = List.of();

    private final Supplier<GenericPlatform> provider = new Supplier<>() {
        private GenericPlatform platform;

        @Override
        public GenericPlatform get() {
            if (platform == null) {
                platform = new GenericPlatform(GenericDescriptor.this);
            }
            return platform;
        }
    };

    public GenericDescriptor() {
        super(BACKEND_NAME, VERSION, VENDOR);
    }

    @Override
    public Collection<ShaderType> getSupportedShaderTypes() {
        return SUPPORTED_SHADER_TYPES;
    }

    @Override
    public Supplier<? extends Platform> getProvider() {
        return provider;
    }
}
