package org.sudu.experiments.js.node;

import org.sudu.experiments.js.JsArray;
import org.sudu.experiments.js.JsFunctions;
import org.sudu.experiments.js.JsHelper;
import org.teavm.jso.*;
import org.teavm.jso.core.JSBoolean;
import org.teavm.jso.core.JSError;
import org.teavm.jso.core.JSString;
import org.teavm.jso.typedarrays.ArrayBufferView;

import java.util.function.Consumer;

public interface NodeFs extends JSObject {
  @JSProperty("constants")
  Constants constants();

  // https://nodejs.org/api/fs.html#fsconstants
  interface Constants extends JSObject {
    @JSProperty("O_RDONLY")
    int O_RDONLY();

    @JSProperty("O_RDWR")
    int O_RDWR();

    @JSProperty("O_CREAT")
    int O_CREAT();

    @JSProperty("O_EXCL")
    int O_EXCL();

    @JSProperty("O_APPEND")
    int O_APPEND();

    @JSProperty("COPYFILE_EXCL")
    int COPYFILE_EXCL();

    @JSProperty("COPYFILE_FICLONE")
    int COPYFILE_FICLONE();

    @JSProperty("COPYFILE_FICLONE_FORCE")
    int COPYFILE_FICLONE_FORCE();
  }

  // https://nodejs.org/api/fs.html#class-fsstats
  interface Stats extends JSObject {
    boolean isDirectory();

    boolean isFile();

    boolean isSymbolicLink();

    @JSProperty("size")
    double size();
  }

  Stats lstatSync(JSString name);

  Stats lstatSync(JSString name, JSObject options);

  interface Dirent extends JSObject {
    boolean isDirectory();

    boolean isFile();

    @JSProperty("name")
    JSString name();
  }

  JsArray<JSString> readdirSync(JSString string);

  JsArray<JSString> readdirSync(JSString string, JSObject options);

  JSBoolean existsSync(JSString name);

  void mkdirSync(JSString name, JSObject options);

  int openSync(JSString name, int mode);

  int readSync(
      int handle,
      ArrayBufferView buffer, int bufferOffset,
      int bytesToRead, double position);

  void closeSync(int handle);

  @JSFunctor
  interface ReadCallback extends JSObject {
    void f(JSObject error, JSString result);
  }

  void readFile(JSString name, JSString encoding, ReadCallback callback);

  void writeFile(
      JSString name, JSString content, JSString encoding,
      JsFunctions.Consumer<JSError> callback);

  void copyFile(
      JSString src, JSString dest, int mode,
      JsFunctions.Consumer<JSError> callback);

  void unlink(JSString name, JsFunctions.Consumer<JSError> callback);

  void cp(
      JSString src, JSString dest, JSObject options,
      JsFunctions.Consumer<JSError> callback);

  // todo rewrite to rm?
  void rmdir(JSString name, JSObject options, JsFunctions.Consumer<JSError> callback);

  static JsFunctions.Consumer<JSError> callback(Runnable onComplete, Consumer<String> onError) {
    return error -> {
      if (error == null) {
        onComplete.run();
      } else {
        onError.accept(JsHelper.getMessage(error).stringValue());
      }
    };
  }

  // todo rewrite parent path searching
  static String parent(String path) {
    int slIndex = path.lastIndexOf('/');
    int revSlInd = path.lastIndexOf('\\');
    if (slIndex == -1 && revSlInd == -1) {
      return null;
    } else {
      int ind = Math.max(slIndex, revSlInd);
      System.out.println(path.substring(0, ind));
      return path.substring(0, ind);
    }
  }
}
