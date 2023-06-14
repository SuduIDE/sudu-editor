package org.sudu.experiments.demo.menu;

import org.sudu.experiments.Scene0;
import org.sudu.experiments.SceneApi;
import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.demo.Colors;
import org.sudu.experiments.demo.IdeaCodeColors;
import org.sudu.experiments.demo.SetCursor;
import org.sudu.experiments.demo.ui.*;
import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.input.InputListener;
import org.sudu.experiments.input.KeyCode;
import org.sudu.experiments.input.KeyEvent;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.math.*;

import java.util.function.Supplier;

public class FindUsagesDemo extends Scene0 implements InputListener {

    private final SetCursor windowCursor;
    private final V2i windowSize = new V2i();
    private final FontDesk font;
    private double dpr;
    private final FindUsages tbV = new FindUsages();
    private final V2i hLine = new V2i();
    private final V2i vLine = new V2i();

    private final FindUsagesWindow findUsagesWindow;

    public FindUsagesDemo(SceneApi api) {
        super(api);
        windowCursor = SetCursor.wrap(api.window);

        findUsagesWindow = new FindUsagesWindow(api.graphics);

        api.input.addListener(this);

        tbV.setLayoutVertical();
        font = api.graphics.fontDesk("Consolas", 25);

        findUsagesWindow.setTheme(font, Colors.findUsagesBg);
        clearColor.set(new Color(43));

        setFindUsagesStyle(tbV);

        tbV.onClickOutside(() -> System.out.println("tbV onClickOutside"));
        onEnterLeave(tbV);

        tbV.setItems(createItems().get());

    }

    private void setFindUsagesStyle(FindUsages fu) {
        fu.setFont(font);
        fu.setBgColor(Colors.findUsagesBg);
        fu.setFrameColor(Colors.findUsagesBorder);
    }

    private void onEnterLeave(FindUsages fu) {
        fu.onEnter((mouse, index, item) ->
                System.out.println(
                        "onEnter item " + index + ", item " + item));
        fu.onLeave((mouse, index, item) ->
                System.out.println(
                        "onLeave item " + index + ", item " + item));
    }

    private static Supplier<FindUsagesItem[]> createItems() {
        FindUsagesItemBuilder tbb = new FindUsagesItemBuilder();

        addAction(tbb, "main.java  | 5     | private static void foo (...);", null);
        addAction(tbb, "main.java  | 25    | String foo = \"boo\";", null);
        addAction(tbb, "main.java  | 131   | int a = 5;", null);
        addAction(tbb, "class.java | 176   | public class FindTest extend Test {...};", null);
        addAction(tbb, "main.java  | 1234  | private static void foo (...);", null);
        addAction(tbb, "sub.java   | 4321  | private static void foo (...);", null);
        addAction(tbb, "demo.java  | 23872 | private static void foo (...);", null);

        return tbb.supplier();
    }

    private static void addAction(FindUsagesItemBuilder fu, String action, Supplier<FindUsagesItem[]> sub) {
        FindUsagesItemColors colors = new FindUsagesItemColors(IdeaCodeColors.Colors.defaultText, Colors.toolbarBg, Colors.toolbarSelectedBg);
        if (sub != null) {
            fu.addItem(action, colors, sub);
        } else {
            fu.addItem(action, colors, () -> System.out.println(action));
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        tbV.dispose();
        findUsagesWindow.dispose();
    }

    @Override
    public void onResize(V2i newSize, double dpr) {
        super.onResize(newSize, dpr);
        windowSize.set(newSize);

        hLine.set(newSize.x, Numbers.iRnd(dpr) * 2);
        vLine.set(Numbers.iRnd(dpr) * 2, newSize.y);

        findUsagesWindow.onResize(newSize, dpr);

        if (this.dpr != dpr) {
            tbV.measure(api.graphics.mCanvas, dpr);
        }
        this.dpr = dpr;
        V2i tbvSize = tbV.size();
        tbV.setPos((newSize.x - tbvSize.x) / 2, (newSize.y - tbvSize.y) / 2);
    }

    @Override
    public void paint() {
        super.paint();
        WglGraphics graphics = api.graphics;
        graphics.enableBlend(true);
        tbV.render(graphics, dpr);
        findUsagesWindow.paint();
        graphics.enableBlend(false);
    }

    @Override
    public boolean onMouseMove(MouseEvent event) {
        boolean r = findUsagesWindow.onMouseMove(event.position, windowCursor);
        boolean tbVResult = tbV.onMouseMove(event.position, windowCursor);
        return r || tbVResult;
    }

    @Override
    public boolean onMousePress(MouseEvent event, int button, boolean press, int clickCount) {
        boolean r = findUsagesWindow.onMousePress(event.position, button, press, clickCount);
        boolean tbVResult = tbV.onMousePress(event.position, button, press, clickCount);
        return r || tbVResult;
    }

    @Override
    public boolean onContextMenu(MouseEvent event) {
        System.out.println("onContextMenu");
        if (!findUsagesWindow.isVisible()) {
            findUsagesWindow.display(event.position, createItems(),
                    this::onPopupClosed);
        }
        return true;
    }

    private void onPopupClosed() {
        System.out.println("onPopupClosed");
    }

    @Override
    public boolean onKey(KeyEvent event) {
        if (event.isPressed && event.keyCode == KeyCode.SPACE) {
            tbV.dispose();
            return true;
        }
        if (event.isPressed && (event.keyCode == KeyCode.ARROW_DOWN || event.keyCode == KeyCode.ARROW_UP)) {
            tbV.onKeyArrow(event.keyCode);
            return true;
        }

        return false;
    }

    @Override
    public boolean update(double timestamp) {
        return false;
    }
}
