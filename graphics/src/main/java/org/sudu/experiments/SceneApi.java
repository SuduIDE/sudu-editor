package org.sudu.experiments;

import org.sudu.experiments.input.InputListeners;

public class SceneApi {
  public final WglGraphics graphics;
  public final InputListeners input;
  public final Window window;

  public SceneApi(WglGraphics graphics, InputListeners input, Window window) {
    this.graphics = graphics;
    this.input = input;
    this.window = window;
  }
}
