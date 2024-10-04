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
  // compared     0b...0000000x   x  = (0|1)
  // isFile       0b...000000x0   x  = (0|1)
  // propagation  0b...0000xx00   xx = (00|01|10)
  // diffType     0b...00xx0000   xx = (00|01|10|11)
  // itemKind     0b...xx000000   xx = (00|01|10|11)
  public int posInParent = -1;

  public FolderDiffModel(FolderDiffModel parent) {
    this.parent = parent;
  }

  public void update(FolderDiffModel newModel) {
    this.children = newModel.children;
    for (var child: children) child.parent = this;
    this.childrenComparedCnt = newModel.childrenComparedCnt;
    this.flags = newModel.flags;
    this.posInParent = newModel.posInParent;
    if (this.isCompared() && parent != null) parent.childCompared();
  }

  // returns true if parent is fully compared
  public boolean itemCompared() {
    if (isCompared()) System.err.println("File is already compared");
    setCompared(true);
    return parent == null || parent.childCompared();
  }

  public boolean childCompared() {
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

  public void markUp(int diffType) {
    setPropagation(PROP_UP);
    setDiffType(diffType);
    if (parent != null) parent.markUp(diffType);
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

//  public void setIsFile(boolean isFile) {
//    int bit = isFile ? 1 : 0;
//    flags = flags & (~(0b1 << 1)) | (bit << 1);
//  }

  public void setPropagation(int propagation) {
    flags = flags & (~(0b11 << 2)) | (propagation << 2);
  }

  public void setDiffType(int diffType) {
    flags = flags & (~(0b11 << 4)) | (diffType << 4);
  }

  public void setItemKind(int itemKind) {
    flags = flags & (~(0b11 << 6)) | (itemKind << 6);
  }

  public boolean isCompared() {
    return (flags & 0b1) == 1;
  }

  public boolean isFile() {
    int itemKind = getItemKind();
    return itemKind == ItemKind.FILE
        || (isLeft() && itemKind == ItemKind.LEFT_ONLY_FILE)
        || (isRight() && itemKind == ItemKind.RIGHT_ONLY_FILE);
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

  public int nextInd(int ind, int filter) {
    switch (filter) {
      case ModelFilter.NO_FILTER -> {
        return ind;
      }
      case ModelFilter.LEFT -> {
        while (ind < children.length) {
          if (child(ind).isLeft()) return ind;
          ind++;
        }
      }
      case ModelFilter.RIGHT -> {
        while (ind < children.length) {
          if (child(ind).isRight()) return ind;
          ind++;
        }
      }
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

  public boolean isRight() {
    return getDiffType() != DiffTypes.DELETED;
  }

  public boolean matchFilter(int filter) {
    return switch (filter) {
      case ModelFilter.LEFT -> isLeft();
      case ModelFilter.RIGHT -> isRight();
      default -> true;
    };
  }

  // todo change parent status after diff applying
  public void deleteItem() {
    if (parent == null) throw new RuntimeException("Parent can't be null");
    var newChildren = new FolderDiffModel[parent.children.length - 1];
    for (int i = 0, j = 0; i < parent.children.length; i++) {
      var child = parent.child(i);
      if (this == child) continue;
      newChildren[j++] = child;
    }
    parent.childrenComparedCnt--;
    parent.children = newChildren;
    parent.updateItem();
  }

  // todo change parent status after diff applying
  public void insertItem() {
    if (isFile()) setDiffType(DiffTypes.DEFAULT);
    else if (children != null) {
      for (var child: children) child.insertItem();
    }
    updateItem();
  }

  // todo change parent status after diff applying
  public void editItem(boolean left) {
    int diffType = getDiffType();
    if (diffType == DiffTypes.DEFAULT) return;
    if (diffType == DiffTypes.EDITED) {
      if (isFile()) {
        setDiffType(DiffTypes.DEFAULT);
        updateItem();
      } else if (children != null) {
        for (var child: children) child.editItem(left);
      }
    } else {
      if ((left && diffType == DiffTypes.DELETED) || (!left && diffType == DiffTypes.INSERTED)) this.insertItem();
//    if ((left && diffType == DiffTypes.INSERTED) || (!left && diffType == DiffTypes.DELETED)) this.deleteItem();
    }
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

  public FolderDiffModel findNode(int[] path) {
    return findNode(path, 0);
  }

  private FolderDiffModel findNode(int[] path, int ind) {
    if (ind == path.length) return this;
    if (children == null || path[ind] >= children.length) return null;
    return children[path[ind]].findNode(path, ind + 1);
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
        "parent=" + parent +
        ", childrenComparedCnt=" + childrenComparedCnt +
        ", children.length=" + (children != null ? children.length : 0) +
        ", compared=" + isCompared() +
        ", propagation=" + getPropagation() +
        ", diffType=" + getDiffType() +
        "}";
  }

  public String value() {
    return DiffTypes.name(getDiffType());
  }
}
