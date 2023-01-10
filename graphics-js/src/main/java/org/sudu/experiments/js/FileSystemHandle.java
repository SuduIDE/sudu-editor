package org.sudu.experiments.js;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSString;

public interface FileSystemHandle extends JSObject {

  @JSProperty JSString getKind();
  @JSProperty JSString getName();

  @JSBody(script = "return this.kind === 'directory'")
  boolean isDirectory();

  @JSBody(script = "return this.kind === 'file'")
  boolean isFile();
}
