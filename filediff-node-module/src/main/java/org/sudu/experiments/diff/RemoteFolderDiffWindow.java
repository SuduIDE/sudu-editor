package org.sudu.experiments.diff;

import org.sudu.experiments.Channel;
import org.sudu.experiments.Debug;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.js.JsArray;
import org.sudu.experiments.ui.window.WindowManager;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

import java.util.function.Supplier;

public class RemoteFolderDiffWindow extends FolderDiffWindow {

  protected Channel channel;

  public RemoteFolderDiffWindow(
      EditorColorScheme theme,
      WindowManager wm,
      Supplier<String[]> fonts,
      Channel channel
  ) {
    super(theme, wm, fonts);
    this.channel = channel;
    channel.setOnMessage(arr -> Debug.consoleInfo("From channel: ", arr));
  }

  @Override
  protected void compareRootFolders() {
    super.compareRootFolders();
    JsArray<JSObject> arr = JsArray.create();
    arr.push(JSString.valueOf("Start comparing root folders"));
    channel.sendMessage(arr);
  }

  @Override
  protected void updateDiffInfo() {
    super.updateDiffInfo();
    if (leftModel.compared && rightModel.compared) {
      JsArray<JSObject> arr = JsArray.create();
      arr.push(JSString.valueOf("Finish comparing root folders"));
      channel.sendMessage(arr);
    }
  }
}
