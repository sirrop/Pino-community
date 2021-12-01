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

package jp.gr.java_conf.alpius.pino.graphics.brush.context;

@FunctionalInterface
public interface GaussianFunction {
    float apply(int x, int y);
    double SQRT_2PI = Math.sqrt(2 * Math.PI);
    static GaussianFunction create(double deviation) {
        // 効率化のため、予め計算できる部分をキャッシュしています
        var deviation2 = deviation * deviation * 2;
        var sqrt2piTimesDeviation = SQRT_2PI * deviation;
        return (x, y) -> (float) (Math.exp( -(x * x  + y * y) / deviation2 ) / sqrt2piTimesDeviation);
    }
}
