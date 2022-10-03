package org.sudu.experiments;

import org.sudu.experiments.input.Input;

public class SceneApi {
  public final WglGraphics graphics;
  public final Input input;
  public final Window window;

  public SceneApi(WglGraphics graphics, Input input, Window window) {
    this.graphics = graphics;
    this.input = input;
    this.window = window;
  }
}
