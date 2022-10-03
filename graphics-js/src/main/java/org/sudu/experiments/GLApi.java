package org.sudu.experiments;

import org.sudu.experiments.js.JsCanvas;
import org.sudu.experiments.js.JsMemoryAccess;
import org.sudu.experiments.math.V4f;
import org.teavm.jso.JSMethod;
import org.teavm.jso.typedarrays.ArrayBufferView;
import org.teavm.jso.webgl.*;

public interface GLApi {
  interface Canvas {}
  interface Buffer extends WebGLBuffer {}

  interface Shader extends WebGLShader {}

  interface Program extends WebGLProgram {}

  interface UniformLocation extends WebGLUniformLocation {}

  interface Texture extends WebGLTexture {}

  interface Context extends WebGLRenderingContext {

    // webgl2 texture internal formats, for glTexStorage2D
    int R8    = 0x8229;
    int RG8   = 0x822B;
    int RGBA8 = 0x8058;
    // webgl2 texture formats, for texSubImage2D
    int RED   = 0x1903;

    @JSMethod("getRenderbufferParameter")
    int getRenderbufferParameteri(int target, int name);

    // shader api
    Shader createShader(int type);

    void deleteShader(Shader shader);

    void shaderSource(Shader shader, String source);

    void compileShader(Shader shader);

    @JSMethod("getShaderParameter")
    int getShaderParameteri(Shader shader, int name);

    void attachShader(Program program, Shader shader);

    String getShaderInfoLog(Shader shader);

    Program createProgram();

    void deleteProgram(Program program);

    void linkProgram(Program program);

    @JSMethod("getProgramParameter")
    int getProgramParameteri(Program program, int pname);

    String getProgramInfoLog(Program program);

    void bindAttribLocation(Program program, int index, String name);

    void useProgram(Program program);

    UniformLocation getUniformLocation(Program program, String name);

    // buffer api
    Buffer createBuffer();

    void deleteBuffer(Buffer buffer);

    void bindBuffer(int target, Buffer buffer);

    default void bufferData(Buffer buffer, float[] fData, int target) {
      bindBuffer(target, buffer);
      bufferData(target, JsMemoryAccess.bufferView(fData), STATIC_DRAW);
    }

    default void bufferData(Buffer buffer, byte[] bData, int target) {
      bindBuffer(target, buffer);
      bufferData(target, JsMemoryAccess.bufferView(bData), STATIC_DRAW);
    }

    default void bufferData(Buffer buffer, char[] cData, int target) {
      bindBuffer(target, buffer);
      bufferData(target, JsMemoryAccess.bufferView(cData), STATIC_DRAW);
    }

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
                       int width, int height,  int format, int type,
                       ArrayBufferView pixels);

    default void texSubImage2D(int target, int level, int xOffset, int yOffset,
                               int width, int height, int format, int type,
                               byte[] pixels
    ) {
      texSubImage2D(target, level, xOffset, yOffset, width, height, format, type,
          JsMemoryAccess.uInt8View(pixels));
    }

    default void texSubImage2D(int target, int level, int xOffset, int yOffset, int format, int type, Canvas canvas) {
      texSubImage2D(target, level, xOffset, yOffset, format, type, ((JsCanvas)canvas).element);
    }

    // uniforms:

    void uniform1i(UniformLocation location, int x);

    void uniform2f(UniformLocation location, float x, float y);

    void uniform4f(UniformLocation location, float x, float y, float z, float w);

    default void uniform4f(UniformLocation location, V4f v) {
      uniform4f(location, v.x, v.y, v.z, v.w);
    }

    default void checkError(String title) {
      int error = getError();
      if (error != 0) System.out.println(title + error);
    }
  }
}
