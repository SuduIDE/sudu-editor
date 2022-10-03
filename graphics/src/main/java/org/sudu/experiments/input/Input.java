package org.sudu.experiments.input;

import org.sudu.experiments.Disposable;

import java.util.function.Consumer;

public abstract class Input {

  public final Disposable addListener(InputListener l) {
    return addListener(l, 0);
  }

  public abstract Disposable addListener(InputListener l, int order);

  public abstract void sendBlurEvent();

  public abstract boolean sendKeyEvent(KeyEvent e);

  public abstract void sendMouseMove(MouseEvent e);

  public abstract boolean sendMouseButton(MouseEvent e, int button, boolean press, int count);

  public abstract void sendMouseWheel(MouseEvent e, double dX, double dY);

  public abstract boolean sendContextMenu(MouseEvent e);

  public abstract boolean sendCopy(Consumer<String> receiver, boolean isCut);

  public abstract Consumer<String> onPastePlainText();
}
