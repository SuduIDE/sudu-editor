package org.sudu.experiments.editor.menu;

import org.sudu.experiments.SceneApi;
import org.sudu.experiments.editor.WindowScene;
import org.sudu.experiments.editor.ui.FindUsagesItemBuilder;
import org.sudu.experiments.editor.ui.FindUsagesItemData;
import org.sudu.experiments.editor.ui.FindUsagesView;
import org.sudu.experiments.editor.ui.colors.DialogItemColors;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.editor.ui.colors.Themes;
import org.sudu.experiments.fonts.Fonts;
import org.sudu.experiments.input.KeyCode;
import org.sudu.experiments.input.KeyEvent;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.ui.DprChangeListener;
import org.sudu.experiments.ui.PopupMenu;
import org.sudu.experiments.ui.ToolbarItem;
import org.sudu.experiments.ui.UiFont;
import org.sudu.experiments.ui.window.ScrollView;
import org.sudu.experiments.ui.window.Window;

import java.util.function.Supplier;

import static org.sudu.experiments.Const.emptyRunnable;

public class FindUsagesDemo extends WindowScene implements DprChangeListener {

  private Window window1;

  public FindUsagesDemo(SceneApi api) {
    super(api);
    uiContext.dprListeners.add(this);
    clearColor.set(new Color(43));
    api.input.onKeyPress.add(this::onKey);
    api.input.onContextMenu.add(this::onContextMenu);
  }

  @Override
  public void onDprChanged(float oldDpr, float newDpr) {
    if (oldDpr == 0) openWindows();
  }

  private boolean onContextMenu(MouseEvent event) {
    var popupMenu = new PopupMenu(uiContext);
    popupMenu.setTheme(Themes.darculaColorScheme(),
        new UiFont("Consolas", 25));
    popupMenu.setItems(event.position, items(), emptyRunnable);
    windowManager.setPopupMenu(popupMenu);
    return true;
  }

  private Supplier<ToolbarItem[]> items() {
    return ArrayOp.supplier(new ToolbarItem(this::openWindows, "newWindow"));
  }

  private void openWindows() {
    disposeWindow(window1);

    window1 = newWindow();
    windowManager.addWindow(window1);
//    findUsagesWindow.display(new V2i(), createItems());
    layoutWindows();
  }

  private void disposeWindow(Window w) {
    if (w != null) {
      windowManager.removeWindow(w);
      w.dispose();
    }
  }

  private Window newWindow() {
    EditorColorScheme editorColorScheme = EditorColorScheme.darculaIdeaColorScheme();
    DialogItemColors theme = editorColorScheme.dialogItem;

    FindUsagesView view = new FindUsagesView(uiContext, () -> {});
    UiFont uiFont = new UiFont(Fonts.Consolas, 14);
    view.setItems(createItems());
    view.setTheme(theme, uiFont);

    Window window = new Window(uiContext);
    ScrollView scrollView = new ScrollView(view, uiContext);
    scrollView.setScrollColor(theme.dialogScrollLine, theme.dialogScrollBg);
    window.setContent(scrollView);
    window.setTheme(theme);
    window.setTitle("FindUsagesDemo");
    return window;
  }

  @Override
  public void onResize(V2i newSize, float newDpr) {
    super.onResize(newSize, newDpr);
//    findUsagesWindow.center(newSize);
    layoutWindows();
  }

  private FindUsagesItemData[] createItems() {
    FindUsagesItemBuilder tbb = new FindUsagesItemBuilder();

    for (int i = 0; i < 300; i++) {
      addAction(tbb, "main.java", String.valueOf(i), "private static void foo (...);");
    }
    return tbb.items();
  }

  private static void addAction(FindUsagesItemBuilder fu, String fileName, String lineNumber, String codeContent) {
    fu.addItem(fileName, lineNumber, codeContent, () -> System.out.println(fileName +"\t" + lineNumber +"\t" + codeContent));
  }

  private void layoutWindows() {
    V2i newSize = uiContext.windowSize;
    window1.setPosition(
        new V2i(newSize.x * 2 / 10, newSize.y * 4 / 10),
        new V2i(newSize.x * 7 / 10, newSize.y * 3 / 10)
    );
    window1.setTitle("Window 1: " + window1.size().toString());
  }


  private boolean onKey(KeyEvent event) {
    return event.isPressed && event.keyCode == KeyCode.SPACE;
  }

}
