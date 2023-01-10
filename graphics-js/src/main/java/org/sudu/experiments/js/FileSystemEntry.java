package org.sudu.experiments.js;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSString;

interface FileSystemEntry extends JSObject {
  @JSProperty JSString getFilePath();
  @JSProperty JSString getName();
  @JSProperty boolean getIsDirectory();
  @JSProperty boolean getIsFile();
}
