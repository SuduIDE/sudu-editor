package org.sudu.experiments.js.node;

import org.sudu.experiments.DirectoryHandle;
import org.sudu.experiments.FsItem;
import org.sudu.experiments.js.JsHelper;
import org.teavm.jso.core.JSString;

import java.util.Arrays;

public abstract class NodeDirectoryHandle0 implements DirectoryHandle {
  final String name;
  final String[] path;
  final JSString sep;

  public NodeDirectoryHandle0(String name, String[] path, JSString sep) {
    this.name = name;
    this.path = path;
    this.sep = sep;
  }

  public NodeDirectoryHandle0(JSString pathBasename, JSString pathDirname, JSString sep) {
    this.name = pathBasename.stringValue();
    this.path = pathDirname == null ? new String[0] : new String[]{
        pathDirname.stringValue()
    };
    this.sep = sep;
    JsHelper.consoleInfo2("NodeDirectoryHandle0: pathBasename =", pathBasename);
    JsHelper.consoleInfo2("NodeDirectoryHandle0: pathDirname =", pathDirname);
  }

  JSString jsPath() {
    return Fs.concatPath(name, path, sep);
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String[] getPath() {
    return path;
  }

  @Override
  public String toString() {
    return FsItem.toString("dir", path, name, false);
  }
}
