package org.sudu.experiments.protocol;

import org.sudu.experiments.arrays.ArrayReader;
import org.sudu.experiments.arrays.ArrayWriter;
import org.sudu.experiments.diff.folder.FolderDiffModel;
import org.sudu.experiments.diff.folder.FolderDiffSide;
import org.sudu.experiments.diff.folder.RemoteFolderDiffModel;
import org.sudu.experiments.js.JsArray;
import org.sudu.experiments.js.JsMemoryAccess;
import org.sudu.experiments.ui.fs.RemoteFileTreeNode;
import org.teavm.jso.JSObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.sudu.experiments.protocol.JsCast.ints;
import static org.sudu.experiments.protocol.JsCast.jsString;

// Also known as FrontendViewState
public class FrontendMessage {

  public FrontendTreeNode openedFolders;
  public String searchQuery;
  public boolean filtered;

  public static final FrontendMessage EMPTY;

  static {
    EMPTY = new FrontendMessage();
    EMPTY.openedFolders = new FrontendTreeNode();
    EMPTY.openedFolders.name = "";
    EMPTY.openedFolders.isFile = false;
    EMPTY.searchQuery = "";
    EMPTY.filtered = false;
  }

  public FrontendTreeNode findNode(int[] path) {
    return openedFolders.findNode(path);
  }

  public FrontendTreeNode findParentNode(int[] path) {
    if (path.length - 1 < 0) return null;
    return findNode(Arrays.copyOf(path, path.length - 1));
  }

  public void collectPath(int[] path, ArrayWriter pathWriter, FolderDiffModel root, boolean left) {
    openedFolders.collectPath(path, pathWriter, root, left ? FolderDiffSide.LEFT : FolderDiffSide.RIGHT);
  }

  public static JsArray<JSObject> serialize(
      RemoteFileTreeNode leftRoot,
      RemoteFileTreeNode rightRoot,
      RemoteFolderDiffModel root,
      boolean filtered,
      String searchQuery
  ) {
    JsArray<JSObject> result = JsArray.create();
    List<String> paths = new ArrayList<>();

    ArrayWriter writer = new ArrayWriter();
    int pathLenInd = writer.getPointer();
    writer.write(-1);
    writer.write(filtered ? 1 : 0);

    serialize(leftRoot, rightRoot, root, writer, paths);
    writer.writeAtPos(pathLenInd, paths.size());

    var ints = writer.getInts();
    result.set(0, JsMemoryAccess.bufferView(ints));
    for (int i = 0; i < paths.size(); i++)
      result.set(1 + i, jsString(paths.get(i)));

    result.set(1 + paths.size(), jsString(searchQuery));
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
    writer.write(model.isFile() ? 1 : 0);

    if (leftNode.isClosed() || rightNode.isClosed() || model.children == null)
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
    writer.write(model.isFile() ? 1 : 0);

    if (node.isClosed() || model.children == null) writer.write(-1);
    else {
      writer.write(model.children.length);
      for (int i = 0; i < model.children.length; i++)
        serialize(node.child(i), model.child(i), writer, paths);
    }
  }

  public static FrontendMessage deserialize(JsArray<JSObject> jsArray) {
    ArrayReader reader = new ArrayReader(ints(jsArray, 0));
    int pathsLen = reader.next();

    String[] paths = new String[pathsLen];
    for (int i = 0; i < pathsLen; i++)
      paths[i] = JsCast.string(jsArray, 1 + i);

    FrontendMessage message = new FrontendMessage();
    message.filtered = reader.next() == 1;
    message.openedFolders = deserialize(reader, paths);
    message.searchQuery = JsCast.string(jsArray, 1 + pathsLen);
    return message;
  }

  public static FrontendTreeNode deserialize(ArrayReader reader, String[] paths) {
    int ind = reader.next();
    boolean isFile = reader.next() == 1;
    int childLen = reader.next();

    FrontendTreeNode node = new FrontendTreeNode();
    node.name = paths[ind];
    node.isFile = isFile;
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
