package org.sudu.experiments.js.node;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSObjects;
import org.teavm.jso.core.JSString;

public abstract class Fs implements NodeFs {
  @JSBody(script = "return fs")
  public static native Fs fs();

  @JSBody(script = "return {throwIfNoEntry: false};")
  public static native JSObject lStatNoThrow();

  @JSBody(params = {"dir", "file"}, script = "return dir + path.sep + file;")
  public static native JSString concatPath(JSString dir, JSString file);

  @JSBody(script = "return path.sep.charAt(0);")
  public static native JSString pathSepChar();

  @JSBody(params = {"file"}, script = "return path.basename(file);")
  public static native JSString pathBasename(JSString file);

  @JSBody(params = {"file"}, script = "return path.dirname(file);")
  public static native JSString pathDirname(JSString file);

  @JSBody(params = {"error"}, script = "return error.cause;")
  public static native String errorCause(JSObject error);

  @JSBody(
      params = {"recursive"},
      script = "return {recursive : recursive};")
  public static native JSObject cpOptions(boolean recursive);

  public static JSString concatPath(String name, String[] path) {
    JSString jsPath = JSString.valueOf(name);
    for (int i = path.length - 1; i >= 0; i--) {
      jsPath = concatPath(JSString.valueOf(path[i]), jsPath);
    }
    return jsPath;
  }

  public static boolean isDirectory(JSString path) {
    Stats stats = fs().lstatSync(path, lStatNoThrow());
    return stats != JSObjects.undefined() && stats.isDirectory();
  }

  public static boolean isFile(JSString path) {
    Stats stats = fs().lstatSync(path, lStatNoThrow());
    return stats != JSObjects.undefined() && stats.isFile();
  }
}
