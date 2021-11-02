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

package jp.gr.java_conf.alpius.pino.graphics.gl;

import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.Animator;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.GL2ES2.GL_FRAGMENT_SHADER;
import static com.jogamp.opengl.GL2ES2.GL_VERTEX_SHADER;

public class Main {
    private static final String vert = """
            attribute vec4 vPosition;
            void main() {
                gl_Position = vPosition;
            }
            """;

    private static final String frag = """
            precision mediump float;
            void main() {
                gl_FragColor = vec4(0.3, 0.3, 0.3, 1.0);
            }
            """;

    public static void main(String[] args) {
        var glp = GLProfile.get(GLProfile.GLES2);
        System.out.println("Profile: " + glp);
        var glc = new GLCapabilities(glp);
        var window = GLWindow.create(glc);
        window.setTitle("JogAmp JOGL test");
        window.setSize(800, 600);
        window.addGLEventListener(new GLListener(window));
        window.setVisible(true);
    }

    private static final class GLListener implements GLEventListener {
        private final Animator animator;
        private int gvPositionHandle;
        private final AtomicBoolean initialized = new AtomicBoolean(false);
        private int program;
        private final float[] vertices = {
                0, 0.5f, -0.5f, -0.5f, 0.5f, -0.5f
        };

        public GLListener(GLWindow window) {
            animator = new Animator(window);
            animator.start();
        }

        @Override
        public void init(GLAutoDrawable glAutoDrawable) {
            if (!initialized.getAndSet(true)) {
                var context = glAutoDrawable.getContext();
                var gl = context.getGL().getGLES2();
                context.enableGLDebugMessage(true);
                System.out.printf("GL Version: %s\n", context.getGLVersionNumber());
                System.out.printf("GLSL Version: %s\n", context.getGLSLVersionNumber());

                program = createProgram(gl, vert, frag);
                gl.glUseProgram(program);

                gvPositionHandle = gl.glGetAttribLocation(program, "vPosition");
                gl.glEnableVertexAttribArray(gvPositionHandle);
            }
        }

        @Override
        public void dispose(GLAutoDrawable glAutoDrawable) {
            animator.stop();
            glAutoDrawable.getGL().getGLES2().glDeleteProgram(program);
        }

        @Override
        public void display(GLAutoDrawable glAutoDrawable) {
            var gl = glAutoDrawable.getGL().getGLES2();

            gl.glClearColor(1, 1, 1, 1);
            gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            FloatBuffer buffer = ByteBuffer.allocateDirect(vertices.length * 4).asFloatBuffer();
            buffer.put(vertices).rewind();
            gl.glVertexAttribPointer(gvPositionHandle, 2, GL_FLOAT, false, 0, buffer);
            gl.glDrawArrays(GL_TRIANGLES, 0, 3);
            glAutoDrawable.swapBuffers();
        }

        @Override
        public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {

        }
    }

    private static int loadShader(GL2ES2 gl, int shaderType, String source) {
        int shader = gl.glCreateShader(shaderType);
        gl.glShaderSource(shader, 1, new String[] { source }, null);
        gl.glCompileShader(shader);
        return shader;
    }

    private static int createProgram(GL2ES2 gl, String vert, String frag) {
        int vertexShader = loadShader(gl, GL_VERTEX_SHADER, vert);
        int fragmentShader = loadShader(gl, GL_FRAGMENT_SHADER, frag);
        int program = gl.glCreateProgram();
        gl.glAttachShader(program, vertexShader);
        gl.glAttachShader(program, fragmentShader);
        gl.glLinkProgram(program);
        gl.glDeleteShader(vertexShader);
        gl.glDeleteShader(fragmentShader);
        return program;
    }
}
