package org.sudu.experiments.js.node;

import org.sudu.experiments.DirectoryHandle;
import org.sudu.experiments.FsItem;
import org.teavm.jso.core.JSString;

public abstract class NodeDirectoryHandle0 implements DirectoryHandle {
  final String name;
  final String[] path;

  public NodeDirectoryHandle0(String name, String[] path) {
    this.name = name;
    this.path = path;
  }

  public NodeDirectoryHandle0(JSString jsPath) {
    this.name = Fs.pathBasename(jsPath).stringValue();
    this.path = new String[]{
        Fs.pathDirname(jsPath).stringValue()
    };
  }

  JSString jsPath() {
    return Fs.concatPath(name, path);
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
