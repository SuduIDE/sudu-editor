package org.sudu.experiments.editor;

import org.sudu.experiments.Scene0;
import org.sudu.experiments.SceneApi;
import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.editor.ui.colors.IdeaCodeColors;
import org.sudu.experiments.input.MouseListener;
import org.sudu.experiments.math.Numbers;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;
import org.sudu.experiments.math.XorShiftRandom;
import org.sudu.experiments.ui.WindowPaint;

import static org.sudu.experiments.DprUtil.toPx;

public class SinDemo extends Scene0 implements MouseListener  {

  static boolean draw1000 = false;

  final V4f color = new V4f(1,0,0,1);
  final DemoRect rect = new DemoRect();
  final UserXY userXY = new UserXY();
  final ParamSet pSet = new ParamSet();
  final V4f[] params = new V4f[pSet.params0.length];

  final V2i frameT = new V2i();
  final V2i framePos = new V2i();
  final V2i frameSize = new V2i();

  public SinDemo(SceneApi api) {
    super(api);
    clearColor.set(0,0,0,1);
    api.input.onKeyPress.add(userXY::onKeyEvent);
    api.input.onMouse.add(this);

    rect.color.set(IdeaCodeColors.Darcula.editBg);
    ParamSet.t();
  }

  public void onResize(V2i size, float dpr) {
    this.dpr = dpr;
    this.screen.set(size);
    rect.size.set(toPx(600, dpr), 0);
    rect.pos.set((size.x - rect.size.x) / 2, 0);
    for (int i = 0; i < params.length; i++) {
      params[i] = scaleParams(pSet.params0[i], dpr);
    }
  }

  public void paint() {
    WglGraphics g = api.graphics;
    g.clear(clearColor);
    g.enableBlend(true);

    int vDelta = toPx(10, dpr);
    int vSize = params.length > 0 ? -vDelta : 0;

    for (V4f param : params) {
      int extend = UnderlineConstants.boxExtend(param);
      int ySize = extend * 2 * 4 / 3;
      vSize += ySize + vDelta;
    }

    float x0 = rect.pos.x;
    int startY = (screen.y - vSize) / 2;

    for (int i = 0, y = startY; i < params.length; i++) {
      V4f param = params[i];
      int extend = UnderlineConstants.boxExtend(param);
      int ySize = extend * 2 * 4 / 3;
      int drawSize = extend * 2 + 2;
      int yDraw = y + (ySize - drawSize) / 2;
      float offset = UnderlineConstants.offset(param);
      rect.size.y = drawSize;
      g.drawSin(rect.pos.x, yDraw, rect.size,
          x0 + offset, yDraw + extend + offset, param, color, 0);
      y += ySize + vDelta;
    }

    frameSize.set(rect.size.x + 4, vSize + 4);
    framePos.set(rect.pos.x - 2, startY - 2);
    WindowPaint.drawInnerFrame(g, frameSize, framePos,
        IdeaCodeColors.ElementsDark.unused.v.colorF, 1, frameT);

    if (draw1000) draw1000(g, x0, vSize);

    g.enableBlend(false);
  }

  private void draw1000(WglGraphics g, float x0, int vSize) {
    int startY = (screen.y - vSize) / 2;
    XorShiftRandom rng = new XorShiftRandom(1,2);
    for (int i = 0; i < 1000; i++) {
      int idx = rng.nextInt(params.length);
      V4f param = params[idx];
      int extend = UnderlineConstants.boxExtend(param);
      int y = rng.nextInt(vSize - extend * 2);
      rect.size.y = extend * 2;
      float offset = UnderlineConstants.offset(param);
      g.drawSin(rect.pos.x, y + startY, rect.size,
          x0 + offset, y + startY + extend + offset,
          param, color, 0);
    }
  }

  public boolean update(double timestamp) {
    return draw1000;
  }

  static V4f scaleParams(V4f origin, float scale) {
    V4f result = new V4f();
    UnderlineConstants.scaleSinParams(origin, scale, result);
    return result;
  }

  static class ParamSet {
    float Pi = (float) Math.PI;
    final V4f params100 = paramsDefault();
    final V4f params125x = scaleParams(params100, 1.25f);
    final V4f params133x = scaleParams(params100, 4.f / 3);
    final V4f params150x = scaleParams(params100, 1.5f);
    final V4f params166x = scaleParams(params100, 5.f / 3);
    final V4f params200x = scaleParams(params100, 2f);
    final V4f paramsHuge1 = new V4f(Pi / 24, 15f, 3, .5f);
    final V4f paramsHuge2 = new V4f(Pi / 12, 25f, 3, .5f);

    final V4f[] params0 = {
        params100, params125x, params133x,
        params150x, params166x, params200x,
        paramsHuge1, paramsHuge2
    };

    static V4f paramsDefault() {
      V4f params = new V4f();
      UnderlineConstants.sinParamsDefault(params);
      return params;
    }

    static void t() {
      double pow1x = .375;
      double lg375 = Numbers.log(0.5, pow1x);
      double check = Math.pow(pow1x, lg375);
      double lg3 = Numbers.log(lg375, 3);

      double check2 = Math.pow(pow1x, Math.pow(3, lg3));
      V4f check3 = scaleParams(paramsDefault(), 3);
    }
  }
}
