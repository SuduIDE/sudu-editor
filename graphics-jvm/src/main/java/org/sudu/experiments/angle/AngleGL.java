package org.sudu.experiments.angle;

import org.sudu.experiments.CString;
import org.sudu.experiments.GLApi;

class Handle {
  int handle; Handle(int h) { handle = h; }
  public String toString() { return getClass().getSimpleName() + " #" + handle; }
}

class Shader extends Handle implements GLApi.Shader { Shader(int h) { super(h); }}
class Buffer  extends Handle implements GLApi.Buffer { Buffer(int h) { super(h); }}
class Program extends Handle implements GLApi.Program { Program(int h) { super(h); }}
class Texture extends Handle implements GLApi.Texture { Texture(int h) { super(h); }}
class UniformLocation extends Handle implements GLApi.UniformLocation { UniformLocation(int h) { super(h); }}

public abstract class AngleGL implements GLApi.Context {

  @Override
  public String getParameterString(int name) {
    return CString.fromNativeStringNullable(getString(name));
  }

  public native int getParameteri(int name);

  public native float getParameterf(int name);

  static native int glCreateShader(int type);

  @Override
  public GLApi.Shader createShader(int type) {
    return new Shader(glCreateShader(type));
  }

  static native void glDeleteShader(int shader);

  @Override
  public void deleteShader(GLApi.Shader sh) {
    Shader shader = (Shader) sh;
    glDeleteShader(shader.handle);
    shader.handle = 0;
  }

  static native void glShaderSource(int shader, byte[] string);

  @Override
  public void shaderSource(GLApi.Shader shader, String source) {
    int handle = ((Shader) shader).handle;
    glShaderSource(handle, CString.toAsciiCString(source));
  }

  static native void glCompileShader(int shader);

  @Override
  public void compileShader(GLApi.Shader shader) {
    int handle = ((Shader) shader).handle;
    glCompileShader(handle);
  }

  static native int glGetShaderiv(int shader, int pname);

  @Override
  public int getShaderParameteri(GLApi.Shader shader, int name) {
    int handle = ((Shader) shader).handle;
    return glGetShaderiv(handle, name);
  }

  static native void glAttachShader(int program, int shader);

  @Override
  public void attachShader(GLApi.Program program, GLApi.Shader shader) {
    int pHandle = ((Program) program).handle;
    int sHandle = ((Shader) shader).handle;
    glAttachShader(pHandle, sHandle);
  }

  static native int glGetShaderInfoLog(int shader, byte[] buffer);

  @Override
  public String getShaderInfoLog(GLApi.Shader shader) {
    int handle = ((Shader) shader).handle;
    byte[] kByte = new byte[1024];
    int len = glGetShaderInfoLog(handle, kByte);
    return new String(kByte, 0, len);
  }

  static native int glCreateProgram();

  @Override
  public GLApi.Program createProgram() {
    return new Program(glCreateProgram());
  }

  static native void glDeleteProgram(int program);

  @Override
  public void deleteProgram(GLApi.Program program) {
    int handle = ((Program) program).handle;
    glDeleteProgram(handle);
  }

  static native void glLinkProgram(int program);

  @Override
  public void linkProgram(GLApi.Program program) {
    int handle = ((Program) program).handle;
    glLinkProgram(handle);
  }

  static native int getProgramiv(int program, int pname);

  @Override
  public int getProgramParameteri(GLApi.Program program, int pname) {
    int handle = ((Program) program).handle;
    return getProgramiv(handle, pname);
  }

  static native int glGetProgramInfoLog(int program, byte[] buffer);

  @Override
  public String getProgramInfoLog(GLApi.Program program) {
    int handle = ((Program) program).handle;
    byte[] kByte = new byte[1024];
    int len = glGetProgramInfoLog(handle, kByte);
    return new String(kByte, 0, len);
  }

  static native void glBindAttribLocation(int program, int index, byte[] name);

  @Override
  public void bindAttribLocation(GLApi.Program program, int index, String name) {
    int handle = ((Program) program).handle;
    glBindAttribLocation(handle, index, CString.toAsciiCString(name));
  }

  static native void glUseProgram(int program);

  @Override
  public void useProgram(GLApi.Program program) {
    int handle = ((Program) program).handle;
    glUseProgram(handle);
  }

  static native int glGetUniformLocation(int program, byte[] name);

  @Override
  public GLApi.UniformLocation getUniformLocation(GLApi.Program program, String name) {
    int handle = ((Program) program).handle;
    int location = glGetUniformLocation(handle, CString.toAsciiCString(name));
    if (location == -1) throw new RuntimeException("glGetUniformLocation(" + handle + ", " + name + ") failed");
    return new UniformLocation(location);
  }

  // static_assert(sizeof(GLuint) == sizeof(jint));

  static native int glGenBuffer();

  @Override
  public GLApi.Buffer createBuffer() {
    int handle = glGenBuffer();
    return new Buffer(handle);
  }

  static native void glDeleteBuffer(int buffer);

  @Override
  public void deleteBuffer(GLApi.Buffer buffer) {
    int handle = ((Buffer) buffer).handle;
    glDeleteBuffer(handle);
  }

  // static_assert(sizeof(GLenum) == sizeof(jint)
  static native void glBindBuffer(int target, int buffer);

  @Override
  public void bindBuffer(int target, GLApi.Buffer buffer) {
    int handle = buffer != null ? ((Buffer) buffer).handle : 0;
    glBindBuffer(target, handle);
  }

  static native void glBufferData(int target, float[] data, int usage);
  static native void glBufferData(int target, char[] data, int usage);
  static native void glBufferData(int target, byte[] data, int usage);

  @Override
  public void bufferData(GLApi.Buffer buffer, float[] fData, int target) {
    int handle = ((Buffer) buffer).handle;
    glBindBuffer(target, handle);
    glBufferData(target, fData, STATIC_DRAW);
  }

  @Override
  public void bufferData(GLApi.Buffer buffer, byte[] bData, int target) {
    int handle = ((Buffer) buffer).handle;
    glBindBuffer(target, handle);
    glBufferData(target, bData, STATIC_DRAW);
  }

  @Override
  public void bufferData(GLApi.Buffer buffer, char[] cData, int target) {
    int handle = ((Buffer) buffer).handle;
    glBindBuffer(target, handle);
    glBufferData(target, cData, STATIC_DRAW);
  }

  static native int glGenTexture();

  @Override
  public GLApi.Texture createTexture() {
    int handle = glGenTexture();
    return new Texture(handle);
  }

  static native void glDeleteTexture(int handle);

  @Override
  public void deleteTexture(GLApi.Texture texture) {
    int handle = ((Texture) texture).handle;
    glDeleteTexture(handle);
  }

  static native void glBindTexture(int target, int texture);

  @Override
  public void bindTexture(int target, GLApi.Texture texture) {
    int handle = ((Texture) texture).handle;
    glBindTexture(target, handle);
  }

  @Override
  public void texSubImage2D(int target, int level, int xOffset, int yOffset, int format, int type, GLApi.Canvas canvas) {
//    if (canvas instanceof ImageCanvas win32Canvas) {
//      texSubImage2D(target, level, xOffset, yOffset,
//          win32Canvas.width(), win32Canvas.height(),
//          format, type, win32Canvas.image());
//    } else {
      throw new RuntimeException("unsupported type " + canvas);
//    }
  }

  static native void glUniform1i(int location, int v0);

  @Override
  public void uniform1i(GLApi.UniformLocation location, int x) {
    int handle = ((UniformLocation) location).handle;
    glUniform1i(handle, x);
  }

  static native void glUniform2f(int location, float v0, float v1);

  @Override
  public void uniform2f(GLApi.UniformLocation location, float x, float y) {
    int handle = ((UniformLocation) location).handle;
    glUniform2f(handle, x, y);
  }

  static native void glUniform4f(int location, float v0, float v1, float v2, float v3);

  @Override
  public void uniform4f(GLApi.UniformLocation location, float x, float y, float z, float w) {
    int handle = ((UniformLocation) location).handle;
    glUniform4f(handle, x, y, z, w);
  }

  @Override
  public native void activeTexture(int texture);

  @Override
  public native void texStorage2D(int target, int levels, int internalformat, int width, int height);

  @Override
  public native void texSubImage2D(
      int target, int level, int xOffset, int yOffset,
      int width, int height, int format, int type,
      byte[] pixels
  );

  public static native void texSubImage2DPtr(
      int target, int level, int xOffset, int yOffset,
      int width, int height, int format, int type,
      long ptr
  );

  // todo
  public static native void texSubImage2dWicBitmap(
      int target, int level, int xOffset, int yOffset,
      int width, int height, int format, int type,
      long pWicBitmap, int lockDataLength
  );

  @Override
  public native int getError();

  @Override
  public native void viewport(int x, int y, int width, int height);

  @Override
  public native void scissor(int x, int y, int width, int height);

  @Override
  public native void clearColor(float r, float g, float b, float alpha);

  @Override
  public native void clear(int flags);

  @Override
  public native void enable(int feature);

  @Override
  public native void disable(int feature);

  @Override
  public native void blendFuncSeparate(int srcRGB, int dstRGB, int srcAlpha, int dstAlpha);

  @Override
  public native void drawElements(int mode, int count, int type, int offset);

  @Override
  public native void drawArrays(int mode, int first, int count);

  @Override
  public native void enableVertexAttribArray(int index);

  @Override
  public native void disableVertexAttribArray(int index);

  @Override
  public native void vertexAttribPointer(int index, int size, int type, boolean normalized, int stride, int offset);

  @Override
  public native void texParameteri(int target, int pname, int param);

  public native int getRenderbufferParameteri(int target, int name);

  static native long getString(int name);

  public static native void readPixels(
      int x, int y, int width, int height,
      int format, int type, byte[] pixels);
}
