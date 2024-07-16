package org.sudu.experiments.diff.folder;

import org.sudu.experiments.arrays.ArrayReader;
import org.sudu.experiments.arrays.ArrayWriter;
import org.sudu.experiments.diff.DiffTypes;
import org.sudu.experiments.parser.common.Pair;

import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.Objects;

import static org.sudu.experiments.diff.folder.PropTypes.*;

public class FolderDiffModel {

  public FolderDiffModel parent;
  public FolderDiffModel[] children;
  public int childrenComparedCnt;
  int flags;
  // compared     0b...00000(0|1)
  // isFile       0b...0000(0|1)0
  // propagation  0b...00(00|01|10)00
  // diffType     0b...(00|01|10|11)0000
  public int rangeId;
  public int depth;

  public FolderDiffModel(FolderDiffModel parent) {
    this.parent = parent;
    if (parent != null) depth = parent.depth + 1;
  }

  public void update(FolderDiffModel newModel) {
    this.children = newModel.children;
    for (var child: children) child.parent = this;
    this.childrenComparedCnt = newModel.childrenComparedCnt;
    this.flags = newModel.flags;
    this.rangeId = newModel.rangeId;
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

  public void markUp(int diffType, RangeCtx ctx) {
    setPropagation(PROP_UP);
    setDiffType(diffType);
    rangeId = ctx.nextId();
    if (parent != null) parent.markUp(diffType, ctx);
  }

  public void markDown(int diffType) {
    setPropagation(PROP_DOWN);
    setDiffType(diffType);
    if (parent != null) rangeId = parent.rangeId;
    if (children != null) for (var child: children) child.markDown(diffType);
  }

  public boolean noChildren() {
    return children == null || children.length == 0;
  }

  public void setCompared(boolean compared) {
    int bit = compared ? 1 : 0;
    flags = flags & (~0b1) | bit;
  }

  public void setIsFile(boolean isFile) {
    int bit = isFile ? 1 : 0;
    flags = flags & (~(0b1 << 1)) | (bit << 1);
  }

  public void setPropagation(int propagation) {
    flags = flags & (~(0b11 << 2)) | (propagation << 2);
  }

  public void setDiffType(int diffType) {
    flags = flags & (~(0b11 << 4)) | (diffType << 4);
  }

  public boolean isCompared() {
    return (flags & 0b1) == 1;
  }

  public boolean isFile() {
    return ((flags >> 1) & 0b1) == 1;
  }

  public int getPropagation() {
    return (flags >> 2) & 0b11;
  }

  public int getDiffType() {
    return (flags >> 4) & 0b11;
  }

  public static final FolderDiffModel DEFAULT = getDefault();

  private static FolderDiffModel getDefault() {
    var model = new FolderDiffModel(null);
    model.setPropagation(PROP_DOWN);
    model.setDiffType(DiffTypes.DEFAULT);
    model.setCompared(true);
    return model;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    FolderDiffModel that = (FolderDiffModel) o;
    return childrenComparedCnt == that.childrenComparedCnt
        && flags == that.flags
        && rangeId == that.rangeId
        && Objects.deepEquals(children, that.children);
  }

  @Override
  public int hashCode() {
    return Objects.hash(Arrays.hashCode(children), childrenComparedCnt, flags, rangeId);
  }

  public String infoString() {
    return "FolderDiffModel{" +
        "parent=" + parent +
        ", childrenComparedCnt=" + childrenComparedCnt +
        ", children.length=" + (children != null ? children.length : 0) +
        ", compared=" + isCompared() +
        ", propagation=" + getPropagation() +
        ", diffType=" + getDiffType() +
        ", rangeId=" + rangeId +
        "}";
  }
}
