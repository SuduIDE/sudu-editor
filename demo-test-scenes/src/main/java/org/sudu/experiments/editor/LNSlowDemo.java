package org.sudu.experiments.editor;

import org.sudu.experiments.SceneApi;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.fonts.Fonts;
import org.sudu.experiments.input.InputListeners;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.math.Numbers;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.ui.UiFont;

import java.util.Arrays;

public class LNSlowDemo extends Scene1 implements InputListeners.ScrollHandler {
  LineNumbersComponent[] comps = new LineNumbersComponent[0];
  private static final int widthDp = 80;
  private int docLen = 200;
  private int width;
  private static final UiFont uiFont = new UiFont(Fonts.Consolas, 15);
  private FontDesk font;
  private int count;
  private int lineHeight;
  private int scrollPos;
  private final V2i size = new V2i();

  public LNSlowDemo(SceneApi api) {
    super(api);

    size.set(uiContext.windowSize);
    api.input.onScroll.add(this);
  }

  private void updatePos() {
    int newCount = count();
    for (int i = newCount; i < count; i++) {
      comps[i].dispose();
      comps[i] = null;
    }
    comps = Arrays.copyOf(comps, newCount);
    count = newCount;
    V2i pos = new V2i();
    for (int i = 0; i < count; i++) {
      var c = comps[i];
      if (c != null) c.setPos(pos, width, size.y, uiContext.dpr);
      if (c == null) {
        c = new LineNumbersComponent();
        c.setPos(pos, width, size.y, uiContext.dpr);
        c.setFont(font, lineHeight, uiContext.graphics);
        comps[i] = c;
      }
      pos.set(pos.x + width, pos.y);
    }
  }

  private int count() {
    return Numbers.iDivRoundUp(size.x, width);
  }

  @Override
  public void dispose() {
    for (int i = 0; i < count; i++) {
      comps[i].dispose();
      comps[i] = null;
    }
  }

  @Override
  public void paint() {
    int textHeight = Math.min(size.y, docLen * lineHeight - scrollPos);
    for (int i = 0; i < count; i++) {
      comps[i].draw(
          size.y,
          textHeight,
          scrollPos,
          getFirstLine(),
          getLastLine(),
          -1,
          uiContext.graphics,
          EditorColorScheme.darkIdeaColorScheme()
      );
    }
  }

  @Override
  public void onResize(V2i size, float dpr) {
    super.onResize(size, dpr);
    this.size.set(size);
    if (dpr > 0) {
      font = uiContext.fontDesk(uiFont);
      lineHeight = Numbers.iRnd(font.lineHeight() * EditorConst.LINE_HEIGHT);
      width = uiContext.toPx(widthDp);
      updatePos();
    }
  }



  @Override
  public boolean onScroll(MouseEvent event, float dX, float dY) {
    scrollPos = Math.max(0, scrollPos + Numbers.iRnd(lineHeight * 4 * dY / 150));
    docLen = getDocLen();
    return true;
  }

  private int getFirstLine() {
    return Math.min(scrollPos / lineHeight, docLen - 1);
  }

  private int getLastLine() {
    return Math.min((scrollPos + size.y - 1) / lineHeight, docLen - 1);
  }

  private int getDocLen() {
    return scrollPos / lineHeight + (size.y / lineHeight) * 3;
  }
}
