package org.sudu.experiments;

import org.sudu.experiments.js.JsFunctions;
import org.sudu.experiments.js.Promise;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

public interface DiffEngineJs extends JSObject {
  void dispose();

  Promise<JSString> fib(int n);

  void startFolderDiff(JSString leftPath, JSString rightPath, Channel channel);

  void testFS(JSString path, JsFunctions.Runnable onComplete);
  void testFS2(JSString path1, JSString path2, JsFunctions.Runnable onComplete);
  void testDiff(JSString path1, JSString path2, boolean content,
                JsFunctions.Runnable onComplete);

}
