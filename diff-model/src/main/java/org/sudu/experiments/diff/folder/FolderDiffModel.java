package org.sudu.experiments.diff.folder;

import org.sudu.experiments.arrays.ArrayWriter;
import org.sudu.experiments.diff.DiffTypes;
import org.sudu.experiments.diff.ItemKind;

import java.util.Arrays;
import java.util.Objects;

import static org.sudu.experiments.diff.folder.PropTypes.*;

public class FolderDiffModel {

  public FolderDiffModel parent;
  public FolderDiffModel[] children;
  public int childrenComparedCnt;
  public int flags;
  // compared         0b...0000000000x   x  = (0|1)
  // isFile           0b...000000000x0   x  = (0|1)
  // propagation      0b...0000000xx00   xx = (00|01|10)
  // diffType         0b...00000xx0000   xx = (00|01|10|11)
  // itemKind         0b...000xx000000   xx = (00|01|10|11)
  // excluded         0b...00x00000000   x  = (0|1)
  // sendExcluded     0b...0x000000000   x  = (0|1)
  // containsExcluded 0b...x0000000000   x  = (0|1)
  public int posInParent = -1;

  public FolderDiffModel(FolderDiffModel parent) {
    this.parent = parent;
  }

  public void update(FolderDiffModel newModel) {
    this.children = newModel.children;
    if (children != null) for (var child: children) child.parent = this;
    this.childrenComparedCnt = newModel.childrenComparedCnt;
    this.flags = newModel.flags;
    this.posInParent = newModel.posInParent;
    if (this.isCompared() && parent != null) parent.childCompared();
  }

  // returns true if parent is fully compared
  public boolean itemCompared() {
    if (isCompared()) {
      if (!isExcluded()) System.err.println("File is already compared");
      else return false;
    }
    setCompared(true);
    return parent == null || parent.childCompared();
  }

  public boolean childCompared() {
    if (isCompared()) return false;
    childrenComparedCnt++;
    if (!isFullyCompared()) return false;
    setCompared(true);
    if (parent != null) parent.childCompared();
    return true;
  }

  public boolean isFullyCompared() {
    if (childrenComparedCnt > children.length)
      System.err.println("childrenComparedCnt cannot be greater than children.length");
    return children.length == childrenComparedCnt;
  }

  public FolderDiffModel child(int i) {
    return children[i];
  }

  public void markUpContainExcluded() {
    if (containExcluded()) return;
    setContainExcluded(true);
    if (parent != null) parent.markUpContainExcluded();
  }

  public void updateContainExcluded() {
    if (isExcluded() || (isFile() && containExcluded())) return;
    boolean containExcluded = false;
    for (var child: children) {
      containExcluded = child.containExcluded() || child.isExcluded();
      if (containExcluded) break;
    }
    if (containExcluded) return;
    setContainExcluded(false);
    if (parent != null) parent.updateContainExcluded();
  }

  public void markUpDiffType(int diffType) {
    setPropagation(PROP_UP);
    setDiffType(diffType);
    if (parent != null) parent.markUpDiffType(diffType);
  }

  public void markDown(int diffType) {
    setPropagation(PROP_DOWN);
    setDiffType(diffType);
    if (children != null) for (var child: children) child.markDown(diffType);
  }

  public void setCompared(boolean compared) {
    int bit = compared ? 1 : 0;
    flags = flags & (~0b1) | bit;
  }

  public void setPropagation(int propagation) {
    flags = flags & (~(0b11 << 2)) | (propagation << 2);
  }

  public void setDiffType(int diffType) {
    flags = flags & (~(0b11 << 4)) | (diffType << 4);
  }

  public void setItemKind(int itemKind) {
    flags = flags & (~(0b11 << 6)) | (itemKind << 6);
  }

  public void setExcluded(boolean excluded) {
    int bit = excluded ? 1 : 0;
    flags = flags & (~(0b1 << 8)) | (bit << 8);
  }

  public void setSendExcluded(boolean sendExcluded) {
    int bit = sendExcluded ? 1 : 0;
    flags = flags & (~(0b1 << 9)) | (bit << 9);
  }

  public void setContainExcluded(boolean containExcluded) {
    int bit = containExcluded ? 1 : 0;
    flags = flags & (~(0b1 << 10)) | (bit << 10);
  }

  public boolean isCompared() {
    return (flags & 0b1) == 1;
  }

  public boolean isFile() {
    int itemKind = getItemKind();
    return itemKind == ItemKind.FILE
        || (isLeftOnly() && itemKind == ItemKind.LEFT_ONLY_FILE)
        || (isRightOnly() && itemKind == ItemKind.RIGHT_ONLY_FILE);
  }

  public boolean isDir() {
    return !isFile();
  }

  public int getPropagation() {
    return (flags >> 2) & 0b11;
  }

  public int getDiffType() {
    return (flags >> 4) & 0b11;
  }

  public int getItemKind() {
    return (flags >> 6) & 0b11;
  }

  public boolean isExcluded() {
    return ((flags >> 8) & 0b1) == 1;
  }

  public boolean isSendExcluded() {
    return ((flags >> 9) & 0b1) == 1;
  }

  public boolean containExcluded() {
    return ((flags >> 10) & 0b1) == 1;
  }

  public int nextInd(int ind, int side) {
    switch (side) {
      case FolderDiffSide.BOTH -> {
        return ind;
      }
      case FolderDiffSide.LEFT -> {
        while (ind < children.length) {
          if (child(ind).isLeft()) return ind;
          ind++;
        }
      }
      case FolderDiffSide.RIGHT -> {
        while (ind < children.length) {
          if (child(ind).isRight()) return ind;
          ind++;
        }
      }
    }
    return -1;
  }

  public int filteredInd(int fullInd) {
    if (children == null) return -1;
    for (int i = 0; i < children.length; i++) {
      if (child(i).posInParent == fullInd) return i;
    }
    return -1;
  }

  public boolean isBoth() {
    int diffType = getDiffType();
    return diffType == DiffTypes.DEFAULT || diffType == DiffTypes.EDITED;
  }

  public boolean isLeft() {
    return getDiffType() != DiffTypes.INSERTED;
  }

  public boolean isLeftOnly() {
    return getDiffType() == DiffTypes.DELETED;
  }

  public boolean isRight() {
    return getDiffType() != DiffTypes.DELETED;
  }

  public boolean isRightOnly() {
    return getDiffType() == DiffTypes.INSERTED;
  }

  public boolean matchSide(boolean left) {
    return matchSide(left ? FolderDiffSide.LEFT : FolderDiffSide.RIGHT);
  }

  public boolean matchSide(int side) {
    return switch (side) {
      case FolderDiffSide.LEFT -> isLeft();
      case FolderDiffSide.RIGHT -> isRight();
      default -> true;
    };
  }

  public void deleteItem() {
    if (parent == null) throw new RuntimeException("Parent can't be null");
    var newChildren = new FolderDiffModel[parent.children.length - 1];
    for (int i = 0, j = 0; i < parent.children.length; i++) {
      var child = parent.child(i);
      if (this == child) continue;
      child.posInParent = j;
      newChildren[j++] = child;
    }
    parent.childrenComparedCnt--;
    parent.children = newChildren;
    parent.updateItemOnDelete();
  }

  // todo change parent status after diff applying
  public void insertItem() {
    if (isFile()) setDiffType(DiffTypes.DEFAULT);
    else if (children != null) {
      for (var child: children) child.insertItem();
    }
    updateItem();
  }

  public void updateItem() {
    boolean haveChanges = false;
    if (children != null) {
      for (var child: children) {
        if (child.getDiffType() != DiffTypes.DEFAULT) {
          haveChanges = true;
          break;
        }
      }
    } else haveChanges = getDiffType() != DiffTypes.DEFAULT;
    setDiffType(haveChanges ? DiffTypes.EDITED : DiffTypes.DEFAULT);
    if (parent != null) parent.updateItem();
  }

  public void updateItemOnDelete() {
    if (children == null) return;
    boolean deletedOnly = true;
    boolean insertedOnly = true;
    boolean haveChanges = false;
    for (var child: children) {
      int diffType = child.getDiffType();
      deletedOnly &= diffType == DiffTypes.DELETED;
      insertedOnly &= diffType == DiffTypes.INSERTED;
      haveChanges |= diffType != DiffTypes.DEFAULT;
    }
    if (children.length != 0 && (deletedOnly || insertedOnly)) return;
    setDiffType(haveChanges ? DiffTypes.EDITED : DiffTypes.DEFAULT);
    if (parent != null) parent.updateItem();
  }

  public boolean shouldSync() {
    return getDiffType() != DiffTypes.DEFAULT
        && isCompared();
  }

  public FolderDiffModel findNodeByIndPath(int[] path) {
    return findNodeByIndPath(path, 0);
  }

  private FolderDiffModel findNodeByIndPath(int[] path, int ind) {
    if (ind == path.length) return this;
    if (children == null || path[ind] >= children.length) return null;
    return children[path[ind]].findNodeByIndPath(path, ind + 1);
  }

  public FolderDiffModel[] getModelsByPath(int[] path) {
    FolderDiffModel[] result = new FolderDiffModel[path.length];
    collectModelsByPath(path, 0, result);
    return result;
  }

  private void collectModelsByPath(int[] path, int ind, FolderDiffModel[] collected) {
    collected[ind] = this;
    if (ind == path.length - 1 || children == null) return;
    children[path[ind]].collectModelsByPath(path, ind + 1, collected);
  }
  public static final FolderDiffModel DEFAULT = getDefault();

  private static FolderDiffModel getDefault() {
    var model = new FolderDiffModel(null);
    model.setPropagation(PROP_DOWN);
    model.setDiffType(DiffTypes.DEFAULT);
    model.setCompared(true);
    return model;
  }

  public int[] getPathFromRoot() {
    var writer = new ArrayWriter();
    writePathFromRoot(writer);
    return writer.getInts();
  }

  private void writePathFromRoot(ArrayWriter writer) {
    if (parent != null) {
      parent.writePathFromRoot(writer);
      writer.write(posInParent);
    }
  }

  public int[] filteredPath(int[] path) {
    int[] filtered = new int[path.length];
    collectFilteredPath(filtered, path, 0);
    return filtered;
  }

  private void collectFilteredPath(int[] filtered, int[] path, int ind) {
    if (ind == path.length) return;
    filtered[ind] = filteredInd(path[ind]);
    child(filtered[ind]).collectFilteredPath(filtered, path, ind + 1);
  }

  public FolderDiffModel findPrevFileDiff(int index) {
    if (children == null) return null;
    if (index == -1) index = children.length;
    for (int i = index - 1; i >= 0; i--) {
      FolderDiffModel childModel = child(i);
      int diffType = childModel.getDiffType();
      boolean matchSide = diffType != DiffTypes.DEFAULT;
      if (!childModel.isFile() && matchSide) {
        var childResult = childModel.findPrevFileDiff(-1);
        if (childResult != null) return childResult;
        continue;
      }
      if (!matchSide || childModel.isDir()) continue;
      return childModel;
    }
    return null;
  }

  public FolderDiffModel findNextFileDiff(int index) {
    if (children == null) return null;
    for (int i = index + 1; i < children.length; i++) {
      FolderDiffModel childModel = child(i);
      int diffType = childModel.getDiffType();
      boolean matchSide = diffType != DiffTypes.DEFAULT;
      if (!childModel.isFile() && matchSide) {
        var childResult = childModel.findNextFileDiff(-1);
        if (childResult != null) return childResult;
        else continue;
      }
      if (!matchSide || childModel.isDir()) continue;
      return childModel;
    }
    return null;
  }

  public boolean canNavigateUp(int index) {
    if (children == null) return false;
    if (index == -1) index = children.length;
    for (int i = index - 1; i >= 0; i--) {
      FolderDiffModel childModel = child(i);
      int diffType = childModel.getDiffType();
      boolean matchSide = diffType != DiffTypes.DEFAULT;
      if (!childModel.isFile() && matchSide) return true;
      if (!matchSide || childModel.isDir()) continue;
      return true;
    }
    return false;
  }

  public boolean canNavigateDown(int index) {
    if (children == null) return false;
    for (int i = index + 1; i < children.length; i++) {
      FolderDiffModel childModel = child(i);
      int diffType = childModel.getDiffType();
      boolean matchSide = diffType != DiffTypes.DEFAULT;
      if (!childModel.isFile() && matchSide) return true;
      if (!matchSide || childModel.isDir()) continue;
      return true;
    }
    return false;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    FolderDiffModel that = (FolderDiffModel) o;
    return childrenComparedCnt == that.childrenComparedCnt
        && flags == that.flags
        && Objects.deepEquals(children, that.children);
  }

  @Override
  public int hashCode() {
    return Objects.hash(Arrays.hashCode(children), childrenComparedCnt, flags);
  }

  public String infoString() {
    return "FolderDiffModel{" +
        "childrenComparedCnt=" + childrenComparedCnt +
        ", children.length=" + (children != null ? children.length : 0) +
        ", compared=" + isCompared() +
        ", propagation=" + getPropagation() +
        ", diffType=" + DiffTypes.name(getDiffType()) +
        ", itemKind=" + ItemKind.name(getDiffType()) +
        ", exclude=" + isExcluded() +
        ", sendExcluded=" + isSendExcluded() +
        "}";
  }

  public String value() {
    return DiffTypes.name(getDiffType());
  }
}
