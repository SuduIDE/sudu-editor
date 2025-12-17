package org.sudu.experiments.js.node;

import org.sudu.experiments.FileHandle;
import org.teavm.jso.core.JSString;

import java.util.Arrays;
import java.util.Objects;

public abstract class NodeFileHandle0 implements FileHandle {

  final String name;
  final String[] path;
  JSString jsPath;
  JSString sep;

  protected NodeFileHandle0(String name, String[] path, JSString sep) {
    this.name = name;
    this.path = path;
    this.sep = sep;
  }

  protected NodeFileHandle0(
      JSString jsPath,
      JSString pathBasename,
      JSString pathDirname,
      JSString sep
  ) {
    this.jsPath = jsPath;
    this.name = pathBasename.stringValue();
    this.path = NodeDirectoryHandle0.makePath(pathDirname);
    this.sep = sep;
  }

  public JSString jsPath() {
    if (jsPath == null)
      jsPath = Fs.concatPath(name, path, sep);
    return jsPath;
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
    return name;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(name) * 31 + Arrays.hashCode(path);
  }
}

