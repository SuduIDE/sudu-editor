package org.sudu.experiments;

import org.sudu.experiments.math.V2i;

import java.util.function.Consumer;

public class JvmGraphics extends WglGraphics {
  public JvmGraphics(GLApi.Context gl, V2i canvasSize, Runnable repaint) {
    super(gl, canvasSize, repaint);
  }

  public void loadImage(String src, Consumer<GL.Texture> onLoad) {}

  public Canvas createCanvas(int w, int h) {
    return null;
  }
}
