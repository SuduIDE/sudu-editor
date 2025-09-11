package org.sudu.experiments.diff;

import org.sudu.experiments.Channel;
import org.sudu.experiments.LoggingJs;
import org.sudu.experiments.editor.EditorComponent;
import org.sudu.experiments.editor.Model;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.editor.worker.diff.DiffInfo;
import org.sudu.experiments.esm.JsContextMenuProvider;
import org.sudu.experiments.esm.JsExternalMessageBar;
import org.sudu.experiments.input.KeyCode;
import org.sudu.experiments.input.KeyEvent;
import org.sudu.experiments.js.JsArray;
import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.protocol.JsCast;
import org.sudu.experiments.ui.ToolbarItem;
import org.sudu.experiments.ui.window.WindowManager;
import org.sudu.experiments.update.FileDiffChannelUpdater;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

import java.util.function.Supplier;

import static org.sudu.experiments.editor.worker.diff.DiffUtils.readDiffInfo;
import static org.sudu.experiments.esm.JsContextMenuProvider.*;
import static org.sudu.experiments.js.JsHelper.*;

public class RemoteFileDiffWindow extends FileDiffWindow {

  JsExternalMessageBar messageBar;
  JsContextMenuProvider contextMenuProvider;

  private final Channel channel;
  private boolean needScrollSync = false;
  private boolean haveLeftHandle, haveRightHandle;
  private int lastLeftScrollPos = -1, lastRightScrollPos = -1;
  private V2i lastLeftCaretPos = null, lastRightCaretPos = null;

  public RemoteFileDiffWindow(
      WindowManager wm,
      EditorColorScheme theme,
      Supplier<String[]> fonts,
      boolean disableParser,
      Channel channel
  ) {
    super(wm, theme, fonts, disableParser, false);
    canSelectFiles = false;
    rootView.setOnRefresh(this::onRefresh);
    rootView.setOnDiffModelSet(this::onDiffModelSet);
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
    needScrollSync = true;
    rootView.editor1.clearCodeMap();
    rootView.editor1.setMergeButtons(ArrayOp.array(), null, new int[]{});
    rootView.editor2.clearCodeMap();
    rootView.editor2.setMergeButtons(ArrayOp.array(), null, new int[]{});
    rootView.middleLine.setModel(null);

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

  private void onDiffModelSet() {
    LoggingJs.trace("RemoteFileDiffWindow.onDiffModelSet");
    if (needScrollSync) {
      needScrollSync = false;
      if (focusSave == rootView.editor2) {
        rootView.diffSync.sync(0, rootView.editor1, rootView.editor2);
      } else {
        rootView.diffSync.sync(0, rootView.editor2, rootView.editor1);
      }
    }
    printStat();
  }

  public void printStat() {
    var diffInfo = rootView.diffModel;
    int diffRanges = 0;
    int linesInserted = 0, linesDeleted = 0;
    for (var range: diffInfo.ranges) {
      if (range.type == DiffTypes.DEFAULT) continue;
      diffRanges++;
      linesDeleted += range.lenL;
      linesInserted += range.lenR;
    }
    // '{} difference(s): {} lines deleted, {} lines inserted'
    JSString statusBarMsg = JSString.valueOf(sSuffix(linesDeleted, "line") + " deleted"
        + ". " + sSuffix(linesInserted, "line") + " inserted");
    JSString toolBarMsg = JSString.valueOf(sSuffix(diffRanges, "difference"));

    LoggingJs.info(statusBarMsg);
    if (messageBar != null) {
      messageBar.setStatusBarMessage(statusBarMsg);
      messageBar.setToolBarMessage(toolBarMsg);
    }
  }

  static String sSuffix(int n) {
    return n != 1 ? "s" : "";
  }

  public static String sSuffix(int n, String name) {
    return switch (n) {
      case 0 -> "No " + name + "s";
      case 1 -> "1 " + name;
      default -> n + " " + name + "s";
    };
  }

  private void updateOnRefresh() {
    if (lastLeftCaretPos != null) rootView.editor1.setPosition(lastLeftCaretPos.y, lastLeftCaretPos.x);
    if (lastRightCaretPos != null) rootView.editor2.setPosition(lastRightCaretPos.y, lastRightCaretPos.x);
    if (lastLeftScrollPos != -1) rootView.editor1.setVScrollPosSilent(lastLeftScrollPos);
    if (lastRightScrollPos != -1) rootView.editor2.setVScrollPosSilent(lastRightScrollPos);
  }

  @Override
  public boolean onKeyPress(KeyEvent event) {
    return event.keyCode == KeyCode.ESC
        && super.onKeyPress(event);
  }

  @Override
  protected Supplier<ToolbarItem[]> popupActions(V2i pos) {
    return super.popupActions(pos);
  }

  @Override
  protected boolean onContextMenu(V2i pos) {
    if (contextMenuProvider != null) {
      var editor = focused();
      if (editor != null) {
        var actions = cutCopyPaste();
        if (editor.canAlignWith())
          actions.push(jsAlignWith());
        else
          actions.push(jsRemoveAlignment());
//        consoleInfo2("open contextMenuProvider:", actions);
        contextMenuProvider.showContextMenu(actions);
      }
      return false;
    } else {
      return super.onContextMenu(pos);
    }
  }

  void executeCommand(JSString command) {
    consoleInfo2("RemoteFileDiffWindow.executeCommand:", command);
    var f = focused();
    if (f != null) executeEditorCommand(command, f);
  }

  static void executeEditorCommand(JSString command, EditorComponent ed) {
    if (strictEquals(command, jsCut()))
      ed.cutCopy(true);
    else if (strictEquals(command, jsCopy()))
      ed.cutCopy(false);
    else if (strictEquals(command, jsPaste()))
      ed.paste();
    else if (strictEquals(command, jsAlignWith()))
      ed.alignWith();
    else if (strictEquals(command, jsRemoveAlignment()))
      ed.removeAlignment();
    else
      consoleError("Unknown command: ", command);
  }
}
