package org.sudu.experiments.demo;

import org.sudu.experiments.Disposable;

interface EditApi {
  void setText(byte[] urf8bytes);
  byte[] getText();

  boolean selectAll();

  Disposable addListener(Listener listener);

  interface Listener {
    void somethingHappened();
  }
}