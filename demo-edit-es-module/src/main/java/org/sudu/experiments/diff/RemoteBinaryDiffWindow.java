package org.sudu.experiments.diff;

import org.sudu.experiments.Channel;
import org.sudu.experiments.LoggingJs;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.esm.JsNotificationsProvider;
import org.sudu.experiments.js.JsArray;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.protocol.JsCast;
import org.sudu.experiments.ui.ToolbarItem;
import org.sudu.experiments.ui.window.WindowManager;
import org.sudu.experiments.update.FileDiffChannelUpdater;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

import java.util.function.Supplier;

public class RemoteBinaryDiffWindow extends BinaryDiffWindow {

  private final Channel channel;
  private JsNotificationsProvider notificationsProvider;
  private Runnable onRefresh;

  public RemoteBinaryDiffWindow(
      WindowManager wm,
      EditorColorScheme theme,
      Supplier<String[]> fonts,
      Channel channel
  ) {
    super(wm, theme, fonts);
    rootView.setNavigate(this::navigate);
    this.channel = channel;
  }

  public void setOnRefresh(Runnable onRefresh) {
    this.onRefresh = onRefresh;
  }

  public void setNotificationsProvider(JsNotificationsProvider p) {
    this.notificationsProvider = p;
  }

  @Override
  protected Supplier<ToolbarItem[]> popupActions(V2i pos) {
    return null;
  }

  private void navigate(boolean next) {
    int firstLine = firstLine();
    double address = firstLine * rootView.bytesPerLine;
    boolean skipDiff = next && rootView.firstLineEdited;
    JsArray<JSObject> jsArray = JsArray.create();
    jsArray.set(0, JsCast.jsInts(rootView.bytesPerLine, skipDiff ? 1 : 0, next ? 1 : 0));
    jsArray.set(1, JsCast.jsNumbers(address));
    jsArray.push(FileDiffChannelUpdater.BIN_NAVIGATE_ARRAY);
    channel.sendMessage(jsArray);
  }

  void onNotification(JSString notification) {
    LoggingJs.info("onNotification: " + notification.stringValue());
    notificationsProvider.info(notification);
  }

  public void refresh() {
    if (onRefresh != null) {
      onRefresh.run();
      window.context.repaint.run();
    }
  }
}
