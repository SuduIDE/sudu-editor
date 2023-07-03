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

import java.util.List;
import java.util.Objects;

public class FindUsagesWindow {
  private final V2i windowSize = new V2i();
  private final FindUsagesDialog view = new FindUsagesDialog();
  private final WglGraphics graphics;
  private double dpr;
  private FontDesk font;
  private Runnable onClose = Const.emptyRunnable;
  private DialogItemColors theme;

  public FindUsagesWindow(WglGraphics graphics) {
    this.graphics = graphics;
    view.onClickOutside(this::hide);
  }

  // todo: change font and size if dpr changed on
  public void setFont(FontDesk f) {
    font = f;
    view.setFont(f);
  }

  public void setTheme(DialogItemColors dialogItemColors) {
    theme = dialogItemColors;
    view.setTheme(dialogItemColors);
  }

  public void onClose(Runnable onClose) {
    this.onClose = onClose;
  }

  public void display(V2i mousePos, FindUsagesItem[] actions) {
    if (font == null || isVisible()) {
      throw new IllegalArgumentException();
    }
    view.setItems(actions);
    view.measure(graphics.mCanvas, dpr);
    view.setScreenLimitedPosition(mousePos.x, mousePos.y, windowSize);
  }

  public boolean hide() {
    if (isVisible()) {
      onClose.run();
      dispose();
      return true;
    }
    return false;
  }

  public void onResize(V2i newSize, double newDpr) {
    windowSize.set(newSize);
    if (this.dpr != newDpr) {
      view.measure(graphics.mCanvas, newDpr);
      this.dpr = newDpr;
    }
  }

  public void center(V2i newSize) {
    V2i usageSize = view.size();
    view.setPos((newSize.x - usageSize.x) / 2, ((newSize.y - usageSize.y) / 2));
  }

  public void paint() {
    if (!view.isEmpty()) graphics.enableBlend(true);
    view.render(graphics, dpr);
  }

  public boolean onMouseMove(V2i mouse, SetCursor windowCursor) {
    return view.onMouseMove(mouse, windowCursor);
  }

  public boolean onMousePress(V2i position, int button, boolean press, int clickCount) {
    return view.onMousePress(position, button, press, clickCount);
  }

  public boolean isVisible() {
    return !view.isEmpty();
  }

  public void dispose() {
    onClose = Const.emptyRunnable;
    view.dispose();
  }

  public final FindUsagesItem[] buildUsagesItems(List<Pos> usages, EditorComponent editorComponent) {
    return buildItems(usages, null, editorComponent);
  }

  public final FindUsagesItem[] buildDefItems(Location[] defs, EditorComponent editorComponent) {
    return buildItems(null, defs, editorComponent);
  }

  private String fileName(Uri uri) {
    return uri != null ? uri.getFileName() : "";
  }

  private FindUsagesItem[] buildItems(List<Pos> usages, Location[] defs, EditorComponent edit) {
    Model model = edit.model();
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
        codeContent = Objects.equals(model.uri, defs[i].uri)
            ? model.document.line(intLineNumber).makeString().trim() : "";

        fileName = fileName(defs[i].uri);
      }
      String codeContentFormatted = codeContent.length() > 43
              ? codeContent.substring(0, 40) + "..." : codeContent;
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
      Runnable action = defs == null
              ? () -> edit.gotoUsageMenuElement(pos)
              : () -> edit.gotoDefinition(def);
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
          view.onKeyArrow(event.keyCode);
      case KeyCode.ENTER -> view.goToSelectedItem();
      default -> false;
    };
  }
}
