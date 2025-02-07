package org.sudu.experiments.js.node;

import org.sudu.experiments.JsFileInput;
import org.sudu.experiments.JsHasPath;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSObjects;

// export type SshInput = { path: string, ssh: SSHCredentials }

public interface JsSshInput extends JsHasPath {

  static boolean isInstance(JSObject input) {
    return hasSsh(input) && JsFileInput.hasPath(input);
  }

  static boolean hasSsh(JSObject input) {
    return JSObjects.hasProperty(input, "ssh");
  }

  static JsSshCredentials getSsh(JSObject input) {
    return input.<JsSshInput>cast().getSsh();
  }

  @JSProperty
  JsSshCredentials getSsh();
}
