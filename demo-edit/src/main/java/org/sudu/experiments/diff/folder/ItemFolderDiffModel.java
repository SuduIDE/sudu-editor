package org.sudu.experiments.diff.folder;

import org.sudu.experiments.DirectoryHandle;
import org.sudu.experiments.FileHandle;
import org.sudu.experiments.FsItem;
import org.sudu.experiments.arrays.ArrayReader;
import org.sudu.experiments.arrays.ArrayWriter;
import org.sudu.experiments.diff.DiffTypes;
import org.sudu.experiments.editor.worker.FsWorkerJobs;
import org.sudu.experiments.worker.WorkerJobExecutor;

import java.util.Deque;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;

public class ItemFolderDiffModel extends RemoteFolderDiffModel {

  public final FsItem[] items = new FsItem[] {null, null};

  private static final boolean DEBUG = true;

  public ItemFolderDiffModel(FolderDiffModel parent, String path) {
    super(parent, path);
  }

  @Override
  public ItemFolderDiffModel child(int i) {
    return (ItemFolderDiffModel) super.child(i);
  }

  @Override
  public ItemFolderDiffModel parent() {
    return (ItemFolderDiffModel) parent;
  }

  public ItemFolderDiffModel findNeedUpdate(Deque<ItemFolderDiffModel> paths) {
    if (parent == null) return this;
    int diffType = getDiffType();
    if (this.isFile() || (diffType == DiffTypes.DEFAULT || diffType == DiffTypes.EDITED) && countItems() < 2) {
      paths.addFirst(this);
      return parent().findNeedUpdate(paths);
    }
    return this;
  }

  public void setItems(FsItem left, FsItem right) {
    if (!path.isEmpty() && (!path.equals(left.getName()) || !path.equals(right.getName())))
      System.err.println("Set items: " + left.getName() + " & " + right.getName() + " to " + path);
    items[0] = left;
    items[1] = right;
  }

  public void setItem(FsItem item) {
    if (!path.isEmpty() && !path.equals(item.getName()))
      System.err.println("Set item: " + item.getName() + " to " + path);
    if (isLeftOnly()) items[0] = item;
    else if (isRightOnly()) items[1] = item;
  }

  public void setItem(boolean left, FsItem item) {
    if (!path.isEmpty() && !path.equals(item.getName()))
      System.err.println("Set item: " + item.getName() + " to " + path);
    items[left ? 0 : 1] = item;
  }

  public FsItem item() {
    if (isLeftOnly()) return items[0];
    if (isRightOnly()) return items[1];
    return null;
  }

  public FsItem item(boolean left) {
    return left ? left() : right();
  }

  public FsItem left() {
    return items[0];
  }

  public FsItem right() {
    return items[1];
  }

  private int countItems() {
    return (left() == null ? 0 : 1) + (right() == null ? 0 : 1);
  }

  public static int[] toInts(
      ItemFolderDiffModel model,
      List<String> pathList,
      List<FsItem> fsList
  ) {
    ArrayWriter writer = new ArrayWriter();
    writeInts(model, pathList, fsList, writer);
    return writer.getInts();
  }

  public static void writeInts(
      ItemFolderDiffModel model,
      List<String> pathList,
      List<FsItem> fsList,
      ArrayWriter writer
  ) {
    writer.write(model.flags);
    writer.write(model.childrenComparedCnt);
    writer.write(model.posInParent);

    writer.write(pathList.size());
    pathList.add(model.path);

    for (var fsItem: model.items) {
      if (fsItem != null) {
        writer.write(fsList.size());
        fsList.add(fsItem);
      } else writer.write(-1);
    }

    if (model.children == null) writer.write(-1);
    else {
      writer.write(model.children.length);
      for (var child : model.children) writeInts((ItemFolderDiffModel) child, pathList, fsList, writer);
    }
  }

  public static ItemFolderDiffModel fromInts(int[] ints, String[] paths, FsItem[] items) {
    return fromInts(new ArrayReader(ints), paths, items, null);
  }

  public static ItemFolderDiffModel fromInts(
      ArrayReader reader,
      String[] paths,
      FsItem[] items,
      ItemFolderDiffModel parent
  ) {
    ItemFolderDiffModel model = new ItemFolderDiffModel(parent, null);
    model.flags = reader.next();
    model.childrenComparedCnt = reader.next();
    model.posInParent = reader.next();

    int pathInd = reader.next();
    model.path = paths[pathInd];

    for (int i = 0; i < 2; i++) {
      int itemInd = reader.next();
      if (itemInd == -1) continue;
      model.items[i] = items[itemInd];
    }

    int childrenLen = reader.next();
    if (childrenLen != -1) {
      var children = new ItemFolderDiffModel[childrenLen];
      for (int i = 0; i < childrenLen; i++) {
        children[i] = fromInts(reader, paths, items, model);
      }
      model.children = children;
    }
    return model;
  }

  public void updateFsItems(
      boolean left,
      ArrayReader reader,
      Deque<FsItem> items,
      Deque<ItemFolderDiffModel> paths
  ) {
    if (isFile()) return;
    if (items.isEmpty()) return;
    int len = reader.next();
    var item = items.removeFirst();
    if (len == -1) {
      var child = paths.removeFirst();
      child.setItem(left, item);
      child.updateFsItems(left, reader, items, paths);
    } else {
      int side = left ? FolderDiffSide.LEFT : FolderDiffSide.RIGHT;
      int mP = 0;
      for (int i = 0; i < len; i++) {
        mP = nextInd(mP, side);
        var child = child(mP);
        child.setItem(left, item);
        child.updateFsItems(left, reader, items, paths);
        mP++;
      }
    }
  }

  public void getOrCreateDir(
      boolean left,
      WorkerJobExecutor executor,
      Consumer<DirectoryHandle> onComplete,
      Consumer<String> onError
  ) {
    if (item(left) != null) {
      if (item(left) instanceof DirectoryHandle dir) onComplete.accept(dir);
      else onError.accept("Not a directory " + item(left));
    } else {
      Consumer<DirectoryHandle> onParentDirGet = parentDir -> {
        // Can't LoggingJs from here
        if (DEBUG) {
          System.out.println("ItemFolderDiffModel.getOrCreateDir: created dir: " + parentDir.getName());
        }
        parent().setItem(left, parentDir);
        FsWorkerJobs.mkDir(executor, parentDir, path, onComplete, onError);
      };
      parent().getOrCreateDir(left, executor, onParentDirGet, onError);
    }
  }

  public void remove(ModelCopyDeleteStatus status) {
    status.inTraverse++;
    if (getDiffType() != DiffTypes.DELETED && getDiffType() != DiffTypes.INSERTED) {
      status.onCopyError("Can't delete: item " + path + " is not marked as deleted or inserted");
      status.onTraversed();
      return;
    }

    FsItem item = item();
    if (item instanceof FileHandle file) {
      status.inWork++;
      FsWorkerJobs.removeFile(
          status.executor, file,
          () -> status.onFileDeleted(this),
          (err) -> status.onFileDeleteError(this, err)
      );
    } else if (item instanceof DirectoryHandle) {
      status.markForDelete(this);
      if (children == null || children.length == 0) {
        removeEmptyFolder(status);
      } else {
        for (int i = 0; i < children.length; i++) child(i).remove(status);
      }
    }
    status.onTraversed();
  }

  public void removeEmptyFolder(ModelCopyDeleteStatus status) {
    FsWorkerJobs.removeDir(
        status.executor, (DirectoryHandle) item(),
        () -> status.onDirDeleted(this),
        (err) -> status.onFolderDeleteError(this, err)
    );
  }

  public void copy(boolean left, boolean removeItems, ModelCopyDeleteStatus status) {
    status.inTraverse++;
    if (getDiffType() == DiffTypes.DEFAULT) {
      status.onTraversed();
      return;
    }
    if (isFile()) copyFile(left, removeItems, status);
    else copyFolder(left, removeItems, status);
    status.onTraversed();
  }

  private void copyFolder(boolean left, boolean removeItems, ModelCopyDeleteStatus status) {
    if (removeItems &&
        (left && isRightOnly()) ||
        (!left && isLeftOnly())
    ) {
      remove(status);
      return;
    }
    if (getDiffType() == DiffTypes.EDITED) {
      for (int i = 0; i < children.length; i++) child(i).copy(left, removeItems, status);
      return;
    }
    DirectoryHandle toDirParent;
    if ((left && isLeftOnly()) || (!left && isRightOnly())) {
      toDirParent = (DirectoryHandle) parent().item(!left);
    } else return;

    Consumer<DirectoryHandle> onDirCreated = dirHandle -> {
      setItem(!left, dirHandle);
      for (int i = 0; i < children.length; i++) child(i).copy(left, removeItems, status);
      status.onDirCopied();
    };

    status.inWork++;
    FsWorkerJobs.mkDir(status.executor, toDirParent, path, onDirCreated, status::onCopyError);
  }

  private void copyFile(boolean left, boolean removeItems, ModelCopyDeleteStatus status) {
    if (removeItems &&
        (left && isRightOnly()) ||
        (!left && isLeftOnly())
    ) {
      remove(status);
      return;
    }
    FileHandle fromFile = (FileHandle) item(left), toFile;
    DirectoryHandle toDir = (DirectoryHandle) parent().item(!left);
    if (getDiffType() == DiffTypes.EDITED) {
      toFile = (FileHandle) item(!left);
    } else if ((left && isLeftOnly()) || (!left && isRightOnly())) {
      toFile = toDir.createFileHandle(path);
      setItem(!left, toFile);
    } else return;

    DoubleConsumer onFileCopied = (bytes) -> {
      insertItem();
      status.onFileCopied();
    };

    status.inWork++;
    FsWorkerJobs.copyFile(status.executor, fromFile, toFile, onFileCopied, status::onCopyError);
  }
}
