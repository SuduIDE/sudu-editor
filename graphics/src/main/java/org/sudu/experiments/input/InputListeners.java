package org.sudu.experiments.input;

import org.sudu.experiments.Disposable;
import org.sudu.experiments.math.ArrayOp;

import java.util.TreeMap;
import java.util.function.Consumer;

public class InputListeners extends Input {
  public final Runnable repaint;
  private final TreeMap<Integer, InputListener[]> map = new TreeMap<>();
  private final InputListener[] noListeners = new InputListener[0];
  private InputListener[] snapshot = noListeners;
  private boolean snapshotValid = true;
  private int size = 0;

  public InputListeners(Runnable repaint) {
    this.repaint = repaint;
  }

  @Override
  public Disposable addListener(InputListener l, int order) {
    snapshotValid = false;
    snapshot = noListeners;
    size++;
    Integer boxOrder = order;
    InputListener[] listeners = map.get(boxOrder);
    map.put(boxOrder, listeners == null ? new InputListener[]{l} : ArrayOp.add(listeners, l));
    return () -> removeListener(order, l);
  }

  private void removeListener(int order, InputListener element) {
    InputListener[] oldList = map.get(order);
    InputListener[] newList = ArrayOp.remove(oldList, element);
    if (newList == oldList) throw new RuntimeException("unexpected InputListener");
    snapshotValid = false;
    snapshot = noListeners;
    size--;
    if (newList != null) {
      map.put(order, newList);
    } else {
      map.remove(order);
    }
  }

  public void clear() {
    map.clear();
    size = 0;
    snapshot = noListeners;
    snapshotValid = true;
  }

  private InputListener[] snapshot() {
    if (snapshotValid) return snapshot;
    if (snapshot.length != size) snapshot = new InputListener[size];
    int pos = 0;
    for (InputListener[] listeners : map.values()) {
      System.arraycopy(listeners, 0, snapshot, pos, listeners.length);
      pos += listeners.length;
    }
    return snapshot;
  }

  public void sendBlurEvent() {
    for (InputListener listener : snapshot()) {
      listener.onBlur();
    }
    repaint.run();
  }

  public void sendFocusEvent() {
    for (InputListener listener : snapshot()) {
      listener.onFocus();
    }
    repaint.run();
  }

  @Override
  public boolean sendKeyEvent(KeyEvent e) {
    repaint.run();
    for (InputListener listener : snapshot()) {
      if (listener.onKey(e)) return true;
      if (e.prevented) break;
    }
    return false;
  }

  @Override
  public void sendMouseMove(MouseEvent e) {
    repaint.run();
    for (InputListener listener : snapshot()) {
      if (listener.onMouseMove(e)) return;
    }
  }

  @Override
  public boolean sendMouseButton(MouseEvent e, int button, boolean press, int count) {
    repaint.run();
    for (InputListener listener : snapshot()) {
      if (listener.onMousePress(e, button, press, count)) return true;
    }
    return false;
  }

  @Override
  public void sendMouseWheel(MouseEvent e, double dX, double dY) {
    repaint.run();
    for (InputListener listener : snapshot()) {
      if (listener.onMouseWheel(e, dX, dY)) return;
    }
  }

  @Override
  public boolean sendContextMenu(MouseEvent e) {
    repaint.run();
    for (InputListener listener : snapshot()) {
      if (listener.onContextMenu(e)) return true;
    }
    return false;
  }

  @Override
  public boolean sendCopy(Consumer<String> receiver, boolean isCut) {
    repaint.run();
    for (InputListener listener : snapshot()) {
      if (listener.onCopy(receiver, isCut)) return true;
    }
    return false;
  }

  @Override
  public Consumer<String> onPastePlainText() {
    for (InputListener listener : snapshot()) {
      Consumer<String> onPaste = listener.onPastePlainText();
      if (onPaste != null) return onPaste;
    }
    return null;
  }
}
