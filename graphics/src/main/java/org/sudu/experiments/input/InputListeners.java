package org.sudu.experiments.input;

import org.sudu.experiments.Subscribers;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class InputListeners {
  public final Subscribers<KeyHandler> onKeyPress = new Subscribers<>(new KeyHandler[0]);
  public final Subscribers<KeyHandler> onKeyRelease = new Subscribers<>(new KeyHandler[0]);

  public final Subscribers<MouseListener> onMouse = new Subscribers<>(new MouseListener[0]);
  public final Subscribers<ScrollHandler> onScroll = new Subscribers<>(new ScrollHandler[0]);

  public final Subscribers<ContextMenuHandler> onContextMenu = new Subscribers<>(new ContextMenuHandler[0]);

  public final Subscribers<CopyHandler> onCopy = new Subscribers<>(new CopyHandler[0]);
  public final Subscribers<PasteHandler> onPaste = new Subscribers<>(new PasteHandler[0]);

  public final Subscribers<Runnable> onBlur = new Subscribers<>(new Runnable[0]);
  public final Subscribers<Runnable> onFocus = new Subscribers<>(new Runnable[0]);

  public final Runnable repaint;

  public InputListeners(Runnable repaint) {
    this.repaint = repaint;
  }

  public void clear() {
    onKeyPress.clear();
    onKeyRelease.clear();
    onMouse.clear();
    onScroll.clear();
    onContextMenu.clear();
    onCopy.clear();
    onPaste.clear();
    onBlur.clear();
    onFocus.clear();
  }

  public void sendBlurEvent() {
    Runnable[] array = onBlur.array();
    for (Runnable listener : array) {
      listener.run();
    }
    repaint.run();
  }

  public void sendFocusEvent() {
    for (Runnable listener : onFocus.array()) {
      listener.run();
    }
    repaint.run();
  }

  public boolean sendKeyEvent(KeyEvent e) {
    repaint.run();
    var toSend = e.isPressed ? onKeyPress : onKeyRelease;

    for (Predicate<KeyEvent> listener : toSend.array()) {
      if (listener.test(e)) e.prevented = true;
      if (e.prevented) break;
    }

    return false;
  }

  public void sendMouseMove(MouseEvent e) {
    repaint.run();
    for (MouseListener listener : onMouse.array()) {
      if (listener.onMouseMove(e)) return;
    }
  }

  public boolean sendMouseButton(MouseEvent e, int button, boolean press, int count) {
    repaint.run();
    for (MouseListener listener : onMouse.array()) {
      if (listener.onMousePress(e, button, press, count)) return true;
    }
    return false;
  }

  public void sendMouseWheel(MouseEvent e, float dX, float dY) {
    repaint.run();
    for (ScrollHandler listener : onScroll.array()) {
      if (listener.onScroll(e, dX, dY)) return;
    }
  }

  public boolean sendContextMenu(MouseEvent e) {
    repaint.run();
    for (ContextMenuHandler listener : onContextMenu.array()) {
      if (listener.test(e)) return true;
    }
    return false;
  }

  public boolean sendCopy(Consumer<String> receiver, boolean isCut) {
    repaint.run();
    for (CopyHandler listener : onCopy.array()) {
      if (listener.onCopy(receiver, isCut)) return true;
    }
    return false;
  }

  public Consumer<String> onPastePlainText() {
    for (PasteHandler listener : onPaste.array()) {
      Consumer<String> onPaste = listener.get();
      if (onPaste != null) return onPaste;
    }
    return null;
  }

  public interface CopyHandler {
    boolean onCopy(Consumer<String> setText, boolean isCut);
  }

  public interface PasteHandler extends Supplier<Consumer<String>> {}

  public interface ContextMenuHandler extends Predicate<MouseEvent> {}

  public interface KeyHandler extends Predicate<KeyEvent> {}

  public interface ScrollHandler {
    boolean onScroll(MouseEvent event, float dX, float dY);
  }
}
