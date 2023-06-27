package org.sudu.experiments.demo.ui;

import org.sudu.experiments.Const;
import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.demo.Colors;
import org.sudu.experiments.demo.EditorComponent;
import org.sudu.experiments.demo.EditorConst;
import org.sudu.experiments.demo.Model;
import org.sudu.experiments.demo.SetCursor;
import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.input.KeyCode;
import org.sudu.experiments.input.KeyEvent;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;
import org.sudu.experiments.parser.common.Pos;

import java.util.List;
import java.util.function.Supplier;

public class FindUsagesWindow {
  private final V2i windowSize = new V2i();
  public FindUsagesDialog usagesList = new FindUsagesDialog();
  private final WglGraphics graphics;
  private final V4f frameColor = Colors.findUsagesBorder;
  private double dpr;
  private FontDesk font;
  private V4f bgColor = Colors.findUsagesBg;
  private Runnable onClose = Const.emptyRunnable;

  public FindUsagesWindow(WglGraphics graphics) {
    this.graphics = graphics;
  }

  static void setScreenLimitedPosition(FindUsagesDialog findUsages, int x, int y, V2i screen) {
    findUsages.setPos(
        Math.max(0, Math.min(x, screen.x - findUsages.size().x)),
        Math.max(0, Math.min(y, screen.y - findUsages.size().y)));
  }

  // todo: change font and size if dps changed on
  public void setTheme(FontDesk f, V4f bg) {
    font = f;
    bgColor = bg;
  }

  private FindUsagesDialog displayFindUsagesMenu(V2i pos, Supplier<FindUsagesItem[]> items) {
    FindUsagesDialog findUsages = new FindUsagesDialog();
    findUsages.setItems(items.get());
    setFindUsagesStyle(findUsages);
    findUsages.measure(graphics.mCanvas, dpr);
    setScreenLimitedPosition(findUsages, pos.x, pos.y, windowSize);

    findUsages.onEnter((mouse, index, item) -> dispose());

    usagesList = findUsages;
    return findUsages;
  }

  public void display(V2i mousePos, Supplier<FindUsagesItem[]> actions, Runnable onClose) {
    if (font == null || isVisible()) {
      throw new IllegalArgumentException();
    }
    this.onClose = onClose;
    FindUsagesDialog usagesMenu = displayFindUsagesMenu(mousePos, actions);
    usagesMenu.onClickOutside(this::hide);
  }

  public boolean hide() {
    if (isVisible()) {
      dispose();
      onClose.run();
      onClose = Const.emptyRunnable;
      return true;
    }
    return false;
  }

  private void setFindUsagesStyle(FindUsagesDialog fu) {
    fu.setFont(font);
    fu.setBgColor(bgColor);
    fu.setFrameColor(frameColor);
  }

  public void onResize(V2i newSize, double newDpr) {
    windowSize.set(newSize);
    if (this.dpr != newDpr) {
      if (!usagesList.isEmpty())
        usagesList.measure(graphics.mCanvas, newDpr);
      this.dpr = newDpr;
    }
  }

  public void paint() {
    // let's do 0-garbage rendering
    if (!usagesList.isEmpty()) graphics.enableBlend(true);
    usagesList.render(graphics, dpr);
  }

  public boolean onMouseMove(V2i mouse, SetCursor windowCursor) {
    return usagesList.onMouseMove(mouse, windowCursor);
  }

  public boolean onMousePress(V2i position, int button, boolean press, int clickCount) {
    return usagesList.onMousePress(position, button, press, clickCount);
  }

  public boolean isVisible() {
    return !usagesList.isEmpty();
  }

  public void dispose() {
    usagesList.dispose();
  }

  public final Supplier<FindUsagesItem[]> buildUsagesItems(List<Pos> usages, EditorComponent editorComponent, Model model) {
    FindUsagesItemBuilder tbb = new FindUsagesItemBuilder();
    int cnt = 0;
    for (var pos : usages) {
      // TODO(Get file names from server)
      String fileName = "Main.java";
      String codeContent = model.document.line(pos.line).makeString().trim();
      String codeContentFormatted = codeContent.length() > 43 ? codeContent.substring(0, 40) + "..." : codeContent;
      String lineNumber = String.valueOf(pos.line + 1);

      if (++cnt > EditorConst.MAX_SHOW_USAGES_NUMBER) {
        tbb.addItem(
            "... and " + (usages.size() - (cnt - 1)) + " more usages",
            "",
            "",
            Colors.findUsagesColorsContinued,
            () -> {
            }
        );
        break;
      }
      tbb.addItem(
          fileName,
          lineNumber,
          codeContentFormatted,
          Colors.findUsagesColors,
          () -> editorComponent.gotoUsageMenuElement(pos)
      );
    }
    return tbb.supplier();
  }

  public boolean handleUsagesMenuKey(KeyEvent event) {
    return switch (event.keyCode) {
      case KeyCode.ESC -> hide();
      case KeyCode.ARROW_DOWN, KeyCode.ARROW_UP, KeyCode.ARROW_LEFT, KeyCode.ARROW_RIGHT ->
          usagesList.onKeyArrow(event.keyCode);
      case KeyCode.ENTER -> usagesList.goToSelectedItem();
      default -> false;
    };
  }
}
