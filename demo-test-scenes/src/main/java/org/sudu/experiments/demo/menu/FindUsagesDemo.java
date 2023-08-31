package org.sudu.experiments.demo.menu;

import org.sudu.experiments.SceneApi;
import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.demo.Colors;
import org.sudu.experiments.demo.IdeaCodeColors;
import org.sudu.experiments.demo.Scene1;
import org.sudu.experiments.demo.TestHelper;
import org.sudu.experiments.demo.ui.*;
import org.sudu.experiments.demo.ui.window.ScrollView;
import org.sudu.experiments.demo.ui.window.Window;
import org.sudu.experiments.demo.ui.window.WindowManager;
import org.sudu.experiments.fonts.Fonts;
import org.sudu.experiments.input.KeyCode;
import org.sudu.experiments.input.KeyEvent;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.V2i;

import java.util.function.Supplier;

import static org.sudu.experiments.Const.emptyRunnable;

public class FindUsagesDemo extends Scene1 implements DprChangeListener {

  private final PopupMenu popupMenu;

  private final WindowManager windowManager;
  private Window window1;

  public FindUsagesDemo(SceneApi api) {
    super(api);
    windowManager = new WindowManager();
    uiContext.dprListeners.add(windowManager);
    uiContext.dprListeners.add(this);
    clearColor.set(new Color(43));
    popupMenu = new PopupMenu(uiContext);
    popupMenu.setTheme(DialogItemColors.darkColorScheme(),
        new UiFont("Consolas", 25));

    api.input.onKeyPress.add(this::onKey);
    api.input.onContextMenu.add(this::onContextMenu);
    api.input.onMouse.add(TestHelper.popupMouseListener(popupMenu));
    api.input.onMouse.add(windowManager);
    api.input.onScroll.add(windowManager::onScroll);
  }

  @Override
  public void onDprChanged(float oldDpr, float newDpr) {
    if (oldDpr == 0) openWindows();
  }

  @Override
  public void dispose() {
    popupMenu.dispose();
    windowManager.dispose();
  }

  @Override
  public void paint() {
    super.paint();
    WglGraphics graphics = api.graphics;
    windowManager.draw(graphics);
    popupMenu.paint();
  }

  private boolean onContextMenu(MouseEvent event) {
    if (!popupMenu.isVisible()) {
      popupMenu.display(event.position, items(), emptyRunnable);
    }
    return true;
  }

  private Supplier<ToolbarItem[]> items() {
    return ArrayOp.supplier(
        new ToolbarItem(this::openWindows, "newWindow",
            popupMenu.theme().toolbarItemColors)
    );
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
  UiFont titleFont = new UiFont(Fonts.SegoeUI, 15);

  private Window newWindow() {
    DialogItemColors theme = DialogItemColors.darkColorScheme();

    FindUsagesView view = new FindUsagesView(uiContext, () -> {});
    UiFont uiFont = new UiFont(Fonts.Consolas, 14);
    view.setItems(createItems());
    view.setTheme(theme, uiFont);

    Window window = new Window(uiContext);
    window.setContent(new ScrollView(view, uiContext));
    window.setTheme(theme);
    window.setTitle("FindUsagesView1", titleFont, 2);
    return window;
  }

  @Override
  public void onResize(V2i newSize, float newDpr) {
    super.onResize(newSize, newDpr);
    windowManager.onResize(newSize, newDpr);
//    findUsagesWindow.center(newSize);
    layoutWindows();
  }

  private FindUsagesItemData[] createItems() {
    FindUsagesItemBuilder tbb = new FindUsagesItemBuilder();

    for (int i = 0; i < 300; i++) {
      addAction(tbb, "main.java", String.valueOf(i), "private static void foo (...);");
    }

//    addAction(tbb, "main.java", "5", "private static void foo (...);");
//    addAction(tbb, "main.java", "25", "String foo = \"boo\";");
//    addAction(tbb, "main.java", "131", "int a = 5;");
//    addAction(tbb, "class.java", "176", "public class FindTest extend Test {...};");
//    addAction(tbb, "main.java", "1234", "private static void foo (...);");
//    addAction(tbb, "sub.java", "4321", "private static void foo (...);");
//    addAction(tbb, "demo.java", "23872", "private static void foo (...);");
//    addAction(tbb, "demoWW.java", "23872", "private static void foo (...);");
//    addAction(tbb, "demoW.java", "23872", "private static void foo (...);");
//    addAction(tbb, "demok.java", "23872", "private static void foo (...);");
//    addAction(tbb, "demok.java", "23872", "private static void foo (...);");
    return tbb.items();
  }

  private static void addAction(FindUsagesItemBuilder fu, String fileName, String lineNumber, String codeContent) {
    FindUsagesItemColors colors = new FindUsagesItemColors(IdeaCodeColors.Colors.defaultText, IdeaCodeColors.Colors.editNumbersVLine, IdeaCodeColors.Colors.defaultText, Colors.findUsagesBg, Colors.findUsagesSelectedBg);
    fu.addItem(fileName, lineNumber, codeContent, () -> System.out.println(fileName +"\t" + lineNumber +"\t" + codeContent));
  }

  private void layoutWindows() {
    V2i newSize = uiContext.windowSize;
    window1.setPosition(
        new V2i(newSize.x * 2 / 10, newSize.y * 4 / 10),
        new V2i(newSize.x * 7 / 10, newSize.y * 3 / 10)
    );
    window1.setTitle("Window 1: " + window1.size().toString(), titleFont, 2);
  }


  private boolean onKey(KeyEvent event) {
    if (event.isPressed && event.keyCode == KeyCode.SPACE) {
      return true;
    }
    return false;
  }

  @Override
  public boolean update(double timestamp) {
    return windowManager.update(timestamp);
  }
}
