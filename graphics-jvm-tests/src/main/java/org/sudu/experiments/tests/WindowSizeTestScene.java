package org.sudu.experiments.tests;

import org.sudu.experiments.Disposable;
import org.sudu.experiments.Scene0;
import org.sudu.experiments.SceneApi;
import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.input.KeyCode;
import org.sudu.experiments.input.KeyEvent;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;
import org.sudu.experiments.math.XorShiftRandom;

public class WindowSizeTestScene extends Scene0 {

  Disposable disposable;
  V4f mainColor;

  public WindowSizeTestScene(SceneApi api, double h) {
    super(api);
    disposable = api.input.onKeyPress.disposableAdd(this::onKeyPress);
    mainColor = Color.Cvt.fromHSV(h, 1, 1);
  }

  public WindowSizeTestScene(SceneApi api) {
    this(api, r());
  }

  @Override
  public void dispose() {
    disposable.dispose();
    super.dispose();
  }

  boolean onKeyPress(KeyEvent event) {
    if (event.keyCode == KeyCode.F1) {
      api.window.addChild("child", WindowSizeTestScene::new);
    }
    return false;
  }

  @Override
  public void paint() {
    super.paint();
    V2i horSize = new V2i(0, 1);
    V2i verSize = new V2i(1, 0);
    int left = -2, right = screen.x - 1;
    int top = 0, bottom = screen.y - 1;
    WglGraphics g = api.graphics;
    for (int i = 0; i < 2000; i++) {
      if (left < right && top <= bottom) {
        horSize.x = right - left + 1;
        g.drawRect(left, top, horSize, mainColor);
        left += 2;
      } else break;

      if (top < bottom && left <= right) {
        verSize.y = bottom - top + 1;
        g.drawRect(right, top, verSize, mainColor);
        top += 2;
      } else break;

      if (left < right && top <= bottom) {
        horSize.x = right - left + 1;
        g.drawRect(left, bottom, horSize, mainColor);
        right -= 2;
      } else break;

      if (top < bottom && left <= right) {
        verSize.y = bottom - top + 1;
        g.drawRect(left, top, verSize, mainColor);
        bottom -= 2;
      } else break;
    }
  }
  static double r() { return new XorShiftRandom().nextDouble(); }
}
