package org.sudu.experiments.js.node;

import org.sudu.experiments.js.JsFunctions.BiConsumer;
import org.sudu.experiments.js.JsFunctions.Consumer;
import org.sudu.experiments.js.JsFunctions.Runnable;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSError;

public interface JsSshClient extends JSObject {
  void on(String key, Runnable handler);
  void on(String key, Consumer<JSObject> handler);

  void sftp(BiConsumer<JSError, JsSftpClient> handler);

  void connect(JSObject sshCredentials);
  void end();

  default void onReady(Runnable handler) {
    on("ready", handler);
  }

  default void onError(Consumer<JSObject> handler) {
    on("error", handler);
  }
}
