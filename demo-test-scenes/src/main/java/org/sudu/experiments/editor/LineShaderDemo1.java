package org.sudu.experiments.editor;

import org.sudu.experiments.SceneApi;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.math.V2i;

public class LineShaderDemo1 extends LineShaderDemo0  {
  public LineShaderDemo1(SceneApi api) {
    super(api);

    rect.set(0, 0, 300, 300);
    p12.set(300, 300);
  }

  public void onResize(V2i size, float dpr) {
    rect.pos.set((size.x - rect.size.x) / 2, (size.y - rect.size.y) / 2);
    p11.set(0,0);
    p12.set(Math.min(size.x, size.y), Math.min(size.x, size.y));
    p21.set(p11.x, p11.y + 20);
    p22.set(p12.x, p12.y + 40);
  }

  public boolean onMouseMove(MouseEvent event) {
    p11.set(event.position);
    p12.set(rect.pos.x + rect.size.x / 2, rect.pos.y + rect.size.y / 2);
    p21.set(p11.x, p11.y + 20);
    p22.set(p12.x, p12.y + 40);

    return false;
  }
}
