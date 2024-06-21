package org.sudu.experiments;

import org.sudu.experiments.js.JsArray;
import org.sudu.experiments.js.JsHelper;
import org.sudu.experiments.js.JsMemoryAccess;
import org.sudu.experiments.js.Promise;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

public class ChannelTest implements ChannelTestApi {

  final Channel channel;

  public ChannelTest(Channel c) {
    channel = c;
    channel.setOnMessage(m -> onMessage(m));
  }

  void onMessage(JsArray<JSObject> array) {
    var l = LoggingJs.Static.logger;
    if (l != null) {
      l.log(LoggingJs.INFO, JSString.valueOf("ChannelTest.onMessage: "));
      l.log(LoggingJs.INFO, array.toJsString());
    } else {
      JsHelper.consoleInfo("ChannelTest.onMessage: ", array);
    }
  }

  @Override
  public void foo() {
    JsArray<JSObject> data = JsArray.create();
    data.push(JSString.valueOf("ChannelTest::foo"));
    data.push(JsMemoryAccess.bufferView(
        new int[] { 13, 17, 19, 23, 29 }
    ));
    data.push(JsMemoryAccess.bufferView(
        new byte[] { 1, 3, 5, 7, 11 }
    ));
    channel.sendMessage(data);
  }

  static Promise<ChannelTestApi> createChannelTestApi(Channel channel) {
    return Promise.resolve(new ChannelTest(channel));
  }

  public static void publishChannelTest() {
    ChannelTestApiFactory.Setter.set(ChannelTest::createChannelTestApi);
  }
}
