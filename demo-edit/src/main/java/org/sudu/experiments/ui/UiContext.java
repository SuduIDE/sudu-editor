package org.sudu.experiments.ui;

import org.sudu.experiments.*;
import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.input.InputListeners;
import org.sudu.experiments.input.KeyEvent;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.input.MouseListener;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;

import java.util.function.Consumer;

public class UiContext implements
    InputListeners.CopyHandler,
    InputListeners.PasteHandler
{

  public final WglGraphics graphics;
  public final Window window;
  public final SetCursor windowCursor;
  public final V2i windowSize = new V2i();
  public float dpr;
  public boolean cleartype;

  private Focusable focused;

  public final Subscribers<DprChangeListener> dprListeners
      = new Subscribers<>(new DprChangeListener[0]);

  public final V2i v2i1 = new V2i(), v2i2 = new V2i();
  public final V4f v4f1 = new V4f(), v4f2 = new V4f();

  public UiContext(SceneApi api) {
    this.graphics = api.graphics;
    this.window = api.window;
    this.cleartype = graphics.cleartypeSupported;
    windowCursor = SetCursor.wrap(api.window);
    api.input.onFocus.add(this::sendFocusGain);
    api.input.onBlur.add(this::sendFocusLost);
    api.input.onCopy.add(this);
    api.input.onPaste.add(this);
  }

  public void onResize(V2i newSize, float newDpr) {
    windowSize.set(newSize);
    if (dpr != newDpr) {
      float oldDpr = dpr;
      dpr = newDpr;
      for (DprChangeListener listener : dprListeners.array()) {
        listener.onDprChanged(oldDpr, newDpr);
      }
    }
  }

  public boolean onKeyPress(KeyEvent event) {
    return focused != null && focused.onKeyPress(event);
  }

  public void sendFocusGain() {
    if (focused != null) {
      focused.onFocusGain();
    }
  }

  public void sendFocusLost() {
    if (focused != null) {
      focused.onFocusLost();
    }
  }

  public void initFocus(Focusable f) {
    boolean hasFocus = window.hasFocus();
    if (hasFocus) sendFocusLost();
    focused = f;
    if (hasFocus) sendFocusGain();
  }

  public void setFocus(Focusable f) {
    if (focused != f) {
      sendFocusLost();
      focused = f;
      sendFocusGain();
    }
  }

  public void removeFocus(Focusable f) {
    if (focused == f) {
      focused = null;
    }
  }

  public boolean isFocused(Focusable f) {
    return f == focused;
  }

  public Focusable focused() {
    return focused;
  }

  public Runnable captureAndRestoreFocus() {
    Focusable focusSave = focused;
    return () -> setFocus(focusSave);
  }

  public Canvas mCanvas() { return graphics.mCanvas; }

  public void requireWindowVisible() {
    if (windowSize.x * windowSize.y == 0 || dpr == 0) {
      throw new IllegalStateException(
          "trying to display with unknown screen size and dpr");
    }
  }

  public FontDesk fontDesk(UiFont font) {
    return graphics.fontDesk(
        font.familyName, font.size * dpr,
        font.weightRegular, FontDesk.STYLE_NORMAL);
  }

  public int toPx(float value) {
    return DprUtil.toPx(value, dpr);
  }

  // returns true if changed
  public boolean enableCleartype(boolean en) {
    if (cleartype != en) {
      cleartype = en;
      window.repaint();
      return true;
    }
    return false;
  }

  public void setFontPow(float pow) {
    graphics.setTextPow(pow, cleartype);
    if (1<0) Debug.consoleInfo(cleartype
        ? "text pow set for cleartype "
        : "text pow set for grayscale ", pow);
    window.repaint();
  }

  public MouseListener desktopMouse() {
    return desktopMouse(windowCursor);
  }

  public static MouseListener desktopMouse(SetCursor windowCursor) {
    return new MouseListener() {
      @Override
      public boolean onMouseMove(MouseEvent event) {
//        windowCursor.set(null);
        return false;
      }
    };
  }

  public boolean onCopy(Consumer<String> setText, boolean isCut) {
    return focused != null && focused.onCopy(setText, isCut);
  }

  // paste handler
  @Override
  public Consumer<String> get() {
    return focused != null ? focused.onPaste() : null;
  }
}
