package org.sudu.experiments.protocol;

import org.sudu.experiments.arrays.ArrayReader;
import org.sudu.experiments.arrays.ArrayWriter;
import org.sudu.experiments.diff.folder.RemoteFolderDiffModel;
import org.sudu.experiments.js.JsArray;
import org.sudu.experiments.js.JsMemoryAccess;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;
import org.teavm.jso.typedarrays.Int32Array;

import java.util.ArrayList;
import java.util.List;

public class BackendMessage {

  public RemoteFolderDiffModel root;
  public String leftRootName;
  public String rightRootName;

  public static ArrayList<Object> serialize(
      RemoteFolderDiffModel root,
      String leftRootName,
      String rightRootName
  ) {
    ArrayList<Object> result = new ArrayList<>();
    List<String> paths = new ArrayList<>();

    ArrayWriter writer = new ArrayWriter();
    int pathLenPtr = writer.getPointer();
    writer.write(-1);

    RemoteFolderDiffModel.writeInts(root, paths, writer);

    writer.writeAtPos(pathLenPtr, paths.size());

    result.add(writer.getInts());
    result.add(leftRootName);
    result.add(rightRootName);
    result.addAll(paths);
    return result;
  }

  public static BackendMessage deserialize(JsArray<JSObject> jsArray) {
    Int32Array ints = jsArray.get(0).cast();
    ArrayReader reader = new ArrayReader(JsMemoryAccess.toJavaArray(ints));

    int pathLen = reader.next();
    String leftRootName = getString(jsArray, 1);
    String rightRootName = getString(jsArray, 2);

    String[] paths = new String[pathLen];
    for (int i = 0; i < pathLen; i++) paths[i] = getString(jsArray, i + 3);

    BackendMessage message = new BackendMessage();
    message.root = RemoteFolderDiffModel.fromInts(reader, paths, null);
    message.root.path = "";
    message.leftRootName = leftRootName;
    message.rightRootName = rightRootName;

    return message;
  }

  private static String getString(JsArray<JSObject> jsArray, int ind) {
    return ((JSString) jsArray.get(ind)).stringValue();
  }
}
