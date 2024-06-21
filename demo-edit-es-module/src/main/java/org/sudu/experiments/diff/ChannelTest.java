package org.sudu.experiments.diff;

import org.sudu.experiments.Channel;
import org.sudu.experiments.Editor_d_ts;
import org.sudu.experiments.js.JsArray;
import org.sudu.experiments.js.JsHelper;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

public class ChannelTest implements Editor_d_ts.ChannelTestApi {

  final Channel channel;

  public ChannelTest(Channel c) {
    channel = c;
    channel.setOnMessage(m -> onMessage(m));
  }

  void onMessage(JsArray<JSObject> array) {
    JsHelper.consoleInfo(array);
    JsHelper.consoleInfo("ChannelTest.onMessage: ", array);
  }

  @Override
  public void foo() {
    JsArray<JSObject> data = JsArray.create();
    data.push(JSString.valueOf("ChannelTest::foo"));
    channel.sendMessage(data);
  }
}
