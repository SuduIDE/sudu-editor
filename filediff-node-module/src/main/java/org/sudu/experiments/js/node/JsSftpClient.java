package org.sudu.experiments.js.node;

import org.sudu.experiments.js.JsArrayReader;
import org.sudu.experiments.js.JsFunctions;
import org.sudu.experiments.js.JsFunctions.BiConsumer;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSError;
import org.teavm.jso.core.JSString;

// https://github.com/mscdex/ssh2/blob/master/SFTP.md
public interface JsSftpClient extends JSObject {

  // Stats === Attrs + helper methods
  interface Attrs extends JSObject {
    @JSProperty
    int getMode();
    @JSProperty
    int getSize();
    @JSProperty
    int getAtime();
    @JSProperty
    int getMtime();

    boolean isDirectory();
    boolean isFile();
    boolean isSymbolicLink();
  }

  interface DirEntry extends JSObject {
    @JSProperty
    JSString getFilename();
    @JSProperty
    Attrs getAttrs();
  }

  void readdir(
      JSString path,
      BiConsumer<JSError, JsArrayReader<DirEntry>> callback);

  // Retrieves attributes for path.
  void stat(JSString path, BiConsumer<JSError, Attrs> callback);

  // Opens a file filename with flags, flags is any of the OPEN_MODE constants
  void open(JSString path, int flags, BiConsumer<JSError, JSObject> cb);

  // Retrieves attributes for the resource associated with handle
  void fstat(JSObject handle, BiConsumer<JSError, Attrs> callback);

  //  Reads length bytes from the resource associated with handle
  //  starting at position and stores the bytes in buffer starting at offset
  void read(
      JSObject handle,
      JsBuffer buffer, int offset,
      int length, int position, ReadResult cb);

  // Writes length bytes from buffer starting at offset to the resource
  // associated with handle starting at position.
  void write(JSObject handle,
             JsBuffer buffer, int offset, int length,
             int position,  JsFunctions.Consumer<JSError> cb);

  // Closes the resource associated with handle given by open() or opendir().
  void close(JSObject handle, JsFunctions.Consumer<JSError> cb);

  @JSFunctor
  interface ReadResult extends JSObject {
    void f(JSError err, int bytesRead, JsBuffer buffer, int position);
  }

  interface ReadStream extends JSObject {}
  ReadStream createReadStream(JSString path, JSObject options);

  interface WriteStream extends JSObject {}
  WriteStream createWriteStream(JSString path, JSObject options);
}
