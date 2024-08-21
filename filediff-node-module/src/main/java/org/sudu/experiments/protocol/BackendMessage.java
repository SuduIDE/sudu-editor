package org.sudu.experiments.protocol;

import org.sudu.experiments.arrays.ArrayReader;
import org.sudu.experiments.arrays.ArrayWriter;
import org.sudu.experiments.diff.folder.RemoteFolderDiffModel;
import org.sudu.experiments.js.JsArray;
import org.teavm.jso.JSObject;

import java.util.ArrayList;
import java.util.List;

import static org.sudu.experiments.protocol.JsCast.*;

public class BackendMessage {

  public RemoteFolderDiffModel root;
  public String leftRootName;
  public String rightRootName;

  public static JsArray<JSObject> serialize(
      RemoteFolderDiffModel root,
      FrontendMessage message,  // message == null <=> write full model
      String leftRootName,
      String rightRootName
  ) {
    JsArray<JSObject> result = JsArray.create();
    List<String> paths = new ArrayList<>();

    ArrayWriter writer = new ArrayWriter();
    int pathLenPtr = writer.getPointer();
    writer.write(-1);

    if (message == null)
      RemoteFolderDiffModel.writeInts(root, paths, writer);
    else
      RemoteModelWriter.writeInts(root, paths, message.openedFolders, writer);

    writer.writeAtPos(pathLenPtr, paths.size());

    result.set(0, jsInts(writer.getInts()));
    result.set(1, jsString(leftRootName));
    result.set(2, jsString(rightRootName));
    for (int i = 0; i < paths.size(); i++)
      result.set(3 + i, jsString(paths.get(i)));
    return result;
  }

  public static JsArray<JSObject> serializeFullModel(
      RemoteFolderDiffModel root,
      String leftRootName,
      String rightRootName
  ) {
    return serialize(root, null, leftRootName, rightRootName);
  }

  public static BackendMessage deserialize(JsArray<JSObject> jsArray) {
    ArrayReader reader = new ArrayReader(ints(jsArray, 0));

    int pathLen = reader.next();
    String leftRootName = string(jsArray, 1);
    String rightRootName = string(jsArray, 2);

    String[] paths = new String[pathLen];
    for (int i = 0; i < pathLen; i++) paths[i] = string(jsArray, i + 3);

    BackendMessage message = new BackendMessage();
    message.root = RemoteFolderDiffModel.fromInts(reader, paths, null);
    message.root.path = "";
    message.leftRootName = leftRootName;
    message.rightRootName = rightRootName;

    return message;
  }

  @Override
  public String toString() {
    return "{" +
        "\"root\":" + root +
        ", \"leftRootName\":\"" + leftRootName + '\"' +
        ", \"rightRootName\":\"" + rightRootName + '\"' +
        '}';
  }
}