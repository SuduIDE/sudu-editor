package org.sudu.experiments.js.node;

import org.sudu.experiments.js.JsFunctions.BiConsumer;
import org.sudu.experiments.js.JsFunctions.Runnable;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSError;

public interface JsSshClient extends JSObject {
  void on(String key, Runnable handler);

  void sftp(BiConsumer<JSError, JsSftpClient> handler);

  void connect(JSObject sshCredentials);

  default void onReady(Runnable handler) {
    on("ready", handler);
  }
}
