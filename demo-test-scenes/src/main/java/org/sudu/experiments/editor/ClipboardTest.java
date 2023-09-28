package org.sudu.experiments.editor;

import org.sudu.experiments.Debug;
import org.sudu.experiments.Scene0;
import org.sudu.experiments.SceneApi;
import org.sudu.experiments.input.KeyCode;
import org.sudu.experiments.input.KeyEvent;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.input.MouseListener;

import java.util.function.Consumer;

public class ClipboardTest  extends Scene0 implements MouseListener {
  int id;

  public ClipboardTest(SceneApi api) {
    super(api);
    api.input.onMouse.add(this);
    api.input.onKeyPress.add(this::onKeyPress);
    api.input.onCopy.add((setText, isCut) -> false);
    api.input.onCopy.add(this::onCopy);
    api.input.onPaste.add(() -> null);
    api.input.onPaste.add(this::onPastePlainText);
    boolean readClipboardTextSupported = api.window.isReadClipboardTextSupported();
    Debug.consoleInfo("ReadClipboardText is " +
        (readClipboardTextSupported ? "supported" : "unsupported"));
  }

  private boolean onKeyPress(KeyEvent event) {
    return switch (event.keyCode) {
      case KeyCode.C -> { writeClipboard(); yield true; }
      case KeyCode.V -> { readClipboard(); yield true; }
      default -> false;
    };
  }

  private void readClipboard() {
    api.window.readClipboardText(
        value -> Debug.consoleInfo((++id) + " readClipboardText: " + value),
        onError());
  }

  private void writeClipboard() {
    String text = "writeClipboardText " + (++id) + ": " + ClipboardTest.class.getName();
    api.window.writeClipboardText(
        text,
        () -> Debug.consoleInfo(" writeClipboardText '" + text + "' ok"),
        onError());

  }

  private Consumer<Throwable> onError() {
    return e -> Debug.consoleInfo((++id) + " error: " + e.getMessage());
  }

  @Override
  public boolean onMouseClick(MouseEvent event, int button, int clickCount) {
    switch (button) {
      case MOUSE_BUTTON_LEFT -> readClipboard();
      case MOUSE_BUTTON_RIGHT -> writeClipboard();
    }
    return true;
  }

  boolean onCopy(Consumer<String> setText, boolean isCut) {
    String onCopy = (++id) + " on Copy" + ClipboardTest.class.getName();
    Debug.consoleInfo(onCopy);
    setText.accept(onCopy);
    return true;
  }

  Consumer<String> onPastePlainText() {
    return s -> Debug.consoleInfo((++id) + " onPastePlainText: " + s);
  }
}
