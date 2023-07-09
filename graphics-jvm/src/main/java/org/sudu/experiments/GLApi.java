package org.sudu.experiments;

import org.sudu.experiments.math.V4f;

public interface GLApi {
  interface Canvas {}

  interface Buffer {}

  interface Shader {}

  interface Program {}

  interface UniformLocation {}

  interface Texture {}

  interface Context {

    int VENDOR                    = 0x1F00;
    int RENDERER                  = 0x1F01;
    int VERSION                   = 0x1F02;

    int NEAREST                   = 0x2600;
    int LINEAR                    = 0x2601;

    int NEAREST_MIPMAP_NEAREST    = 0x2700;
    int LINEAR_MIPMAP_NEAREST     = 0x2701;
    int NEAREST_MIPMAP_LINEAR     = 0x2702;
    int LINEAR_MIPMAP_LINEAR      = 0x2703;

    int TEXTURE_MAG_FILTER        = 0x2800;
    int TEXTURE_MIN_FILTER        = 0x2801;
    int TEXTURE_WRAP_S            = 0x2802;
    int TEXTURE_WRAP_T            = 0x2803;

    int REPEAT                    = 0x2901;
    int CLAMP_TO_EDGE             = 0x812F;

    int POINTS                    = 0x0000;
    int LINES                     = 0x0001;
    int LINE_LOOP                 = 0x0002;
    int LINE_STRIP                = 0x0003;
    int TRIANGLES                 = 0x0004;
    int TRIANGLE_STRIP            = 0x0005;
    int TRIANGLE_FAN              = 0x0006;

    int RGBA                           = 0x1908;
    // webgl2 texture internal formats, for glTexStorage2D
    int R8    = 0x8229;
    int RG8   = 0x822B;
    int RGBA8 = 0x8058;
    // webgl2 texture formats, for texSubImage2D
    int RED   = 0x1903;
    int TEXTURE_2D          = 0x0DE1;
    int TEXTURE0            = 0x84C0;

    int DEPTH_BUFFER_BIT    = 0x0100;
    int STENCIL_BUFFER_BIT  = 0x0400;
    int COLOR_BUFFER_BIT    = 0x4000;
    int CULL_FACE           = 0x0B44;
    int BLEND               = 0x0BE2;

    int ZERO                = 0;
    int ONE                 = 1;
    int SRC_COLOR           = 0x0300;
    int ONE_MINUS_SRC_COLOR = 0x0301;
    int SRC_ALPHA           = 0x0302;
    int ONE_MINUS_SRC_ALPHA = 0x0303;
    int DST_ALPHA           = 0x0304;
    int ONE_MINUS_DST_ALPHA = 0x0305;

    int UNSIGNED_BYTE       = 0x1401;
    int SHORT               = 0x1402;
    int UNSIGNED_SHORT      = 0x1403;
    int INT                 = 0x1404;
    int UNSIGNED_INT        = 0x1405;
    int FLOAT               = 0x1406;

    int ARRAY_BUFFER                   = 0x8892;
    int ELEMENT_ARRAY_BUFFER           = 0x8893;
    int ARRAY_BUFFER_BINDING           = 0x8894;
    int ELEMENT_ARRAY_BUFFER_BINDING   = 0x8895;
    int STATIC_DRAW                      = 0x88E4;
    int FRAGMENT_SHADER                  = 0x8B30;
    int VERTEX_SHADER                    = 0x8B31;
    int COMPILE_STATUS                   = 0x8B81;
    int LINK_STATUS                      = 0x8B82;

    String getParameterString(int name);

    int getParameteri(int name);

    float getParameterf(int name);

    int getRenderbufferParameteri(int target, int name);

    // shader api
    Shader createShader(int type);

    void deleteShader(Shader shader);

    void shaderSource(Shader shader, String source);

    void compileShader(Shader shader);

    int getShaderParameteri(Shader shader, int name);

    void attachShader(Program program, Shader shader);

    String getShaderInfoLog(Shader shader);

    Program createProgram();

    void deleteProgram(Program program);

    void linkProgram(Program program);

    int getProgramParameteri(Program program, int pname);

    String getProgramInfoLog(Program program);

    void bindAttribLocation(Program program, int index, String name);

    void useProgram(Program program);

    UniformLocation getUniformLocation(Program program, String name);

    // buffer api
    Buffer createBuffer();

    void deleteBuffer(Buffer buffer);

    void bindBuffer(int target, Buffer buffer);

    void bufferData(Buffer buffer, float[] fData, int target);

    void bufferData(Buffer buffer, byte[] bData, int target);
    void bufferData(Buffer buffer, char[] cData, int target);

    // textures
    Texture createTexture();
    void deleteTexture(Texture texture);

    void activeTexture(int texture);

    void bindTexture(int target, Texture texture);

    default void unboundTexture2d() {
      bindTexture(TEXTURE_2D, null);
    }

    void texStorage2D(int target, int levels, int internalformat, int width, int height);

    void texSubImage2D(int target, int level, int xOffset, int yOffset,
                               int width, int height, int format, int type,
                               byte[] pixels);

    void texSubImage2D(int target, int level, int xOffset, int yOffset, int format, int type, Canvas canvas);

    // uniforms:

    void uniform1i(UniformLocation location, int x);

    void uniform2f(UniformLocation location, float x, float y);

    void uniform4f(UniformLocation location, float x, float y, float z, float w);

    default void uniform4f(UniformLocation location, V4f v) {
      uniform4f(location, v.x, v.y, v.z, v.w);
    }

    int getError();
    default void checkError(String title) {
      int error = getError();
      if (error != 0) System.out.println(title + error);
    }

    void viewport(int x, int y, int width, int height);
    void scissor(int x, int y, int width, int height);
    void clearColor(float r, float g, float b, float alpha);
    void clear(int flags);
    void enable(int feature);
    void disable(int feature);

    void blendFuncSeparate(int srcRGB, int dstRGB, int srcAlpha, int dstAlpha);

    void drawElements(int mode, int count, int type, int offset);

    void drawArrays(int mode, int first, int count);

    void enableVertexAttribArray(int index);

    void disableVertexAttribArray(int index);

    void vertexAttribPointer(int index, int size, int type, boolean normalized, int stride, int offset);

    void texParameteri(int target, int pname, int param);
  }
}
