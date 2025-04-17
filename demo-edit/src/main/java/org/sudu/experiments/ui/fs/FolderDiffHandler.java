package org.sudu.experiments.ui.fs;

import org.sudu.experiments.DirectoryHandle;
import org.sudu.experiments.arrays.ArrayWriter;
import org.sudu.experiments.diff.DiffModel;
import org.sudu.experiments.diff.DiffTypes;
import org.sudu.experiments.diff.ItemKind;
import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.parser.common.Pair;

import java.util.*;
import java.util.function.Consumer;

public class FolderDiffHandler {

  DirectoryHandle left, right;
  TreeS[] leftChildren, rightChildren;
  Consumer<Object[]> r;

  public FolderDiffHandler(DirectoryHandle left, DirectoryHandle right, Consumer<Object[]> r) {
    this.left = left;
    this.right = right;
    this.r = r;
  }

  public void read() {
    left.read(new DiffReader(this::sendLeft, this::onError));
    right.read(new DiffReader(this::sendRight, this::onError));
  }

  private void sendLeft(TreeS[] leftChildren) {
    this.leftChildren = leftChildren;
    if (rightChildren != null) compare();
  }

  private void sendRight(TreeS[] rightChildren) {
    this.rightChildren = rightChildren;
    if (this.leftChildren != null) compare();
  }

  private void compare() {
    var result = DiffModel.countFolderCommon(leftChildren, rightChildren);
    int commonLen = result.first;
    int leftLen = leftChildren.length;
    int rightLen = rightChildren.length;
    var commons = result.second;

    BitSet leftCommon = commons[0], rightCommon = commons[1];
    int mergedLen = leftChildren.length + rightChildren.length - commonLen;
    TreeS[] merged = new TreeS[mergedLen];
    int[] kinds = new int[mergedLen];
    int mP = 0;
    int lP = 0, rP = 0;

    // item name  -> (folder, file)
    Map<String, Pair<Integer, Integer>> nameToIndices = new HashMap<>();

    while (lP < leftLen && rP < rightLen) {
      if (!leftCommon.get(lP)) {
        var child = leftChildren[lP++];
        merged[mP] = child;
        merged[mP].diffType = DiffTypes.DELETED;
        mark(nameToIndices, child, mP++);
      } else if (!rightCommon.get(rP)) {
        var child = rightChildren[rP++];
        merged[mP] = child;
        merged[mP].diffType = DiffTypes.INSERTED;
        mark(nameToIndices, child, mP++);
      } else {
        var child = leftChildren[lP];
        merged[mP] = child;
        kinds[mP++] = child.isFolder ? ItemKind.FOLDER : ItemKind.FILE;
        lP++; rP++;
      }
    }
    while (lP < leftLen) {
      var child = leftChildren[lP++];
      merged[mP] = child;
      merged[mP].diffType = DiffTypes.DELETED;
      mark(nameToIndices, child, mP++);
    }
    while (rP < rightLen) {
      var child = rightChildren[rP++];
      merged[mP] = child;
      merged[mP].diffType = DiffTypes.INSERTED;
      mark(nameToIndices, child, mP++);
    }

    for (var pair: nameToIndices.values()) {
      if (pair.first == -1) {
        int i = pair.second;
        kinds[i] = ItemKind.FILE;
      } else if (pair.second == -1) {
        int i = pair.first;
        kinds[i] = ItemKind.FOLDER;
      } else {
        int folderInd = pair.first;
        int fileInd = pair.second;
        boolean left = merged[fileInd].diffType == DiffTypes.DELETED;
        if (left) {
          kinds[folderInd] = ItemKind.LEFT_ONLY_FILE;
          kinds[fileInd] = ItemKind.LEFT_ONLY_FILE;
        } else {
          kinds[folderInd] = ItemKind.RIGHT_ONLY_FILE;
          kinds[fileInd] = ItemKind.RIGHT_ONLY_FILE;
        }
      }
    }
    onCompared(merged, kinds);
  }

  private void mark(Map<String, Pair<Integer, Integer>> map, TreeS item, int mP) {
    map.putIfAbsent(item.name, new Pair<>(-1, -1));
    if (item.isFolder) map.get(item.name).first = mP;
    else map.get(item.name).second = mP;
  }

  private void onError(String error) {
    this.leftChildren = this.rightChildren = null;
    System.err.println(error);
    ArrayList<Object> result = new ArrayList<>();
    result.add(new int[]{-1});
    result.add(error);
    ArrayOp.sendArrayList(result, r);
  }

  protected void onCompared(TreeS[] merged, int[] kinds) {
    ArrayWriter writer = new ArrayWriter();

    // Write diff types
    writer.write(merged.length);
    writer.write(leftChildren.length);
    writer.write(rightChildren.length);
    for (var child: merged) writer.write(child.diffType);
    writer.write(kinds);

    // Write fs items
    var result = new ArrayList<>();
    result.add(writer.getInts());
    for (var child: leftChildren) result.add(child.item);
    for (var child: rightChildren) result.add(child.item);
    ArrayOp.sendArrayList(result, r);
  }
}