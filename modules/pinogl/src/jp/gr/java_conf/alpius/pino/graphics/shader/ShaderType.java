package jp.gr.java_conf.alpius.pino.graphics.shader;

/**
 * Shaderの種類を表します。
 */
public record ShaderType(String desc) {
    public static final ShaderType TYPE_GLSL_VERTEX = new ShaderType("GLSL Vertex");
    public static final ShaderType TYPE_GLSL_FRAGMENT = new ShaderType("GLSL Fragment");
    public static final ShaderType TYPE_HLSL = new ShaderType("HLSL");
    public static final ShaderType TYPE_MSL = new ShaderType("MSL");
}
