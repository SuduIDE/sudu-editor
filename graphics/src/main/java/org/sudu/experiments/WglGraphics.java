package org.sudu.experiments;

import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;

import java.util.function.Consumer;

public abstract class WglGraphics {
  public final CanvasFactory canvasFactory;
  public final Canvas mCanvas;
  public final GLApi.Context gl;
  final GL.TextureContext tc;
  final boolean isWGL2;

  final Shaders.ConstColor shConstColor;
  final Shaders.SimpleTexture shSimpleTexture;
  final Shaders.Shader2d shShowUV;
  final Shaders.TextureShowAlpha shTextureShowAlpha;
  final Shaders.Text shText;
  final Shaders.GrayIcon shGrayIcon;
  final Shaders.Shader2d[] all2dShaders;
  private GL.Mesh rectangle;

  // state
  final V2i clientRect = new V2i();
  private GL.Program currentShader;
  private int attributeMask = 0;
  private boolean blendState;

  public interface CanvasFactory {
    Canvas create(int w, int h);
  }

  public WglGraphics(GLApi.Context gl, CanvasFactory canvasFactory) {
    this.canvasFactory = canvasFactory;
    String version = gl.getParameterString(gl.VERSION);
    Debug.consoleInfo("[Graphics] GL version: " + version);
    this.gl = gl;
    mCanvas = canvasFactory.create(4, 4);
    rectangle = GL.createRectangle(gl);
    isWGL2 = version.startsWith("WebGL 2");
    tc = new GL.TextureContext(gl);

    all2dShaders = new Shaders.Shader2d[] {
        shConstColor = new Shaders.ConstColor(gl),
        shSimpleTexture = new Shaders.SimpleTexture(gl),
        shShowUV = new Shaders.ShowUV(gl),
        shTextureShowAlpha = new Shaders.TextureShowAlpha(gl),
        shText = new Shaders.Text(gl),
        shGrayIcon = new Shaders.GrayIcon(gl),
    };

    gl.checkError("WebGraphics::ctor finish");
  }

  public void setClientRect(V2i size) {
    clientRect.set(size);
  }

  public void dispose() {
    if (rectangle != null) {
      rectangle.dispose();
      rectangle = null;
    }
    mCanvas.dispose();
  }

  public FontDesk fontDesk(String name, int size) {
    return fontDesk(name, size, FontDesk.WEIGHT_REGULAR, FontDesk.STYLE_NORMAL);
  }

  public abstract FontDesk fontDesk(String family, float size, int weight, int style);

  public final Canvas createCanvas(int w, int h) {
    return canvasFactory.create(w, h);
  }

  public void setViewPortToClientRect() {
//    System.out.println("setViewport: 0,0, " + clientRect.x + ", " + clientRect.y);
    gl.viewport(0, 0, clientRect.x, clientRect.y);
  }

  // WglGraphics is shared between different windows and angle contexts
  // there are two options for state management between context switch
  //  - save and restore state on context switch
  //  - or reset state to its default before to present a window
  // lets try the latter for now

  public void resetState() {
    enableBlend(false);
    attributeMask = GL.Mesh.bindAttributes(attributeMask, 0, gl);
    currentShader = null;
  }

  public void restoreState() {}

  public void clear(V4f color) {
    gl.clearColor(color.x, color.y, color.z, color.w);
    gl.clear(GLApi.Context.COLOR_BUFFER_BIT);
  }

  public boolean enableBlend(boolean en) {
    if (en == blendState) return en;
    if (en) {
      gl.enable(GLApi.Context.BLEND);
      gl.blendFuncSeparate(GLApi.Context.SRC_ALPHA, GLApi.Context.ONE_MINUS_SRC_ALPHA, GLApi.Context.ONE, GLApi.Context.ONE);
    } else {
      gl.disable(GLApi.Context.BLEND);
    }
    boolean oldMode = blendState;
    blendState = en;
    return oldMode;
  }

  public void drawRect(int x, int y, V2i size, V4f color) {
    setShader(shConstColor);
    shConstColor.setPosition(gl, x, y, size, clientRect);
    shConstColor.setColor(gl, color);
    attributeMask = rectangle.draw(attributeMask);
  }

  public void drawRect(int x, int y, V2i size, GL.Texture texture) {
    setShader(shSimpleTexture);
    shSimpleTexture.setPosition(gl, x, y, size, clientRect);
    shSimpleTexture.setTexture(gl, texture);
    attributeMask = rectangle.draw(attributeMask);
  }

  public void drawRectShowAlpha(int x, int y, V2i size, GL.Texture texture, float contrast) {
    setShader(shTextureShowAlpha);
    shTextureShowAlpha.setPosition(gl, x, y, size, clientRect);
    shTextureShowAlpha.setTexture(gl, texture);
    shTextureShowAlpha.setContrast(gl, contrast);
    attributeMask = rectangle.draw(attributeMask);
  }

  public void drawText(int x, int y, V2i size, V4f texRect,
                       GL.Texture texture, V4f color, V4f bgColor, float contrast
  ) {
    setShader(shText);
    shText.setPosition(gl, x, y, size, clientRect);
    shText.setTexture(gl, texture);
    shText.setTextureRect(gl, texture, texRect);
    shText.set(gl, color, bgColor, contrast);
    attributeMask = rectangle.draw(attributeMask);
  }

  public void drawRectGrayIcon(int x, int y, V2i size, GL.Texture texture, V4f bColor, V4f fColor, float contrast) {
    setShader(shGrayIcon);
    shGrayIcon.setPosition(gl, x, y, size, clientRect);
    shGrayIcon.setTexture(gl, texture);
    shGrayIcon.set(gl, bColor, fColor, contrast);
    attributeMask = rectangle.draw(attributeMask);
  }

  public void drawRectUV(int x, int y, V2i size) {
    setShader(shShowUV);
    shShowUV.setPosition(gl, x, y, size, clientRect);
    attributeMask = rectangle.draw(attributeMask);
  }

  public GL.Texture createTexture() {
    return new GL.Texture(tc);
  }

  public void checkError(String title) {
    gl.checkError(title);
  }

  private void setShader(GL.Program shader) {
    if (shader != currentShader) {
      gl.useProgram(shader.program);
      currentShader = shader;
    }
  }

  public abstract void loadImage(String src, Consumer<GL.Texture> onLoad);
}