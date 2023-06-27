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
import org.sudu.experiments.demo.Location;

import java.util.Collections;
import java.util.List;

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

  private FindUsagesDialog displayFindUsagesMenu(V2i pos, FindUsagesItem[] items) {
    FindUsagesDialog findUsages = new FindUsagesDialog();
    findUsages.setItems(items);
    setFindUsagesStyle(findUsages);
    findUsages.measure(graphics.mCanvas, dpr);
    setScreenLimitedPosition(findUsages, pos.x, pos.y, windowSize);

    usagesList = findUsages;
    return findUsages;
  }

  public void display(V2i mousePos, FindUsagesItem[] actions, Runnable onClose) {
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

  public void center(V2i newSize) {
    V2i usageSize = usagesList.size();
    usagesList.setPos((newSize.x - usageSize.x) / 2, ((newSize.y - usageSize.y) / 2));
  }

  public void paint() {
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

  public final FindUsagesItem[] buildUsagesItems(List<Pos> usages, EditorComponent editorComponent, Model model) {
    return buildItems(usages, null, editorComponent, model);
  }

  public final FindUsagesItem[] buildDefItems(Location[] defs, EditorComponent editorComponent, Model model) {
    return buildItems(Collections.emptyList(), defs, editorComponent, model);
  }

  private FindUsagesItem[] buildItems(List<Pos> usages, Location[] defs, EditorComponent editorComponent, Model model) {
    FindUsagesItemBuilder tbb = new FindUsagesItemBuilder();
    int cnt = 0;
    int itemsLength = defs == null ? usages.size() : defs.length;
    for (int i = 0; i < itemsLength; i++) {
      int intLineNumber = defs == null ? usages.get(i).line : defs[i].range.startLineNumber;

      // TODO(Get file names from server)
      String fileName = "Main.java";
      String codeContent = model.document.line(intLineNumber).makeString().trim();
      String codeContentFormatted = codeContent.length() > 43 ? codeContent.substring(0, 40) + "..." : codeContent;
      String lineNumber = String.valueOf(intLineNumber + 1);

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
      Location def = defs != null ? defs[i] : null;
      Pos pos = defs == null ? usages.get(i) : null;
      Runnable action = pos != null
          ? () -> editorComponent.gotoUsageMenuElement(pos)
          : () -> editorComponent.gotoDefinition(def);
      tbb.addItem(
          fileName,
          lineNumber,
          codeContentFormatted,
          Colors.findUsagesColors,
          action
      );
    }
    return tbb.items();
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
