package org.sudu.experiments.demo.menu;

import org.sudu.experiments.Scene0;
import org.sudu.experiments.SceneApi;
import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.demo.Colors;
import org.sudu.experiments.demo.IdeaCodeColors;
import org.sudu.experiments.demo.ui.*;
import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.input.*;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.V2i;

public class FindUsagesDemo extends Scene0 implements MouseListener {

  private final UiContext uiContext;
  private final FindUsagesWindow findUsagesWindow;

  public FindUsagesDemo(SceneApi api) {
    super(api);

    uiContext = new UiContext(api);
    uiContext.dprListeners.add(this::open);
    findUsagesWindow = new FindUsagesWindow(uiContext);
    findUsagesWindow.setTheme(DialogItemColors.darkColorScheme());

    api.input.onMouse.add(this);
    api.input.onKeyPress.add(this::onKeyPress);
    api.input.onContextMenu.add(this::onContextMenu);

    FontDesk font = api.graphics.fontDesk("Consolas", 25);

    findUsagesWindow.setFont(font);
    clearColor.set(new Color(43));

  }

  private void open(float oldDpr, float newDpr) {
    if (oldDpr == 0) {
      openWindow(new V2i());
    }
  }

  private static FindUsagesItem[] createItems() {
    FindUsagesItemBuilder tbb = new FindUsagesItemBuilder();

    addAction(tbb,"main.java","5","private static void foo (...);          ");
    addAction(tbb,"main.java","25","String foo = \"boo\";                     ");
    addAction(tbb,"main.java","131","int a = 5;                              ");
    addAction(tbb,"class.java","176","public class FindTest extend Test {...};");
    addAction(tbb,"main.java","1234","private static void foo (...);          ");
    addAction(tbb,"sub.java","4321","private static void foo (...);          ");
    addAction(tbb,"demo.java","23872","private static void foo (...);          ");

    return tbb.items();
  }

  private static void addAction(FindUsagesItemBuilder fu, String fileName, String lineNumber, String codeContent) {
    FindUsagesItemColors colors = new FindUsagesItemColors(IdeaCodeColors.Colors.defaultText, IdeaCodeColors.Colors.editNumbersVLine, IdeaCodeColors.Colors.defaultText, Colors.findUsagesBg, Colors.findUsagesSelectedBg);
    fu.addItem(fileName, lineNumber, codeContent, colors, () -> System.out.println(fileName +"\t" + lineNumber +"\t" + codeContent));
  }

  @Override
  public void dispose() {
    super.dispose();
    findUsagesWindow.dispose();
  }

  @Override
  public void onResize(V2i newSize, float dpr) {
    super.onResize(newSize, dpr);
    uiContext.onResize(newSize, dpr);
    findUsagesWindow.center(newSize);
  }

  @Override
  public void paint() {
    super.paint();
    WglGraphics graphics = api.graphics;
    graphics.enableBlend(true);
    findUsagesWindow.paint();
    graphics.enableBlend(false);
  }

  @Override
  public boolean onMouseMove(MouseEvent event) {
    return findUsagesWindow.onMouseMove(event.position);
  }

  @Override
  public boolean onMousePress(MouseEvent event, int button, boolean press, int clickCount) {
    return findUsagesWindow.onMousePress(event.position, button, press, clickCount);
  }

  boolean onContextMenu(MouseEvent event) {
    if (!findUsagesWindow.isVisible()) {
      openWindow(event.position);
    }
    return true;
  }

  private void openWindow(V2i position) {
    findUsagesWindow.onClose(this::onPopupClosed);
    findUsagesWindow.display(position, createItems());
  }

  private void onPopupClosed() {
    System.out.println("closed");
  }

  boolean onKeyPress(KeyEvent event) {
    if (event.keyCode == KeyCode.SPACE) {
      findUsagesWindow.hide();
      return true;
    }
    return findUsagesWindow.onKey(event);
  }
}
