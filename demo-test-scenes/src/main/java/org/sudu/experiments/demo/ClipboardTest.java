package org.sudu.experiments.demo;

import org.sudu.experiments.Debug;
import org.sudu.experiments.Scene0;
import org.sudu.experiments.SceneApi;
import org.sudu.experiments.input.InputListener;
import org.sudu.experiments.input.KeyCode;
import org.sudu.experiments.input.KeyEvent;
import org.sudu.experiments.input.MouseEvent;

import java.util.function.Consumer;

public class ClipboardTest  extends Scene0 implements InputListener {
    int id;

    public ClipboardTest(SceneApi api) {
        super(api);
        api.input.addListener(this);
        boolean readClipboardTextSupported = api.window.isReadClipboardTextSupported();
        Debug.consoleInfo("ReadClipboardText is " +
                (readClipboardTextSupported ? "supported" : "unsupported"));
    }

    @Override
    public boolean onKey(KeyEvent event) {
        if (event.isPressed) {
            switch (event.keyCode) {
                case KeyCode.C -> writeClipboard();
                case KeyCode.V -> readClipboard();
            }
        }
        return true;
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
    public boolean onMousePress(MouseEvent event, int button, boolean press, int clickCount) {
        if (press) {
            switch (button) {
                case MOUSE_BUTTON_LEFT -> readClipboard();
                case MOUSE_BUTTON_RIGHT -> writeClipboard();
            }
        }
        return true;
    }

    @Override
    public boolean onCopy(Consumer<String> setText, boolean isCut) {
        String onCopy = (++id) + " on Copy" + ClipboardTest.class.getName();
        Debug.consoleInfo(onCopy);
        setText.accept(onCopy);
        return true;
    }

    @Override
    public Consumer<String> onPastePlainText() {
        return s -> Debug.consoleInfo((++id) + " onPastePlainText: " + s);
    }

    @Override
    public boolean onContextMenu(MouseEvent event) {
        return true;
    }
}
