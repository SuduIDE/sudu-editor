package org.sudu.experiments.ui.window;

import org.sudu.experiments.Disposable;
import org.sudu.experiments.GL;
import org.sudu.experiments.editor.ui.colors.DialogItemColors;
import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.math.Rect;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.ui.UiContext;
import org.sudu.experiments.ui.UiFont;
import org.sudu.experiments.ui.WindowColors;
import org.sudu.experiments.ui.fonts.Codicons;

import java.util.Objects;

public class SystemMenu {
  final V2i pos = new V2i();
  final V2i size = new V2i();

  Runnable onClose;
  UiFont font;
  GL.Texture close;
  boolean hover;

  public void dispose() {
    disposeTextures();
    onClose = null;
  }

  public void onDprChanged() {
    disposeTextures();
  }

  public void setTheme(DialogItemColors theme) {
    if (!Objects.equals(theme.windowTitleIcons, font)) {
      font = theme.windowTitleIcons;
      disposeTextures();
    }
  }

  private void disposeTextures() {
    close = Disposable.assign(close, null);
  }

  private void allocTextures(UiContext c) {
    FontDesk iconsFont = c.fontDesk(font);
    int lineHeight = iconsFont.lineHeight();
    close = Disposable.assign(close,
        c.graphics.renderTexture(
            String.valueOf(Codicons.chrome_close),
            iconsFont, 0, lineHeight, 0, false));
  }

  public void draw(UiContext c, View title, WindowColors wc) {
    if (onClose != null) {
      if (close == null) {
        allocTextures(c);
      }
      int dp2 = c.toPx(2);
      int margin = Math.max(0, (title.size.y - close.height()) / 2 - dp2);
      pos.x = title.pos.x + title.size.x - margin - close.width() - dp2;
      pos.y = title.pos.y + margin - dp2 / 2;
      size.set(close.width() + dp2 * 2, close.height() + dp2 * 2);
      c.graphics.drawRect(pos.x, pos.y, size,
          hover ? wc.windowBorderColor : wc.windowTitleBgColor
      );
      c.v4f1.set(0,0, close.width(), close.height());
      c.graphics.drawText(pos.x + dp2, pos.y + dp2 / 2,
          close.size(), c.v4f1, close,
          wc.windowTitleTextColor,
          hover ? wc.windowBorderColor : wc.windowTitleBgColor,
          false);
    }
  }

  public void onMouseMove(V2i position) {
    hover = hitTest(position);
  }

  public boolean hitTest(V2i position) {
    return close != null && Rect.isInside(position, pos, size);
  }
}
