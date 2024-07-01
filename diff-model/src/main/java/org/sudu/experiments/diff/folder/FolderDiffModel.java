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
  public boolean compared;
  public boolean isFile;
  public int propagation = NO_PROP;
  public int diffType = DiffTypes.DEFAULT;
  public int rangeId;
  public int depth;

  public FolderDiffModel(FolderDiffModel parent) {
    this.parent = parent;
    if (parent != null) depth = parent.depth + 1;
  }

  public void update(FolderDiffModel newModel) {
    this.children = newModel.children;
    this.childrenComparedCnt = newModel.childrenComparedCnt;
    this.compared = newModel.compared;
    this.propagation = newModel.propagation;
    this.diffType = newModel.diffType;
    this.rangeId = newModel.rangeId;
    this.isFile = newModel.isFile;
    if (compared && parent != null) parent.childCompared();
  }

  // returns true if parent is fully compared
  public boolean itemCompared() {
    this.compared = true;
    if (parent == null) throw new IllegalStateException("File must have a parent");
    return parent.childCompared();
  }

  public boolean childCompared() {
    childrenComparedCnt++;
    if (!isFullyCompared()) return false;
    compared = true;
    if (parent != null) parent.childCompared();
    return true;
  }

  public boolean isFullyCompared() {
    return children.length == childrenComparedCnt;
  }

  public FolderDiffModel child(int i) {
    return children[i];
  }

  public void markUp(int diffType, RangeCtx ctx) {
    propagation = PROP_UP;
    this.diffType = diffType;
    rangeId = ctx.nextId();
    if (parent != null) parent.markUp(diffType, ctx);
  }

  public void markDown(int diffType) {
    propagation = PROP_DOWN;
    this.diffType = diffType;
    this.compared = true;
    if (parent != null) rangeId = parent.rangeId;
    if (children != null) for (var child: children) child.markDown(diffType);
  }

  public static final FolderDiffModel DEFAULT = getDefault();

  private static FolderDiffModel getDefault() {
    var model = new FolderDiffModel(null);
    model.propagation = PROP_DOWN;
    model.diffType = DiffTypes.DEFAULT;
    model.compared = true;
    return model;
  }

  public static int[] toInts(FolderDiffModel model) {
    ArrayWriter writer = new ArrayWriter();
    writeInts(model, writer);
    return writer.getInts();
  }

  public static void writeInts(FolderDiffModel model, ArrayWriter writer) {
    writeInts(model, writer, new IdentityHashMap<>());
  }

  public static void writeInts(
      FolderDiffModel model, ArrayWriter writer,
      IdentityHashMap<FolderDiffModel, Integer> modelToIntMap
  ) {
    writer.write(model.propagation);
    writer.write(model.diffType);
    writer.write(model.rangeId);
    writer.write(model.childrenComparedCnt);
    writer.write(model.compared ? 1 : 0);
    writer.write(model.isFile ? 1 : 0);
    int ind = modelToIntMap.getOrDefault(model, -1);
    writer.write(ind);
    if (model.children == null) writer.write(-1);
    else {
      writer.write(model.children.length);
      for (var child: model.children) writeInts(child, writer, modelToIntMap);
    }
  }

  public static FolderDiffModel fromInts(int[] ints) {
    return fromInts(new ArrayReader(ints), null);
  }

  public static FolderDiffModel fromInts(ArrayReader reader, FolderDiffModel parent) {
    return fromInts(reader, parent, null);
  }

  public static FolderDiffModel fromInts(
      ArrayReader reader,
      FolderDiffModel parent,
      Pair<?, FolderDiffModel>[] models
  ) {
    FolderDiffModel model = new FolderDiffModel(parent);
    model.propagation = reader.next();
    model.diffType = reader.next();
    model.rangeId = reader.next();
    model.childrenComparedCnt = reader.next();
    model.compared = reader.next() == 1;
    model.isFile = reader.next() == 1;
    int ind = reader.next();
    if (ind != -1) models[ind].second = model;
    int childrenLen = reader.next();
    if (childrenLen != -1) {
      var children = new FolderDiffModel[childrenLen];
      for (int i = 0; i < childrenLen; i++) children[i] = fromInts(reader, model, models);
      model.children = children;
    }
    return model;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    FolderDiffModel that = (FolderDiffModel) o;
    return childrenComparedCnt == that.childrenComparedCnt
        && compared == that.compared
        && propagation == that.propagation
        && diffType == that.diffType
        && rangeId == that.rangeId
        && Objects.deepEquals(children, that.children);
  }

  @Override
  public int hashCode() {
    return Objects.hash(Arrays.hashCode(children), childrenComparedCnt, compared, propagation, diffType, rangeId);
  }

  public String infoString() {
    return "FolderDiffModel{" +
        "parent=" + parent +
        ", childrenComparedCnt=" + childrenComparedCnt +
        ", children.length=" + (children != null ? children.length : 0) +
        ", compared=" + compared +
        ", propagation=" + propagation +
        ", diffType=" + diffType +
        ", rangeId=" + rangeId +
        "}";
  }
}
