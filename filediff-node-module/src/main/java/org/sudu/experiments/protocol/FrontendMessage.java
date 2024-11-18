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

import java.util.*;

import static org.sudu.experiments.protocol.JsCast.ints;
import static org.sudu.experiments.protocol.JsCast.jsString;

// Also known as FrontendViewState
public class FrontendMessage {

  public FrontendTreeNode openedFolders;
  public String searchQuery;

  public static final FrontendMessage EMPTY = empty();

  public FrontendTreeNode findNode(int[] path) {
    return openedFolders.findNode(path);
  }

  public FrontendTreeNode findParentNode(int[] path) {
    if (path.length - 1 < 0) return null;
    return findNode(Arrays.copyOf(path, path.length - 1));
  }

  public FrontendTreeNode find(Deque<String> path) {
    return openedFolders.findNode(path);
  }

  public void collectPath(int[] path, ArrayWriter pathWriter, FolderDiffModel root, boolean left) {
    openedFolders.collectPath(path, pathWriter, root, left ? FolderDiffSide.LEFT : FolderDiffSide.RIGHT);
  }

  public static FrontendMessage mkFrontendMessage(
      RemoteFileTreeNode leftRoot,
      RemoteFileTreeNode rightRoot,
      RemoteFolderDiffModel root,
      String searchQuery
  ) {
    FrontendMessage message = new FrontendMessage();
    message.openedFolders = mkFrontendTreeNode(leftRoot, rightRoot, root);
    message.searchQuery = searchQuery;
    return message;
  }

  public static FrontendTreeNode mkFrontendTreeNode(
      RemoteFileTreeNode left,
      RemoteFileTreeNode right,
      RemoteFolderDiffModel model
  ) {
    FrontendTreeNode state = new FrontendTreeNode();
    state.name = model.path;
    state.isFile = model.isFile();

    if (model.children != null && (left.isOpened() || right.isOpened())) {
      state.children = new FrontendTreeNode[model.children.length];
      int lP = 0, rP = 0;
      for (int i = 0; i < state.children.length; i++) {
        var child = model.child(i);
        if (child.isBoth()) {
          state.children[i] = mkFrontendTreeNode(left.child(lP), right.child(rP), child);
          lP++;
          rP++;
        } else if (child.isLeft()) {
          state.children[i] = mkFrontendTreeNode(left.child(lP), child);
          lP++;
        } else {
          state.children[i] = mkFrontendTreeNode(right.child(rP), child);
          rP++;
        }
      }
    }
    return state;
  }

  public static FrontendTreeNode mkFrontendTreeNode(RemoteFileTreeNode node, RemoteFolderDiffModel model) {
    FrontendTreeNode state = new FrontendTreeNode();
    state.name = model.path;
    state.isFile = model.isFile();

    if (model.children != null && node.isOpened()) {
      state.children = new FrontendTreeNode[model.children.length];
      for (int i = 0; i < state.children.length; i++) {
        var child = model.child(i);
        state.children[i] = mkFrontendTreeNode(node.child(i), child);
      }
    }
    return state;
  }

  public static JsArray<JSObject> serialize(FrontendMessage message) {
    JsArray<JSObject> result = JsArray.create();
    List<String> paths = new ArrayList<>();

    ArrayWriter writer = new ArrayWriter();
    int pathLenInd = writer.getPointer();
    writer.write(-1);

    serialize(message.openedFolders, writer, paths);
    writer.writeAtPos(pathLenInd, paths.size());

    var ints = writer.getInts();
    result.set(0, JsMemoryAccess.bufferView(ints));
    for (int i = 0; i < paths.size(); i++)
      result.set(1 + i, jsString(paths.get(i)));

    result.set(1 + paths.size(), jsString(message.searchQuery));
    return result;
  }

  public static void serialize(
      FrontendTreeNode node,
      ArrayWriter writer,
      List<String> paths
  ) {
    var pathInd = paths.size();
    paths.add(node.name);
    writer.write(pathInd);
    writer.write(node.isFile ? 1 : 0);
    if (node.children == null) writer.write(-1);
    else {
      writer.write(node.children.length);
      for (var child: node.children) serialize(child, writer, paths);
    }
  }

  public static FrontendMessage deserialize(JsArray<JSObject> jsArray) {
    ArrayReader reader = new ArrayReader(ints(jsArray, 0));
    int pathsLen = reader.next();

    String[] paths = new String[pathsLen];
    for (int i = 0; i < pathsLen; i++)
      paths[i] = JsCast.string(jsArray, 1 + i);

    FrontendMessage message = new FrontendMessage();
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

  public static FrontendMessage empty() {
    var empty = new FrontendMessage();
    empty.openedFolders = new FrontendTreeNode();
    empty.openedFolders.name = "";
    empty.openedFolders.isFile = false;
    empty.searchQuery = "";
    return empty;
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
