package org.sudu.experiments;

import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

public interface DiffEngineJs extends JSObject {
  void dispose();

  AsyncShutdown startFolderDiff(JSString leftPath, JSString rightPath, Channel channel);

  JsDiffTestApi testApi();
}
