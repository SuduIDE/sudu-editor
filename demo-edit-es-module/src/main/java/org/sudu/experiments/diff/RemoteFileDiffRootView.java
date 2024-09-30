package org.sudu.experiments.diff;

import org.sudu.experiments.Channel;
import org.sudu.experiments.js.JsArray;
import org.sudu.experiments.protocol.JsCast;
import org.sudu.experiments.ui.window.WindowManager;

import org.sudu.experiments.editor.worker.diff.DiffUtils;
import org.sudu.experiments.update.FileDiffChannelUpdater;
import org.teavm.jso.core.JSString;

public class RemoteFileDiffRootView extends FileDiffRootView {

  private final Channel channel;

  RemoteFileDiffRootView(WindowManager wm, Channel channel) {
    super(wm);
    this.channel = channel;
  }

  @Override
  protected void sendToDiff() {
    var document1 = editor1.model().document;
    var document2 = editor2.model().document;
    String src1 = new String(document1.getChars());
    String src2 = new String(document2.getChars());
    int[] intervals1 = DiffUtils.makeIntervals(document1);
    int[] intervals2 = DiffUtils.makeIntervals(document2);

    var jsArray = JsArray.create();
    jsArray.set(0, JSString.valueOf(src1));
    jsArray.set(1, JSString.valueOf(src2));
    jsArray.set(2, JsCast.jsInts(intervals1));
    jsArray.set(3, JsCast.jsInts(intervals2));
    jsArray.push(FileDiffChannelUpdater.SEND_DIFF_MESSAGE);
    System.out.println("Message sent");
    channel.sendMessage(jsArray);
  }

  @Override
  protected void sendIntervalToDiff(int fromL, int toL, int fromR, int toR) {
    var document1 = editor1.model().document;
    var document2 = editor2.model().document;
    String src1 = new String(document1.getChars());
    String src2 = new String(document2.getChars());
    int[] intervals1 = DiffUtils.makeIntervals(document1, fromL, toL);
    int[] intervals2 = DiffUtils.makeIntervals(document2, fromR, toR);

    var jsArray = JsArray.create();
    jsArray.set(0, JSString.valueOf(src1));
    jsArray.set(1, JSString.valueOf(src2));
    jsArray.set(2, JsCast.jsInts(intervals1));
    jsArray.set(3, JsCast.jsInts(intervals2));
    jsArray.set(4, JsCast.jsInts(fromL, toL, fromR, toR));
    jsArray.push(FileDiffChannelUpdater.SEND_INT_DIFF_MESSAGE);
    channel.sendMessage(jsArray);
  }
}
