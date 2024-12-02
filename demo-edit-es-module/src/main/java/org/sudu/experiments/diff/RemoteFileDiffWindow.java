package org.sudu.experiments.diff;

import org.sudu.experiments.Channel;
import org.sudu.experiments.LoggingJs;
import org.sudu.experiments.editor.Model;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.editor.worker.diff.DiffInfo;
import org.sudu.experiments.js.JsArray;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.protocol.JsCast;
import org.sudu.experiments.ui.window.WindowManager;
import org.sudu.experiments.update.FileDiffChannelUpdater;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

import java.util.function.Supplier;

import static org.sudu.experiments.editor.worker.diff.DiffUtils.readDiffInfo;

public class RemoteFileDiffWindow extends FileDiffWindow {

  private final Channel channel;
  private boolean haveLeftHandle, haveRightHandle;
  private int lastLeftScrollPos = -1, lastRightScrollPos = -1;
  private V2i lastLeftCaretPos = null, lastRightCaretPos = null;

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
    updateOnRefresh();
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
      lastLeftCaretPos = rootView.editor1.model().getCaretPos();
    }
    if (haveRightHandle) {
      rootView.unsetModelFlagsBit(2);
      rightFile = null;
      sendReadFile(false);
      lastRightScrollPos = rootView.editor2.getVScrollPos();
      lastRightCaretPos = rootView.editor2.model().getCaretPos();
    }
  }

  private void updateOnRefresh() {
    if (lastLeftCaretPos != null) rootView.editor1.setPosition(lastLeftCaretPos.y, lastLeftCaretPos.x);
    if (lastRightCaretPos != null) rootView.editor2.setPosition(lastRightCaretPos.y, lastRightCaretPos.x);
    if (lastLeftScrollPos != -1) rootView.editor1.setVScrollPosSilent(lastLeftScrollPos);
    if (lastRightScrollPos != -1) rootView.editor2.setVScrollPosSilent(lastRightScrollPos);
  }
}
