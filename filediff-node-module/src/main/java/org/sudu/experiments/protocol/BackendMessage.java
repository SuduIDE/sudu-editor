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
  public String leftRootPath;
  public String rightRootName;
  public String rightRootPath;
  public int foldersCmp, filesCmp, timeDelta;
  public int differentFiles;
  public boolean rootReplaced;

  public static JsArray<JSObject> serialize(
      RemoteFolderDiffModel root,
      FrontendMessage message,  // message == null <=> write full model
      String leftRootPath,
      String leftRootName,
      String rightRootPath,
      String rightRootName,
      int foldersCmp,
      int filesCmp,
      int timeDelta,
      int differentFiles,
      boolean rootReplaced
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
    result.set(1, jsString(leftRootPath));
    result.set(2, jsString(leftRootName));
    result.set(3, jsString(rightRootPath));
    result.set(4, jsString(rightRootName));
    result.set(5, jsInts(timeDelta, foldersCmp, filesCmp, differentFiles, rootReplaced ? 1 : 0));
    for (int i = 0; i < paths.size(); i++)
      result.set(6 + i, jsString(paths.get(i)));
    return result;
  }

  public static JsArray<JSObject> serializeFullModel(
      RemoteFolderDiffModel root,
      String leftRootPath,
      String leftRootName,
      String rightRootPath,
      String rightRootName
  ) {
    return serialize(
        root, null,
        leftRootPath, leftRootName,
        rightRootPath, rightRootName,
        0, 0, 0, 0, false
    );
  }

  public static BackendMessage deserialize(JsArray<JSObject> jsArray) {
    ArrayReader reader = new ArrayReader(ints(jsArray, 0));

    int pathLen = reader.next();
    String leftRootPath = string(jsArray, 1);
    String leftRootName = string(jsArray, 2);
    String rightRootPath = string(jsArray, 3);
    String rightRootName = string(jsArray, 4);
    int[] stats = ints(jsArray, 5);
    int time = stats[0],
        foldersCmp = stats[1],
        filesCmp = stats[2],
        differentFiles = stats[3];
    boolean rootReplaced = stats[4] == 1;

    String[] paths = new String[pathLen];
    for (int i = 0; i < pathLen; i++) paths[i] = string(jsArray, i + 6);

    BackendMessage message = new BackendMessage();
    message.root = RemoteFolderDiffModel.fromInts(reader, paths, null);
    message.root.path = "";
    message.leftRootPath = leftRootPath;
    message.leftRootName = leftRootName;
    message.rightRootPath = rightRootPath;
    message.rightRootName = rightRootName;
    message.timeDelta = time;
    message.foldersCmp = foldersCmp;
    message.filesCmp = filesCmp;
    message.differentFiles = differentFiles;
    message.rootReplaced = rootReplaced;
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
