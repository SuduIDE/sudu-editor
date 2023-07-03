package org.sudu.experiments.demo.ui;

import org.sudu.experiments.Const;
import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.demo.EditorComponent;
import org.sudu.experiments.demo.EditorConst;
import org.sudu.experiments.demo.Location;
import org.sudu.experiments.demo.Model;
import org.sudu.experiments.demo.SetCursor;
import org.sudu.experiments.demo.Uri;
import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.input.KeyCode;
import org.sudu.experiments.input.KeyEvent;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.parser.common.Pos;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class FindUsagesWindow {
  private final V2i windowSize = new V2i();
  public FindUsagesDialog findUsagesDialog = new FindUsagesDialog();
  private final WglGraphics graphics;
  private double dpr;
  private FontDesk font;
  private Runnable onClose = Const.emptyRunnable;
  private DialogItemColors theme;

  public FindUsagesWindow(WglGraphics graphics) {
    this.graphics = graphics;
  }

  static void setScreenLimitedPosition(FindUsagesDialog findUsages, int x, int y, V2i screen) {
    findUsages.setPos(
        Math.max(0, Math.min(x, screen.x - findUsages.size().x)),
        Math.max(0, Math.min(y, screen.y - findUsages.size().y)));
  }

  // todo: change font and size if dps changed on
  public void setFont(FontDesk f) {
    font = f;
  }

  public void setTheme(DialogItemColors dialogItemColors) {
    this.theme = dialogItemColors;
    findUsagesDialog.setTheme(dialogItemColors);
  }

  private FindUsagesDialog displayFindUsagesMenu(V2i pos, FindUsagesItem[] items) {
    FindUsagesDialog findUsages = new FindUsagesDialog();
    findUsages.setItems(items);
    setFindUsagesStyle(findUsages);
    findUsages.measure(graphics.mCanvas, dpr);
    setScreenLimitedPosition(findUsages, pos.x, pos.y, windowSize);

    findUsagesDialog = findUsages;
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
    fu.setTheme(theme);
  }

  public void onResize(V2i newSize, double newDpr) {
    windowSize.set(newSize);
    if (this.dpr != newDpr) {
      if (!findUsagesDialog.isEmpty())
        findUsagesDialog.measure(graphics.mCanvas, newDpr);
      this.dpr = newDpr;
    }
  }

  public void center(V2i newSize) {
    V2i usageSize = findUsagesDialog.size();
    findUsagesDialog.setPos((newSize.x - usageSize.x) / 2, ((newSize.y - usageSize.y) / 2));
  }

  public void paint() {
    if (!findUsagesDialog.isEmpty()) graphics.enableBlend(true);
    findUsagesDialog.render(graphics, dpr);
  }

  public boolean onMouseMove(V2i mouse, SetCursor windowCursor) {
    return findUsagesDialog.onMouseMove(mouse, windowCursor);
  }

  public boolean onMousePress(V2i position, int button, boolean press, int clickCount) {
    return findUsagesDialog.onMousePress(position, button, press, clickCount);
  }

  public boolean isVisible() {
    return !findUsagesDialog.isEmpty();
  }

  public void dispose() {
    findUsagesDialog.dispose();
  }

  public final FindUsagesItem[] buildUsagesItems(List<Pos> usages, EditorComponent editorComponent, Model model) {
    return buildItems(usages, null, editorComponent, model);
  }

  public final FindUsagesItem[] buildDefItems(Location[] defs, EditorComponent editorComponent, Model model) {
    return buildItems(Collections.emptyList(), defs, editorComponent, model);
  }

  private String fileName(Uri uri) {
    return uri != null ? uri.getFileName() : "";
  }

  private FindUsagesItem[] buildItems(List<Pos> usages, Location[] defs, EditorComponent editorComponent, Model model) {
    if (theme == null) throw new RuntimeException("Dialog item color theme has not been set");

    FindUsagesItemBuilder tbb = new FindUsagesItemBuilder();
    int cnt = 0;
    int itemsLength = defs == null ? usages.size() : defs.length;
    for (int i = 0; i < itemsLength; i++) {
      int intLineNumber;
      String codeContent;
      String fileName;
      if (defs == null) {
        intLineNumber = usages.get(i).line;
        codeContent = model.document.line(intLineNumber).makeString().trim();
        fileName = fileName(model.uri);
      } else {
        intLineNumber = defs[i].range.startLineNumber;
        codeContent = Objects.equals(editorComponent.model().uri, defs[i].uri)
            ? model.document.line(intLineNumber).makeString().trim() : "";

        fileName = fileName(defs[i].uri);
      }
      String codeContentFormatted = codeContent.length() > 43 ? codeContent.substring(0, 40) + "..." : codeContent;
      String lineNumber = String.valueOf(intLineNumber + 1);

      if (++cnt > EditorConst.MAX_SHOW_USAGES_NUMBER) {
        tbb.addItem(
            "... and " + (usages.size() - (cnt - 1)) + " more usages",
            "",
            "",
            theme.findUsagesColorsContinued,
            () -> {
            }
        );
        break;
      }
      Location def;
      Pos pos;
      if (defs == null) {
        def = null;
        pos = usages.get(i);
      } else {
        pos = null;
        def = defs[i];
      }
      Runnable action = defs == null ? () -> editorComponent.gotoUsageMenuElement(pos) :
          () -> editorComponent.gotoDefinition(def);
      tbb.addItem(
          fileName,
          lineNumber,
          codeContentFormatted,
          theme.findUsagesColors,
          action
      );
    }
    return tbb.items();
  }

  public boolean onKey(KeyEvent event) {
    if (!isVisible()) return false;
    return switch (event.keyCode) {
      case KeyCode.ESC -> hide();
      case KeyCode.ARROW_DOWN, KeyCode.ARROW_UP, KeyCode.ARROW_LEFT, KeyCode.ARROW_RIGHT ->
          findUsagesDialog.onKeyArrow(event.keyCode);
      case KeyCode.ENTER -> findUsagesDialog.goToSelectedItem();
      default -> false;
    };
  }
}
