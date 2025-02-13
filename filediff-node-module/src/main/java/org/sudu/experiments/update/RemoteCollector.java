package org.sudu.experiments.update;

import org.sudu.experiments.*;
import org.sudu.experiments.arrays.ArrayReader;
import org.sudu.experiments.arrays.ArrayWriter;
import org.sudu.experiments.diff.DiffTypes;
import org.sudu.experiments.diff.SizeScanner;
import org.sudu.experiments.diff.folder.FolderDiffModel;
import org.sudu.experiments.diff.folder.ItemFolderDiffModel;
import org.sudu.experiments.diff.folder.RemoteFolderDiffModel;
import org.sudu.experiments.editor.worker.ArgsCast;
import org.sudu.experiments.editor.worker.diff.DiffUtils;
import org.sudu.experiments.js.JsArray;
import org.sudu.experiments.LoggingJs;
import org.sudu.experiments.js.node.SshDirectoryHandle;
import org.sudu.experiments.math.Numbers;
import org.sudu.experiments.protocol.BackendMessage;
import org.sudu.experiments.protocol.FrontendMessage;
import org.sudu.experiments.ui.fs.FileCompare;
import org.sudu.experiments.worker.ArrayView;
import org.teavm.jso.JSObject;
import org.teavm.jso.browser.Performance;
import org.teavm.jso.typedarrays.Int32Array;

import java.util.*;
import java.util.function.Consumer;

public class RemoteCollector {

  private ItemFolderDiffModel root;

  private final NodeWorkersPool executor;
  private final int workerSize;
  private final boolean scanFileContent;

  private Consumer<JsArray<JSObject>> sendResult;
  private Consumer<JsArray<JSObject>> onComplete;
  private Runnable onShutdown;

  private static final double SEND_FIRST_MSG_MS = 500;
  private static final double SEND_MSG_MS = 2000;

  private int inComparing = 0;
  private int sentToWorker = 0;

  private int foldersCompared = 0, filesCompared;
  private int filesInserted = 0, filesDeleted = 0, filesEdited = 0;
  private int lastFoldersCompared = 0, lastFilesCompared = 0;

  private boolean firstMessageSent = false;
  private boolean lastMessageSent = false;
  private boolean isShutdown = false;
  private boolean isRefresh = false;

  private FrontendMessage lastFrontendMessage = FrontendMessage.EMPTY;
  private double lastMessageSentTime;
  private double startTime;
  private double completeTime = -1;

  private final Deque<Runnable> sendToWorkerQueue;

  private final BitSet lastFilters = new BitSet();
  private int lastFiltersLength = 0;

  public RemoteCollector(
      ItemFolderDiffModel root,
      boolean scanFileContent,
      NodeWorkersPool executor
  ) {
    this.root = root;
    this.executor = executor;
    this.scanFileContent = scanFileContent;
    this.startTime = this.lastMessageSentTime = Performance.now();
    sendToWorkerQueue = new LinkedList<>();
    workerSize = executor.workersLength();
  }

  public void beginCompare() {
    String beginMsg = "Begin comparing " + leftHandle().getName() +
        " ↔ " + rightHandle().getName();
    LoggingJs.info(beginMsg);
    compare(root);
  }

  public void refresh() {
    LoggingJs.info("RemoteCollector.Refresh");
    firstMessageSent = lastMessageSent = false;
    isRefresh = true;
    startTime = lastMessageSentTime = Performance.now();
    foldersCompared = filesCompared = 0;
    filesInserted = filesDeleted = filesEdited = 0;
    completeTime = -1;
    root.setCompared(false);
    root.childrenComparedCnt = 0;
    sendToWorkerQueue.clear();
    beginCompare();
  }

  public void onMessageGot(FrontendMessage message) {
    double time = Performance.now() - startTime;
    LoggingJs.trace("RemoteCollector got frontend message in " + time + "ms");
    lastFrontendMessage = message;
    if (sendResult != null) sendMessage();
  }

  public FileHandle findFileByIndexPath(int[] path, boolean left) {
    var model = (ItemFolderDiffModel) root.findNodeByIndPath(path);
    if (model == null) {
      String error = root.describeError(path);
      LoggingJs.error("findFileByIndexPath: root.findNodeByIndPath(path) failed: " + error);
      return null;
    }
    if (!(model.item(left) instanceof FileHandle fileHandle)) {
      String error = String.format("model.item(%s): %s isn't file", left, model.getFullPath(""));
      LoggingJs.error(error);
      return null;
    }
    return fileHandle;
  }

  public void applyDiff(int[] path, boolean left) {
    var model = (ItemFolderDiffModel) root.findNodeByIndPath(path);
    if (model == null) {
      String error = root.describeError(path);
      LoggingJs.debug("applyDiff: root.findNode(path) failed: " + error);
      return;
    }
    int diffType = model.getDiffType();
    if (diffType == DiffTypes.DEFAULT) return;

    boolean isDeleteDiff = (left && diffType == DiffTypes.INSERTED) || (!left && diffType == DiffTypes.DELETED);
    boolean isInsertDiff = (left && diffType == DiffTypes.DELETED) || (!left && diffType == DiffTypes.INSERTED);

    ArrayWriter pathWriter = new ArrayWriter();
    lastFrontendMessage.collectPath(path, pathWriter, root, left);

    Runnable updateModel = () -> {
      LoggingJs.info("RemoteCollector.applyDiff.updateModel");
      if (isDeleteDiff) {
        var node = lastFrontendMessage.findNode(path);
        var parentNode = lastFrontendMessage.findParentNode(path);
        if (node != null && parentNode != null) parentNode.deleteItem(node);
        model.deleteItem();
      } else if (isInsertDiff) {
        model.insertItem();
      } else {
        model.editItem(left);
      }
      if (isDeleteDiff) sendApplied();
      else updateModelItems(model, left);
    };
    if (model.isFile()) {
      if (!isDeleteDiff) copyFile(model, left, updateModel);
      else removeFile(model, updateModel);
    } else {
      if (!isDeleteDiff) copyFolder(model, left, updateModel);
      else removeFolder(model, updateModel);
    }
  }

  private void updateModelItems(ItemFolderDiffModel model, boolean left) {
    Deque<ItemFolderDiffModel> updModelPath = new LinkedList<>();
    var updModel = model.findNeedUpdate(updModelPath);

    int st = 0;
    StringBuilder sb = new StringBuilder();
    ArrayWriter writer = new ArrayWriter();
    writer.write(updModelPath.size());
    for (var item: updModelPath) {
      String path = item.path;
      writer.write(st, path.length(), !item.isFile() ? 1 : 0);
      sb.append(path);
      st += path.length();
    }
    LoggingJs.info("Upd Model: " + updModel.path);
    LoggingJs.info("Send reread");
    executor.sendToWorker(true,
        (res) -> updateNode(updModel, !left, updModelPath, res),
        DiffUtils.REREAD_FOLDER,
        updModel.item(!left), writer.getInts(), sb.toString().toCharArray()
    );
  }

  private void updateNode(ItemFolderDiffModel updModel, boolean left, Deque<ItemFolderDiffModel> updModelPath, Object[] result) {
    LoggingJs.info("RemoteCollector.updateNode");
    int[] ints = ((ArrayView) result[0]).ints();
    ArrayReader reader = new ArrayReader(ints);
    Deque<FsItem> items = new LinkedList<>();
    for (int i = 1; i < result.length; i++) {
      items.add((FsItem) result[i]);
    }
    updModel.updateFsItems(left, reader, items, updModelPath);
    sendApplied();
  }

  public void fileSave(int[] path, boolean left, Object source, String encoding) {
    var model = (ItemFolderDiffModel) root.findNodeByIndPath(path);
    var item = (FileHandle) (left ? model.left() : model.right());
    LoggingJs.debug("fileSave " + item + ", encoding = " + encoding);
    item.writeText(source, encoding, () -> cmpFilesAndSend(model, item), this::onError);
  }

  private void copyFile(ItemFolderDiffModel model, boolean left, Runnable onComplete) {
    LoggingJs.debug("copyFile " + model + ", left = " + left);
    if (!(model.item(left) instanceof FileHandle fileItem)) return;
    String to = model.getFullPath(!left ? leftHandle().getFullPath() : rightHandle().getFullPath());
    LoggingJs.debug("copyFile " + fileItem + " -> " + to);
    fileItem.copyTo(to, onComplete, this::onError);
  }

  private void removeFile(ItemFolderDiffModel model, Runnable onComplete) {
    if (!(model.item() instanceof FileHandle fileItem)) return;
    LoggingJs.debug("removeFile " + fileItem);
    fileItem.remove(onComplete, this::onError);
  }

  private void copyFolder(ItemFolderDiffModel model, boolean left, Runnable onComplete) {
    if (!(model.item(left) instanceof DirectoryHandle dirItem)) return;
    String to = model.getFullPath(!left ? leftHandle().getFullPath() : rightHandle().getFullPath());
    LoggingJs.debug("copyFolder " + dirItem + " -> " + to);
    dirItem.copyTo(to, onComplete, this::onError);
  }

  private void removeFolder(ItemFolderDiffModel model, Runnable onComplete) {
    if (!(model.item() instanceof DirectoryHandle dirItem)) return;
    LoggingJs.debug("remove folder " + dirItem);
    dirItem.remove(onComplete, this::onError);
  }

  private void cmpFilesAndSend(ItemFolderDiffModel model, FileHandle item) {
    LoggingJs.debug("file write complete: " + item);
    cmpFilesAndSend(model);
  }

  private void cmpFilesAndSend(ItemFolderDiffModel model) {
    Consumer<Object[]> onCompared = result -> {
      String message = FileCompare.message(result);
      if (message != null)
        LoggingJs.debug("cmpFilesAndSend message: " + message);
      boolean equals = FileCompare.isEquals(result);
      if (equals) model.setDiffType(DiffTypes.DEFAULT);
      else model.setDiffType(DiffTypes.EDITED);
      model.updateItem();
      sendApplied();
    };
    executor.sendToWorker(
        onCompared,
        DiffUtils.CMP_FILES,
        model.left(), model.right()
    );
  }

  private void onError(String error) {
    LoggingJs.error(error);
  }

  private ItemFolderDiffModel oldOrNull(
      FolderDiffModel[] oldChildren,
      ItemFolderDiffModel parent,
      FsItem item
  ) {
    String path = item.getName();
    boolean isFile = item instanceof FileHandle;
    if (oldChildren == null) return new ItemFolderDiffModel(parent, path);
    for (var oldChild: oldChildren)
      if (oldChild instanceof ItemFolderDiffModel itemChild &&
          path.equals(itemChild.path) && isFile == itemChild.isFile()
      ) return itemChild;
    return new ItemFolderDiffModel(parent, path);
  }

  private void compare(ItemFolderDiffModel model) {
    ++inComparing;
    if (model.left() instanceof DirectoryHandle leftDir &&
        model.right() instanceof DirectoryHandle rightDir
    ) compareFolders(model, leftDir, rightDir);
    else if (model.left() instanceof FileHandle leftFile
        && model.right() instanceof FileHandle rightFile
    ) compareFiles(model, leftFile, rightFile);
    else throw new IllegalArgumentException();
  }

  private void compareFolders(
      ItemFolderDiffModel model,
      DirectoryHandle leftDir,
      DirectoryHandle rightDir
  ) {
    Runnable task = () -> executor.sendToWorker(
        result -> onFoldersCompared(model, result),
        DiffUtils.CMP_FOLDERS,
        leftDir, rightDir
    );
    sendToWorkerQueue.addLast(task);
    sendTaskToWorker();
  }

  private void onFoldersCompared(
      ItemFolderDiffModel model,
      Object[] result
  ) {
    foldersCompared++;
    int[] ints = ArgsCast.intArray(result, 0);

    int commonLen = ints[0];
    int leftLen = ints[1];
    int rightLen = ints[2];

    int[] diffs = Arrays.copyOfRange(ints, 3, 3 + commonLen);
    int[] kinds = Arrays.copyOfRange(ints, 3 + commonLen, 3 + 2 * commonLen);
    FsItem[] leftItem = Arrays.copyOfRange(result, 1, 1 + leftLen, FsItem[].class);
    FsItem[] rightItem = Arrays.copyOfRange(result, 1 + leftLen, 1 + leftLen + rightLen, FsItem[].class);

    int len = diffs.length;
    var oldChildren = model.children;
    model.childrenComparedCnt = 0;
    model.children = new ItemFolderDiffModel[len];

    int lP = 0, rP = 0;
    int mP = 0;
    boolean edited = false;

    while (mP < len) {
      int kind = kinds[mP];
      if (diffs[mP] == DiffTypes.DELETED) {
        edited = true;
        model.children[mP] = oldOrNull(oldChildren, model, leftItem[lP]);
        model.child(mP).setCompared(false);
        model.child(mP).posInParent = mP;
        model.child(mP).setItemKind(kind);
        model.child(mP).setDiffType(DiffTypes.DELETED);
        model.child(mP).setItem(leftItem[lP]);
        read(model.child(mP));
        mP++;
        lP++;
      } else if (diffs[mP] == DiffTypes.INSERTED) {
        edited = true;
        model.children[mP] = oldOrNull(oldChildren, model, rightItem[rP]);
        model.child(mP).setCompared(false);
        model.child(mP).posInParent = mP;
        model.child(mP).setItemKind(kind);
        model.child(mP).setDiffType(DiffTypes.INSERTED);
        model.child(mP).setItem(rightItem[rP]);
        read(model.child(mP));
        mP++;
        rP++;
      } else {
        model.children[mP] = oldOrNull(oldChildren, model, leftItem[lP]);
        model.child(mP).setCompared(false);
        model.child(mP).posInParent = mP;
        model.child(mP).setItemKind(kind);
        model.child(mP).setItems(leftItem[lP], rightItem[rP]);
        compare(model.child(mP));
        mP++;
        lP++;
        rP++;
      }
    }
    if (len == 0) model.itemCompared();
    if (edited) model.markUp(DiffTypes.EDITED);
    onItemCompared();
  }

  private void compareFiles(
      ItemFolderDiffModel model,
      FileHandle leftFile,
      FileHandle rightFile
  ) {
    if (scanFileContent) {
      Runnable task = () -> executor.sendToWorker(
          result -> onFilesCompared(model, result),
          DiffUtils.CMP_FILES,
          leftFile, rightFile
      );
      sendToWorkerQueue.addLast(task);
      sendTaskToWorker();
    } else {
      new SizeScanner(leftFile, rightFile) {
        @Override
        protected void onComplete(int sizeL, int sizeR) {
          onFilesCompared(model, sizeL == sizeR);
        }
      };
    }
  }

  private void onFilesCompared(
      ItemFolderDiffModel model,
      Object[] result
  ) {
    boolean equals = FileCompare.isEquals(result);
    String message = FileCompare.message(result);
    if (message != null)
      LoggingJs.debug("onFilesCompared message: " + message);
    onFilesCompared(model, equals);
  }

  private void onFilesCompared(
      ItemFolderDiffModel model,
      boolean equals
  ) {
    filesCompared++;
    if (!equals) {
      filesEdited++;
      model.markUp(DiffTypes.EDITED);
    }
    model.itemCompared();
    onItemCompared();
  }

  private void read(ItemFolderDiffModel model) {
    if (model.item() instanceof DirectoryHandle dirHandle) readFolder(model, dirHandle);
    else {
      if (model.getDiffType() == DiffTypes.INSERTED) {
        filesInserted++;
      } else if (model.getDiffType() == DiffTypes.DELETED) {
        filesDeleted++;
      }
      filesCompared++;
      model.itemCompared();
    }
  }

  private void readFolder(ItemFolderDiffModel model, DirectoryHandle dirHandle) {
    ++inComparing;
    Runnable task = () -> executor.sendToWorker(
        result -> onFolderRead(model, result),
        DiffUtils.READ_FOLDER,
        dirHandle, new int[]{model.getDiffType(), model.getItemKind(), model.posInParent}
    );
    sendToWorkerQueue.addLast(task);
    sendTaskToWorker();
  }

  private void onFolderRead(
      ItemFolderDiffModel model,
      Object[] result
  ) {
    int[] ints = ArgsCast.intArray(result, 0);
    int[] stats = ArgsCast.intArray(result, 1);
    String[] paths = new String[stats[0]];
    FsItem[] fsItems = new FsItem[stats[1]];
    int foldersRead = stats[2];
    int filesRead = stats[3];
    foldersCompared += foldersRead;
    filesCompared += filesRead;
    if (model.getDiffType() == DiffTypes.INSERTED) {
      filesInserted += filesRead;
    } else if (model.getDiffType() == DiffTypes.DELETED) {
      filesDeleted += filesRead;
    }
    for (int i = 0; i < paths.length; i++) paths[i] = (String) result[i + 2];
    for (int i = 0; i < fsItems.length; i++) fsItems[i] = (FsItem) result[stats[0] + i + 2];
    var updModel = ItemFolderDiffModel.fromInts(ints, paths, fsItems);
    model.update(updModel);
    onItemCompared();
  }

  private void onItemCompared() {
    if (--inComparing < 0) throw new IllegalStateException("inComparing cannot be negative");
    if (--sentToWorker < 0) throw new IllegalStateException("sentToWorker cannot be negative");
    if (isShutdown) shutdown();
    else if (inComparing == 0) onComplete();
    else {
      sendTaskToWorker();
      if (sendResult == null || lastMessageSent) return;
      double time = firstMessageSent
          ? RemoteCollector.SEND_MSG_MS
          : RemoteCollector.SEND_FIRST_MSG_MS;
      if (getTimeDelta() >= time) sendMessage();
    }
  }

  // todo rewrite model searching by its fullPath string
  public void onRemoteFileSave(boolean left, String fullPath) {
    String rootPath = replaceSlashes(left ? leftHandle().getFullPath() : rightHandle().getFullPath());
    String preparedPath = replaceSlashes(fullPath);
    if (preparedPath.startsWith(rootPath)) {
      preparedPath = preparedPath.substring(rootPath.length() + 1);
      String[] path = preparedPath.split("/", -1);
      ItemFolderDiffModel model = (ItemFolderDiffModel) root.getByPath(path, 0, left);
      if (model != null) {
        cmpFilesAndSend(model);
      } else {
        LoggingJs.error("RemoteCollector.onRemoteFileSave: can't find model by path: " + fullPath);
      }
    }
  }

  private void sendTaskToWorker() {
    if (sentToWorker < workerSize && !sendToWorkerQueue.isEmpty()) {
      var task = sendToWorkerQueue.removeFirst();
      sentToWorker++;
      task.run();
    }
  }

  private void sendMessage() {
    if (sendResult == null) return;
    firstMessageSent = true;
    send(sendResult, lastFrontendMessage, DiffModelChannelUpdater.FRONTEND_MESSAGE_ARRAY);

    this.lastMessageSentTime = Performance.now();
    if (lastFilesCompared == filesCompared && lastFoldersCompared == foldersCompared) return;
    String progressMsg = "Sent message in " + Numbers.iRnd(lastMessageSentTime - startTime) + "ms, " +
        "foldersCompared: " + foldersCompared + ", filesCompared: " + filesCompared;
    LoggingJs.trace(progressMsg);
    lastFilesCompared = filesCompared;
    lastFoldersCompared = foldersCompared;
  }

  private void onComplete() {
    if (onComplete == null) return;
    lastMessageSent = true;
    completeTime = Performance.now();
    send(onComplete, null, DiffModelChannelUpdater.FRONTEND_MESSAGE_ARRAY);

    this.lastMessageSentTime = Performance.now();
    String completeMsg = leftHandle().getName() + " ↔ " + rightHandle().getName() +
        " - finished in " + Numbers.iRnd(lastMessageSentTime - startTime) + "ms, " +
        "foldersCompared: " + foldersCompared + ", filesCompared: " + filesCompared;
    LoggingJs.info(completeMsg);
  }

  private void send(Consumer<JsArray<JSObject>> send, FrontendMessage message, Int32Array msgType) {
    LoggingJs.debug("inComparing: " + inComparing);
    if (isRefresh) {
      isRefresh = false;
      var fullMsg = serializeBackendMessage(root, message);
      fullMsg.push(DiffModelChannelUpdater.REFRESH_ARRAY);
      send.accept(fullMsg);
    }
    var backendMessage = filteredBackendModel();
    var jsArray = serializeBackendMessage(backendMessage, message);
    jsArray.push(msgType);
    send.accept(jsArray);
  }

  private JsArray<JSObject> serializeBackendMessage(RemoteFolderDiffModel model, FrontendMessage message) {
    String leftRootPath = leftHandle().getFullPath();
    String leftRootName = rootName(leftHandle());
    String rightRootPath = rightHandle().getFullPath();
    String rightRootName = rootName(rightHandle());
    return BackendMessage.serialize(
        model, message,
        leftRootPath,
        leftRootName,
        rightRootPath,
        rightRootName,
        foldersCompared,
        filesCompared,
        getTotalTime(),
        getDifferentFilesCnt()
    );
  }

  private void sendApplied() {
    if (sendResult == null) return;
    LoggingJs.debug("Send applied model");
    send(sendResult, lastFrontendMessage, DiffModelChannelUpdater.APPLY_DIFF_ARRAY);
  }

  public RemoteFolderDiffModel filteredBackendModel() {
    if (!isFiltered()) return root;
    var filtered = root.applyFilter(lastFilters, null);
    if (filtered == null) {
      filtered = new RemoteFolderDiffModel(null, "");
      filtered.setCompared(root.isCompared());
      filtered.setItemKind(root.getItemKind());
      filtered.children = new RemoteFolderDiffModel[]{};
    }
    return filtered;
  }

  public void applyFilters(int[] filters) {
    lastFilters.clear();
    lastFiltersLength = filters.length;
    for (var filter: filters) {
      if (filter == DiffTypes.INSERTED) filter = DiffTypes.DELETED;
      else if (filter == DiffTypes.DELETED) filter = DiffTypes.INSERTED;
      lastFilters.set(filter);
    }
    send(sendResult, lastFrontendMessage, DiffModelChannelUpdater.APPLY_FILTERS_ARRAY);
  }

  private double getTimeDelta() {
    return Performance.now() - lastMessageSentTime;
  }

  private int getTotalTime() {
    double time = (completeTime < 0 ? Performance.now() : completeTime);
    return Numbers.iRnd(time - startTime);
  }

  public void setSendResult(Consumer<JsArray<JSObject>> sendResult) {
    this.sendResult = sendResult;
  }

  public void setOnComplete(Consumer<JsArray<JSObject>> onComplete) {
    this.onComplete = onComplete;
  }

  public void shutdown(Runnable onShutdown) {
    this.isShutdown = true;
    this.onShutdown = onShutdown;
    sendToWorkerQueue.clear();
    shutdown();
  }

  private void shutdown() {
    if (sentToWorker != 0) return;
    root = null;
    lastFrontendMessage = null;
    sendResult = onComplete = null;
    onShutdown.run();
  }

  private DirectoryHandle leftHandle() {
    return (DirectoryHandle) root.items[0];
  }

  private DirectoryHandle rightHandle() {
    return (DirectoryHandle) root.items[1];
  }

  private String replaceSlashes(String path) {
    return path.replace('\\', '/');
  }

  private boolean isFiltered() {
    return !(lastFiltersLength == 4 || lastFiltersLength == 0);
  }

  private int getDifferentFilesCnt() {
    if (!isFiltered()) return filesEdited + filesInserted + filesDeleted;
    int result = 0;
    if (lastFilters.get(DiffTypes.EDITED)) result += filesEdited;
    if (lastFilters.get(DiffTypes.INSERTED)) result += filesInserted;
    if (lastFilters.get(DiffTypes.DELETED)) result += filesDeleted;
    return result;
  }

  private String rootName(DirectoryHandle handle) {
    if (handle instanceof SshDirectoryHandle sshHandle) return sshHandle.getFullPathWithHost();
    return handle.getFullPath();
  }
}
