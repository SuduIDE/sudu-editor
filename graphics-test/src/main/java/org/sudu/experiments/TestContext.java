package org.sudu.experiments;

@SuppressWarnings("SwitchStatementWithTooFewBranches")
public class TestContext implements GLApi.Context {

  int totalDrawCalls = 0;
  int totalPrimitives = 0;
  int stepDrawCalls = 0;
  int createTextureCalls = 0;

  GLApi.Program currentProgram;
  TestTexture[] currentTexture = new TestTexture[16];
  private int activeTexture;

  @Override
  public String getParameterString(int name) {
    return null;
  }

  @Override
  public int getParameteri(int name) {
    return 0;
  }

  @Override
  public float getParameterf(int name) {
    return 0;
  }

  @Override
  public int getRenderbufferParameteri(int target, int name) {
    return 0;
  }

  @Override
  public GLApi.Shader createShader(int type) {
    return null;
  }

  @Override
  public void deleteShader(GLApi.Shader shader) {}

  @Override
  public void shaderSource(GLApi.Shader shader, String source) {}

  @Override
  public void compileShader(GLApi.Shader shader) {}

  @Override
  public int getShaderParameteri(GLApi.Shader shader, int name) {
    return 0;
  }

  @Override
  public void attachShader(GLApi.Program program, GLApi.Shader shader) {}

  @Override
  public String getShaderInfoLog(GLApi.Shader shader) {
    return null;
  }

  @Override
  public GLApi.Program createProgram() {
    return null;
  }

  @Override
  public void deleteProgram(GLApi.Program program) {}

  @Override
  public void linkProgram(GLApi.Program program) {}

  @Override
  public int getProgramParameteri(GLApi.Program program, int pname) {
    return 0;
  }

  @Override
  public String getProgramInfoLog(GLApi.Program program) {
    return null;
  }

  @Override
  public void bindAttribLocation(GLApi.Program program, int index, String name) {}

  @Override
  public void useProgram(GLApi.Program program) {
    currentProgram = program;
  }

  @Override
  public GLApi.UniformLocation getUniformLocation(GLApi.Program program, String name) {
    return null;
  }

  @Override
  public GLApi.Buffer createBuffer() {
    return new GLApi.Buffer() {};
  }

  @Override
  public void deleteBuffer(GLApi.Buffer buffer) {}

  @Override
  public void bindBuffer(int target, GLApi.Buffer buffer) {}

  @Override
  public void bufferData(GLApi.Buffer buffer, float[] fData, int target) {  }

  @Override
  public void bufferData(GLApi.Buffer buffer, byte[] bData, int target) { }

  @Override
  public void bufferData(GLApi.Buffer buffer, char[] cData, int target) { }

  public GLApi.Texture createTexture() {
    createTextureCalls++;
    return new TestTexture(createTextureCalls);
  }

  @Override
  public void deleteTexture(GLApi.Texture texture) {}

  @Override
  public void activeTexture(int texture) {
    activeTexture = texture - TEXTURE0;
  }

  @Override
  public void bindTexture(int target, GLApi.Texture texture) {
    currentTexture[activeTexture] = (TestTexture) texture;
  }

  TestTexture currentTexture() {
    return currentTexture[activeTexture];
  }

  @Override
  public void texStorage2D(int target, int levels, int internalformat, int width, int height) {
    currentTexture().allocated++;
  }

  @Override
  public void texSubImage2D(int target, int level, int xOffset, int yOffset, int width, int height, int format, int type, byte[] pixels) {
    currentTexture().texSubImageCalls++;
  }

  @Override
  public void texSubImage2D(int target, int level, int xOffset, int yOffset, int format, int type, GLApi.Canvas canvas) {
    currentTexture().texSubImageCalls++;
  }

  @Override
  public void uniform1i(GLApi.UniformLocation location, int x) {}

  @Override
  public void uniform2f(GLApi.UniformLocation location, float x, float y) {}

  @Override
  public void uniform4f(GLApi.UniformLocation location, float x, float y, float z, float w) {}

  @Override
  public int getError() {
    return 0;
  }

  @Override
  public void checkError(String title) {}

  @Override
  public void viewport(int x, int y, int width, int height) {}

  @Override
  public void scissor(int x, int y, int width, int height) {}

  @Override
  public void clearColor(float r, float g, float b, float alpha) {}

  @Override
  public void clear(int flags) {}

  @Override
  public void enable(int feature) {}

  @Override
  public void disable(int feature) {}

  @Override
  public void blendFuncSeparate(int srcRGB, int dstRGB, int srcAlpha, int dstAlpha) {}

  private void drawCall(int numTriangles) {
    totalDrawCalls++;
    stepDrawCalls++;
    totalPrimitives += numTriangles;
  }

  static int triCount(int mode, int count) {
    return switch (mode) {
      case TRIANGLES -> count / 3;
      default -> 0;
    };
  }

  // drawIndexedPrimitive
  @Override
  public void drawElements(int mode, int count, int type, int offset) {
    drawCall(triCount(mode, count));
  }

  // drawPrimitive (not indexed)
  @Override
  public void drawArrays(int mode, int first, int count) {
    drawCall(triCount(mode, count));
  }

  @Override
  public void enableVertexAttribArray(int index) {}

  @Override
  public void disableVertexAttribArray(int index) {}

  @Override
  public void vertexAttribPointer(int index, int size, int type, boolean normalized, int stride, int offset) {}

  @Override
  public void texParameteri(int target, int pname, int param) {}

  public void debug() {
    Debug.consoleInfo("\tcreateTextureCalls: " + createTextureCalls);
    Debug.consoleInfo("\ttotalDrawCalls: " + totalDrawCalls);
    Debug.consoleInfo("\tstepDrawCalls: " + stepDrawCalls);
    stepDrawCalls = 0;
  }
}
