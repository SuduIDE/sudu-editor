package org.sudu.experiments.demo.menu;

import org.sudu.experiments.Scene0;
import org.sudu.experiments.SceneApi;
import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.demo.Colors;
import org.sudu.experiments.demo.IdeaCodeColors;
import org.sudu.experiments.demo.SetCursor;
import org.sudu.experiments.demo.ui.FindUsagesItem;
import org.sudu.experiments.demo.ui.FindUsagesItemBuilder;
import org.sudu.experiments.demo.ui.FindUsagesItemColors;
import org.sudu.experiments.demo.ui.FindUsagesWindow;
import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.input.InputListener;
import org.sudu.experiments.input.KeyCode;
import org.sudu.experiments.input.KeyEvent;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.Numbers;
import org.sudu.experiments.math.V2i;

public class FindUsagesDemo extends Scene0 implements InputListener {

  private final SetCursor windowCursor;
  private final V2i windowSize = new V2i();

  private final V2i hLine = new V2i();
  private final V2i vLine = new V2i();
  private final FindUsagesWindow findUsagesWindow;

  public FindUsagesDemo(SceneApi api) {
    super(api);
    windowCursor = SetCursor.wrap(api.window);

    findUsagesWindow = new FindUsagesWindow(api.graphics);

    api.input.addListener(this);

    FontDesk font = api.graphics.fontDesk("Consolas", 25);

    findUsagesWindow.setTheme(font, Colors.findUsagesBg);
    clearColor.set(new Color(43));

  }

  private static FindUsagesItem[] createItems() {
    FindUsagesItemBuilder tbb = new FindUsagesItemBuilder();

    addAction(tbb, "main.java  ", "5    ", "private static void foo (...);           ");
    addAction(tbb, "main.java  ", "25   ", "String foo = \"boo\";                      ");
    addAction(tbb, "main.java  ", "131  ", "int a = 5;                               ");
    addAction(tbb, "class.java ", "176  ", "public class FindTest extend Test {...}; ");
    addAction(tbb, "main.java  ", "1234 ", "private static void foo (...);           ");
    addAction(tbb, "sub.java   ", "4321 ", "private static void foo (...);           ");
    addAction(tbb, "demo.java  ", "23872", "private static void foo (...);           ");

    return tbb.items();
  }

  private static void addAction(FindUsagesItemBuilder fu, String fileName, String lineNumber, String codeContent) {
    FindUsagesItemColors colors = new FindUsagesItemColors(IdeaCodeColors.Colors.defaultText, IdeaCodeColors.Colors.editNumbersVLine, IdeaCodeColors.Colors.defaultText, Colors.findUsagesBg, Colors.findUsagesSelectedBg);
    fu.addItem(fileName, lineNumber, codeContent, colors, () -> System.out.println(fileName + "\t" + lineNumber + "\t" + codeContent));
  }

  @Override
  public void dispose() {
    super.dispose();
    findUsagesWindow.dispose();
  }

  @Override
  public void onResize(V2i newSize, double dpr) {
    super.onResize(newSize, dpr);
    windowSize.set(newSize);

    hLine.set(newSize.x, Numbers.iRnd(dpr) * 2);
    vLine.set(Numbers.iRnd(dpr) * 2, newSize.y);

    if (!findUsagesWindow.isVisible()) {
      openWindow(new V2i());
    }

    findUsagesWindow.onResize(newSize, dpr);
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
    return findUsagesWindow.onMouseMove(event.position, windowCursor);
  }

  @Override
  public boolean onMousePress(MouseEvent event, int button, boolean press, int clickCount) {
    return findUsagesWindow.onMousePress(event.position, button, press, clickCount);
  }

  @Override
  public boolean onContextMenu(MouseEvent event) {
    System.out.println("onContextMenu");
    if (!findUsagesWindow.isVisible()) {
      openWindow(event.position);
    }
    return true;
  }

  private void openWindow(V2i position) {
    findUsagesWindow.display(position, createItems(), this::onPopupClosed);
  }

  private void onPopupClosed() {
    System.out.println("onPopupClosed");
  }

  @Override
  public boolean onKey(KeyEvent event) {
    if (event.isPressed && event.keyCode == KeyCode.SPACE) {
      findUsagesWindow.hide();
      return true;
    }
    if (event.isPressed) {
      findUsagesWindow.onKey(event);
      return true;
    }

    return false;
  }

}
