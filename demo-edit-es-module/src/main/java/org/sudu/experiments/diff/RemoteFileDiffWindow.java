package org.sudu.experiments.diff;

import org.sudu.experiments.Channel;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.editor.worker.diff.DiffInfo;
import org.sudu.experiments.js.JsArray;
import org.sudu.experiments.protocol.JsCast;
import org.sudu.experiments.ui.window.WindowManager;
import org.sudu.experiments.update.FileDiffChannelUpdater;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

import java.util.function.Supplier;

import static org.sudu.experiments.editor.worker.diff.DiffUtils.readDiffInfo;

public class RemoteFileDiffWindow extends FileDiffWindow {

  private final Channel channel;

  public RemoteFileDiffWindow(
      WindowManager wm,
      EditorColorScheme theme,
      Supplier<String[]> fonts,
      Channel channel
  ) {
//    super(wm, theme, fonts, (_wm) -> new RemoteFileDiffRootView(_wm, channel));
    super(wm, theme, fonts);
    this.channel = channel;
    this.channel.setOnMessage(this::onMessage);
    this.setOnDiffMade(
        src -> saveFile(true, src),
        src -> saveFile(false, src)
    );
  }

  private void saveFile(boolean left, String source) {
    JsArray<JSObject> jsArray = JsArray.create();
    jsArray.set(0, JSString.valueOf(source));
    jsArray.set(1, JsCast.jsInts(left ? 1 : 0));
    jsArray.push(FileDiffChannelUpdater.FILE_SAVE_MESSAGE);
    channel.sendMessage(jsArray);
  }

  private void onMessage(JsArray<JSObject> jsArray) {
    int type = JsCast.ints(jsArray.pop())[0];
    switch (type) {
      case FileDiffChannelUpdater.FILE_READ -> onFileOpen(jsArray);
      case FileDiffChannelUpdater.SEND_DIFF -> onDiffSent(jsArray);
    }
  }

  private void onFileOpen(JsArray<JSObject> jsArray) {
    String source = JsCast.string(jsArray, 0);
    String name = JsCast.string(jsArray, 1);
    boolean left = JsCast.ints(jsArray, 2)[0] == 1;
    open(source, name, left);
  }

  private void onDiffSent(JsArray<JSObject> jsArray) {
    int[] modelInts = JsCast.ints(jsArray, 0);
    DiffInfo model = readDiffInfo(modelInts);
    rootView.setDiffModel(model);
  }
}
