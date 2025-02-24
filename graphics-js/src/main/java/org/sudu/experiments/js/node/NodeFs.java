package org.sudu.experiments.js.node;

import org.sudu.experiments.js.JsArray;
import org.sudu.experiments.js.JsFunctions;
import org.sudu.experiments.js.JsHelper;
import org.teavm.jso.*;
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

  boolean existsSync(JSString name);

  void mkdirSync(JSString name, JSObject options);

  int openSync(JSString name, int mode);

  int readSync(
      int handle,
      ArrayBufferView buffer, int bufferOffset,
      int bytesToRead, double position);

  int writeSync(
      int handle, ArrayBufferView buffer,
      int bufferOffset, int bytesToWrite, double position
  );

  int closeSync(int handle);

  @JSFunctor
  interface ReadCallback extends JSObject {
    void f(JSObject error, JSString result);
  }

  void readFile(JSString name, JSString encoding, ReadCallback callback);

  void writeFile(
      JSString name, JSObject content, JSString encoding,
      JsFunctions.Consumer<JSObject> callback);

  void copyFile(
      JSString src, JSString dest, int mode,
      JsFunctions.Consumer<JSObject> callback);

  void copyFileSync(JSString src, JSString dest, int mode);

  void unlink(JSString name, JsFunctions.Consumer<JSObject> callback);
  void unlinkSync(JSString name);

  void cp(
      JSString src, JSString dest, JSObject options,
      JsFunctions.Consumer<JSObject> callback);

  // todo rewrite to rm?
  void rmdir(JSString name, JSObject options, JsFunctions.Consumer<JSObject> callback);

  static JsFunctions.Consumer<JSObject> callback(Runnable onComplete, Consumer<String> onError) {
    return error -> {
      if (error == null) {
        onComplete.run();
      } else {
        onError.accept(JsHelper.jsToString(error).stringValue());
      }
    };
  }
}
