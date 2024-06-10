package org.sudu.experiments.js.node;

import org.sudu.experiments.DirectoryHandle;
import org.sudu.experiments.FsItem;
import org.sudu.experiments.js.JsArray;
import org.sudu.experiments.math.ArrayOp;
import org.teavm.jso.core.JSString;

public class NodeDirectoryHandle implements DirectoryHandle {
  final String name;
  final String[] path;

  public NodeDirectoryHandle(String name, String[] path) {
    this.name = name;
    this.path = path;
  }

  public NodeDirectoryHandle(JSString jsPath) {
    this.name = Fs.pathBasename(jsPath).stringValue();
    this.path = new String[]{
        Fs.pathDirname(jsPath).stringValue()
    };
  }

  JSString jsPath() {
    return Fs.concatPath(name, path);
  }

  @Override
  public void read(Reader reader) {
    JSString jsPath = jsPath();
    Fs fs = Fs.fs();
    JsArray<JSString> content = fs.readdirSync(jsPath);
    String[] childPath = ArrayOp.add(path, name);
    for (int i = 0; i < content.getLength(); i++) {
      JSString file = content.get(i);
      JSString child = Fs.concatPath(jsPath, file);
      var stats = fs.lstatSync(child);
      if (stats.isDirectory()) {
        reader.onDirectory(
            new NodeDirectoryHandle(file.stringValue(), childPath));
      } else {
        reader.onFile(
            new NodeFileHandle(file.stringValue(), childPath));
      }
    }
    reader.onComplete();
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
