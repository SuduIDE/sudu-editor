package org.sudu.experiments.ui.window;

import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.math.V4f;

public class ViewFill extends View {

  public final V4f color = new V4f(Colors.viewColor);

  public void setColor(V4f v) {
    color.set(v);
  }

  public void draw(WglGraphics g) {
    g.drawRect(pos.x, pos.y, size, color);
  }
}
