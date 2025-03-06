package org.sudu.experiments.editor;

import org.sudu.experiments.Scene;
import org.sudu.experiments.SceneApi;
import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.editor.ui.colors.IdeaCodeColors;
import org.sudu.experiments.input.KeyEvent;
import org.sudu.experiments.input.MouseListener;
import org.sudu.experiments.math.Numbers;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;
import org.sudu.experiments.math.XorShiftRandom;

public class SinDemo extends Scene implements MouseListener  {

  final V4f bgColor = new V4f();
  final V4f color = new V4f(1,0,0,1);
  final DemoRect rect = new DemoRect();
  final V2i screen = new V2i();
  static boolean draw1000 = false;

  public SinDemo(SceneApi api) {
    super(api);
    api.input.onKeyPress.add(this::onKeyEvent);
    api.input.onMouse.add(this);

    rect.color.set(IdeaCodeColors.Darcula.editBg);

    rect.set(0, 0, 600, 60);
    t();
  }

  public void onResize(V2i size, float dpr) {
    screen.set(size);
    rect.pos.set((size.x - rect.size.x) / 2, (size.y - rect.size.y) / 2);
  }

  public void dispose() {}

  static V4f scaleParams(V4f origin, float scale) {
    V4f result = new V4f();
    UnderlineConstants.scaleSinParams(origin, scale, result);
    return result;
  }

  static V4f paramsDefault() {
    V4f params = new V4f();
    UnderlineConstants.sinParamsDefault(params);
    return params;
  }

  float Pi = (float) Math.PI;
  final V4f params100 = paramsDefault();
  final V4f params125x = scaleParams(params100, 1.25f);
  final V4f params133x = scaleParams(params100, 4.f / 3);
  final V4f params150x = scaleParams(params100, 1.5f);
  final V4f params166x = scaleParams(params100, 5.f / 3);
  final V4f params200x = scaleParams(params100, 2f);
  final V4f paramsHuge1 = new V4f(Pi / 24, 15f, 3, .5f);
  final V4f paramsHuge2 = new V4f(Pi / 12, 25f, 3, .5f);

  int[] yOffset = { -120,  -95, -70, -45, -20, 5, 80, 150 };

  V4f[] params = {
      params100,  params125x, params133x,
      params150x, params166x, params200x,
      paramsHuge1, paramsHuge2
  };

  @SuppressWarnings("unused")
  private void t() {
    double pow1x = .375;
    double lg375 = Numbers.log(0.5, pow1x);
    double check = Math.pow(pow1x, lg375);
    double lg3 = Numbers.log(lg375, 3);

    double check2 = Math.pow(pow1x, Math.pow(3, lg3));
    V4f check3 = scaleParams(paramsDefault(), 3);
  }

  public void paint() {
    WglGraphics g = api.graphics;
    g.clear(bgColor);
    g.enableBlend(true);
    float x0 = rect.pos.x;
    int halfH = rect.size.y / 2;

    for (int i = 0; i < params.length; i++) {
      int yShift = yOffset[i];
      V4f param = params[i];
      float offset = UnderlineConstants.offset(param);
      g.drawSin(rect.pos.x, rect.pos.y + yShift, rect.size,
          x0 + offset, rect.pos.y + halfH + yShift + offset,
          param, color);
    }

    if (draw1000) draw1000(g, x0, halfH);

    g.enableBlend(false);
  }

  private void draw1000(WglGraphics g, float x0, int halfH) {
    XorShiftRandom rng = new XorShiftRandom(1,2);
    for (int i = 0; i < 1000; i++) {
      int idx = rng.nextInt(params.length);
      int y = rng.nextInt(screen.y - rect.size.y);
      V4f param = params[idx];
      float offset = UnderlineConstants.offset(param);
      g.drawSin(rect.pos.x, y, rect.size,
          x0 + offset, halfH + y + offset,
          param, color);
    }
  }

  boolean onKeyEvent(KeyEvent event) {
    return false;
  }

  public boolean update(double timestamp) {
    return draw1000;
  }

}
