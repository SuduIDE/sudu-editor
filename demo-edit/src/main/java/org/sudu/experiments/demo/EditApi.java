package org.sudu.experiments.demo;

import org.sudu.experiments.Disposable;

interface EditApi {
  void setText(char[] charArray);
  char[] getText();

  boolean selectAll();

  Disposable addListener(Listener listener);

  interface Listener {
    void somethingHappened();
  }


}
