package org.sudu.experiments.protocol;

import org.sudu.experiments.arrays.ArrayReader;
import org.sudu.experiments.arrays.ArrayWriter;
import org.sudu.experiments.diff.folder.RemoteFolderDiffModel;
import org.sudu.experiments.js.JsArray;
import org.sudu.experiments.js.JsMemoryAccess;
import org.sudu.experiments.ui.fs.RemoteFileTreeNode;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;
import org.teavm.jso.typedarrays.Int32Array;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// Also known as FrontendViewState
public class FrontendMessage {

  public FrontendTreeNode openedFolders;
  public String searchQuery;

  public static JsArray<JSObject> serialize(
      RemoteFileTreeNode leftRoot,
      RemoteFileTreeNode rightRoot,
      RemoteFolderDiffModel root,
      String searchQuery
  ) {
    JsArray<JSObject> result = JsArray.create();
    List<String> paths = new ArrayList<>();

    ArrayWriter writer = new ArrayWriter();
    int pathLenInd = writer.getPointer();
    writer.write(-1);

    serialize(leftRoot, rightRoot, root, writer, paths);
    writer.writeAtPos(pathLenInd, paths.size());

    var ints = writer.getInts();
    result.set(0, JsMemoryAccess.bufferView(ints));;
    for (int i = 0; i < paths.size(); i++)
      result.set(1 + i, JSString.valueOf(paths.get(i)));

    result.set(1 + paths.size(), JSString.valueOf(searchQuery));
    return result;
  }

  private static void serialize(
      RemoteFileTreeNode leftNode,
      RemoteFileTreeNode rightNode,
      RemoteFolderDiffModel model,
      ArrayWriter writer,
      List<String> paths
  ) {
    var pathInd = paths.size();
    paths.add(model.path);
    writer.write(pathInd);

    if (leftNode.isClosed() && rightNode.isClosed())
      writer.write(-1);
    else {
      writer.write(model.children.length);
      int lP = 0, rP = 0;
      for (var child: model.children) {
        if (child.isBoth()) {
          serialize(leftNode.child(lP), rightNode.child(rP), (RemoteFolderDiffModel) child, writer, paths);
          lP++;
          rP++;
        } else if (child.isLeft()) {
          serialize(leftNode.child(lP), (RemoteFolderDiffModel) child, writer, paths);
          lP++;
        } else {
          serialize(rightNode.child(rP), (RemoteFolderDiffModel) child, writer, paths);
          rP++;
        }
      }
    }
  }

  private static void serialize(
      RemoteFileTreeNode node,
      RemoteFolderDiffModel model,
      ArrayWriter writer,
      List<String> paths
  ) {
    var pathInd = paths.size();
    paths.add(model.path);
    writer.write(pathInd);

    if (node.isClosed()) writer.write(-1);
    else {
      writer.write(model.children.length);
      for (int i = 0; i < model.children.length; i++)
        serialize(node.child(i), model.child(i), writer, paths);
    }
  }

  public static FrontendMessage deserialize(JsArray<JSObject> result) {
    Int32Array jsInts = result.get(0).cast();
    ArrayReader reader = new ArrayReader(JsMemoryAccess.toJavaArray(jsInts));
    int pathsLen = reader.next();

    String[] paths = new String[pathsLen];
    for (int i = 0; i < pathsLen; i++) {
      JSString jsString = result.get(1 + i).cast();
      paths[i] = jsString.stringValue();
    }

    FrontendMessage message = new FrontendMessage();
    message.openedFolders = deserialize(reader, paths);
    message.searchQuery = ((JSString) result.get(1 + pathsLen).cast()).stringValue();
    return message;
  }

  public static FrontendTreeNode deserialize(ArrayReader reader, String[] paths) {
    int ind = reader.next();
    int childLen = reader.next();

    FrontendTreeNode node = new FrontendTreeNode();
    node.name = paths[ind];
    if (childLen != -1) {
      node.children = new FrontendTreeNode[childLen];
      for (int i = 0; i < childLen; i++)
        node.children[i] = deserialize(reader, paths);
    }
    return node;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    FrontendMessage that = (FrontendMessage) o;
    return Objects.equals(openedFolders, that.openedFolders) && Objects.equals(searchQuery, that.searchQuery);
  }

  @Override
  public int hashCode() {
    return Objects.hash(openedFolders, searchQuery);
  }

  @Override
  public String toString() {
    return
        "{\"openedFolders\":" + openedFolders +
        ", \"searchQuery\":\"" + searchQuery + "\"}";
  }
}
