package org.sudu.experiments;

import org.sudu.experiments.js.Promise;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;

public interface ChannelTestApi extends JSObject {
  void foo();

  @JSFunctor
  interface ChannelTestApiFactory extends JSObject {
    Promise<ChannelTestApi> create(Channel channel);

    class Setter {
      @JSBody(params = {"f"}, script = "newRemoteChannelTest = f;")
      public static native void set(ChannelTestApiFactory f);
    }
  }
}
