package org.sudu.experiments.diff;

import org.sudu.experiments.Channel;
import org.sudu.experiments.LoggingJs;
import org.sudu.experiments.editor.Model;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.editor.worker.diff.DiffInfo;
import org.sudu.experiments.esm.JsExternalStatusBar;
import org.sudu.experiments.js.JsArray;
import org.sudu.experiments.protocol.JsCast;
import org.sudu.experiments.ui.window.WindowManager;
import org.sudu.experiments.update.FileDiffChannelUpdater;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

import java.util.function.Supplier;

import static org.sudu.experiments.editor.worker.diff.DiffUtils.readDiffInfo;

public class RemoteFileDiffWindow extends FileDiffWindow {

  JsExternalStatusBar statusBar;

  private final Channel channel;
  private boolean haveLeftHandle, haveRightHandle;
  private int lastLeftScrollPos = -1;
  private int lastRightScrollPos = -1;

  public RemoteFileDiffWindow(
      WindowManager wm,
      EditorColorScheme theme,
      Supplier<String[]> fonts,
      Channel channel
  ) {
    super(wm, theme, fonts);
    rootView.setOnRefresh(this::onRefresh);
    processEsc = false;
    this.channel = channel;
    this.channel.setOnMessage(this::onMessage);
    this.setOnDiffMade(
        src -> saveFile(true, src),
        src -> saveFile(false, src)
    );
    this.rootView.setOnFileDiffGet(this::onFileDiffGet);
  }

  private void saveFile(boolean left, Model m) {
    JsArray<JSObject> jsArray = JsArray.create();
    jsArray.set(0, JsCast.jsString(m.document.getChars()));
    jsArray.set(1, JSString.valueOf(m.encoding()));
    jsArray.set(2, JsCast.jsInts(left ? 1 : 0));
    jsArray.set(3, JsCast.jsInts(FileDiffChannelUpdater.FILE_SAVE));
    channel.sendMessage(jsArray);
  }

  private void onMessage(JsArray<JSObject> jsArray) {
    int type = JsCast.ints(jsArray.pop())[0];
    switch (type) {
      case FileDiffChannelUpdater.FILE_READ -> onFileRead(jsArray);
      case FileDiffChannelUpdater.SEND_DIFF -> onDiffSent(jsArray);
    }
  }

  private void onFileRead(JsArray<JSObject> jsArray) {
    String source = JsCast.string(jsArray, 0);
    String encoding = JsCast.string(jsArray, 1);
    String name = JsCast.string(jsArray, 2);
    boolean left = JsCast.ints(jsArray, 3)[0] == 1;
    boolean haveHandle = JsCast.ints(jsArray, 3)[1] == 1;
    if (left) haveLeftHandle = haveHandle;
    else haveRightHandle = haveHandle;
    LoggingJs.debug(
        "RemoteFileDiffWindow.open:  name = " + name
            + ", encoding = " + encoding);
    open(source, encoding, name, left);
    var editor = left ? rootView.editor1 : rootView.editor2;
    int newScrollPos = left ? lastLeftScrollPos : lastRightScrollPos;
    if (newScrollPos != -1) editor.setVScrollPosSilent(newScrollPos);
  }

  private void onDiffSent(JsArray<JSObject> jsArray) {
    int[] modelInts = JsCast.ints(jsArray, 0);
    DiffInfo model = readDiffInfo(modelInts);
    rootView.setDiffModel(model);
  }

  private void sendReadFile(boolean left) {
    LoggingJs.trace("RemoteFileDiffWindow.sendReadFile, left = " + left);
    JsArray<JSObject> jsArray = JsArray.create();
    jsArray.set(0, JsCast.jsInts(left ? 1 : 0));
    jsArray.push(JsCast.jsInts(FileDiffChannelUpdater.FILE_READ));
    channel.sendMessage(jsArray);
  }

  public void onRefresh() {
    LoggingJs.trace("RemoteFileDiffWindow.onRefresh");
    if (haveLeftHandle) {
      rootView.unsetModelFlagsBit(1);
      leftFile = null;
      sendReadFile(true);
      lastLeftScrollPos = rootView.editor1.getVScrollPos();
    }
    if (haveRightHandle) {
      rootView.unsetModelFlagsBit(2);
      rightFile = null;
      sendReadFile(false);
      lastRightScrollPos = rootView.editor2.getVScrollPos();
    }
  }

  public void onFileDiffGet() {
    var diffInfo = rootView.diffModel;
    int diffRanges = 0;
    for (var range: diffInfo.ranges) if (range.type != DiffTypes.DEFAULT) diffRanges++;
    String statMsg = "Total diffs: " + diffRanges;
    LoggingJs.info(statMsg);
    if (statusBar != null) statusBar.setMessage(JSString.valueOf(statMsg));
  }
}
