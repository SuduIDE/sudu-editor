package org.sudu.experiments.update;

import org.sudu.experiments.*;
import org.sudu.experiments.arrays.ArrayWriter;
import org.sudu.experiments.diff.DiffTypes;
import org.sudu.experiments.diff.folder.FolderDiffModel;
import org.sudu.experiments.diff.folder.ItemFolderDiffModel;
import org.sudu.experiments.diff.folder.ModelCopyDeleteStatus;
import org.sudu.experiments.diff.folder.RemoteFolderDiffModel;
import org.sudu.experiments.editor.worker.ArgsCast;
import org.sudu.experiments.editor.worker.FileCompare;
import org.sudu.experiments.editor.worker.FsWorkerJobs;
import org.sudu.experiments.editor.worker.SizeScanner;
import org.sudu.experiments.editor.worker.diff.DiffUtils;
import org.sudu.experiments.exclude.ExcludeList;
import org.sudu.experiments.js.JsArray;
import org.sudu.experiments.js.TextEncoder;
import org.sudu.experiments.js.node.SshDirectoryHandle;
import org.sudu.experiments.math.Numbers;
import org.sudu.experiments.protocol.BackendMessage;
import org.sudu.experiments.protocol.FrontendMessage;
import org.sudu.experiments.protocol.FrontendTreeNode;
import org.teavm.jso.JSObject;
import org.teavm.jso.browser.Performance;
import org.teavm.jso.core.JSString;
import org.teavm.jso.typedarrays.Int32Array;

import java.util.*;
import java.util.function.Consumer;

public class RemoteCollector {

  private ItemFolderDiffModel root;

  final NodeWorkersPool executor;
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
  private boolean onCompleteSent = false;
  private boolean isShutdown = false;
  private boolean isRefresh = false;

  private FrontendMessage lastFrontendMessage = FrontendMessage.EMPTY;
  private double lastMessageSentTime;
  private double startTime;
  private double completeTime = -1;

  private final Deque<Runnable> sendToWorkerQueue;

  private final BitSet lastFilters = new BitSet();
  private int lastFiltersLength = 0;

  private final ExcludeList exclude;

  public RemoteCollector(
      ItemFolderDiffModel root,
      boolean scanFileContent,
      NodeWorkersPool executor,
      ExcludeList exclude
  ) {
    this.root = root;
    this.executor = executor;
    this.scanFileContent = scanFileContent;
    this.startTime = this.lastMessageSentTime = Performance.now();
    this.exclude = exclude;
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
    onCompleteSent = false;
    root.setCompared(false);
    root.childrenComparedCnt = 0;
    sendToWorkerQueue.clear();
    beginCompare();
  }

  public void onMessageGot(FrontendMessage message) {
    double time = Performance.now() - startTime;
    LoggingJs.trace("RemoteCollector got frontend message in " + time + "ms");
    lastFrontendMessage = message;
    sendExcludedToCompare(root, lastFrontendMessage.openedFolders);
    if (sendResult != null) sendMessage();
  }

  public FileHandle findFileByIndexPath(int[] path, boolean left) {
    var model = (ItemFolderDiffModel) root.findNodeByIndPath(path);
    if (model == null) {
      String error = root.describeError(path);
      onError("findFileByIndexPath: root.findNodeByIndPath(path) failed: " + error);
      return null;
    }
    if (!(model.item(left) instanceof FileHandle fileHandle)) {
      String error = String.format("model.item(%s): %s isn't file", left, model.getFullPath(""));
      onError(error);
      return null;
    }
    return fileHandle;
  }

  public void applyDiff(int[] path, boolean left) {
    var model = (ItemFolderDiffModel) root.findNodeByIndPath(path);
    if (model == null) {
      String error = root.describeError(path);
      onError("applyDiff: root.findNode(path) failed: " + error);
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
      }
      sendApplied();
    };
    if (model.isFile()) {
      if (!isDeleteDiff) copyFile(model, left, updateModel);
      else removeFile(model, updateModel);
    } else {
      if (!isDeleteDiff) copyFolder(model, left, updateModel);
      else removeFolder(model, updateModel);
    }
  }

  public void fileSave(
      int[] path, boolean left,
      JSString source, JSString encoding
  ) {
    var model = (ItemFolderDiffModel) root.findNodeByIndPath(path);
    var file = (FileHandle) (left ? model.left() : model.right());
    char[] chars = TextEncoder.toCharArray(source);
    LoggingJs.debug("fileSave " + file + ", encoding = " + encoding
        + ", content length = " + chars.length);
    FsWorkerJobs.fileWriteText(
        executor, file, chars, encoding.stringValue(),
        () -> cmpFilesAndSend(model, file), this::onError);
  }

  void copyFile(ItemFolderDiffModel model, boolean left, Runnable onComplete) {
    LoggingJs.debug("copyFile " + model.path + ", left = " + left);
    if (!(model.item(left) instanceof FileHandle fileItem)) return;

    ModelCopyDeleteStatus status = new ModelCopyDeleteStatus(executor, onComplete, this::onError);
    if (model == root) {
      model.copy(left, status);
    } else {
      Consumer<DirectoryHandle> onToDirGet = (toDir) -> {
        LoggingJs.debug("copyFile " + fileItem + " to dir " + toDir);
        model.parent().setItem(!left, toDir);
        model.copy(left, status);
      };
      model.parent().getOrCreateDir(!left, executor, onToDirGet, this::onError);
    }
  }

  private void copyFolder(ItemFolderDiffModel model, boolean left, Runnable onComplete) {
    LoggingJs.debug("copyFolder " + model.path + ", left = " + left);
    if (!(model.item(left) instanceof DirectoryHandle dirItem)) return;

    ModelCopyDeleteStatus status = new ModelCopyDeleteStatus(executor, onComplete, this::onError);
    if (model == root) {
      model.copy(left, status);
    } else {
      Consumer<DirectoryHandle> onToDirGet = (toDir) -> {
        LoggingJs.debug("copyFolder " + dirItem + " -> " + toDir);
        model.parent().setItem(!left, toDir);
        model.copy(left, status);
      };
      model.parent().getOrCreateDir(!left, executor, onToDirGet, this::onError);
    }
  }

  private void removeFile(ItemFolderDiffModel model, Runnable onComplete) {
    if (!(model.item() instanceof FileHandle fileItem)) return;
    LoggingJs.debug("removeFile " + fileItem);
    ModelCopyDeleteStatus status = new ModelCopyDeleteStatus(executor, onComplete, this::onError);
    model.remove(status);
  }

  private void removeFolder(ItemFolderDiffModel model, Runnable onComplete) {
    if (!(model.item() instanceof DirectoryHandle dirItem)) return;
    LoggingJs.debug("removeFolder " + dirItem);
    ModelCopyDeleteStatus status = new ModelCopyDeleteStatus(executor, onComplete, this::onError);
    model.remove(status);
  }

  private void cmpFilesAndSend(ItemFolderDiffModel model, FileHandle item) {
    LoggingJs.debug("file write complete: " + item);
    cmpFilesAndSend(model);
  }

  private void cmpFilesAndSend(ItemFolderDiffModel model) {
    FileCompare.asyncCompareFiles(executor,
        (FileHandle) model.left(),
        (FileHandle) model.right(),
        (boolean equals, String error) -> {
          if (error != null)
            LoggingJs.error("FileCompare error: " + error);
          if (equals) model.setDiffType(DiffTypes.DEFAULT);
          else model.setDiffType(DiffTypes.EDITED);
          model.updateItem();
          sendApplied();
        }
    );
  }

  private void onError(String error) {
    LoggingJs.error(error);
  }

  private ItemFolderDiffModel oldOrNew(
      FolderDiffModel[] oldChildren,
      ItemFolderDiffModel parent,
      FsItem item
  ) {
    ItemFolderDiffModel model = getOldOrNew(oldChildren, parent, item);
    checkExcludeModel(model);
    return model;
  }

  private static ItemFolderDiffModel getOldOrNew(
      FolderDiffModel[] oldChildren,
      ItemFolderDiffModel parent,
      FsItem item
  ) {
    String path = item.getName();
    boolean isFile = item instanceof FileHandle;
    if (oldChildren == null) return new ItemFolderDiffModel(parent, path);
    for (var oldChild: oldChildren) {
      if (!(oldChild instanceof ItemFolderDiffModel itemChild)) continue;
      if (path.equals(itemChild.path) && isFile == itemChild.isFile()) return itemChild;
    }
    return new ItemFolderDiffModel(parent, path);
  }

  private void compare(ItemFolderDiffModel model) {
    if (model.isExcluded() && !model.isSendExcluded()) {
      LoggingJs.info("Excluded: " + model.getFullPath(""));
      model.itemCompared();
      return;
    }
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
    LoggingJs.trace("Comparing folders " + model.path);
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
    LoggingJs.trace("Compared folders" + model.path);
    foldersCompared++;
    int[] ints = ArgsCast.intArray(result, 0);
    if (isErrorInts(ints)) {
      String error = ArgsCast.string(result, 1);
      onError(error);
      return;
    }

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
        model.children[mP] = oldOrNew(oldChildren, model, leftItem[lP]);
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
        model.children[mP] = oldOrNew(oldChildren, model, rightItem[rP]);
        model.child(mP).setCompared(false);
        model.child(mP).posInParent = mP;
        model.child(mP).setItemKind(kind);
        model.child(mP).setDiffType(DiffTypes.INSERTED);
        model.child(mP).setItem(rightItem[rP]);
        read(model.child(mP));
        mP++;
        rP++;
      } else {
        model.children[mP] = oldOrNew(oldChildren, model, leftItem[lP]);
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
    LoggingJs.trace("Comparing files " + model.path);
    if (scanFileContent) {
      Runnable task = () -> FileCompare.asyncCompareFiles(executor,
          leftFile, rightFile,
          (equals, error) -> onFilesCompared(model, equals, error));
      sendToWorkerQueue.addLast(task);
      sendTaskToWorker();
    } else {
      SizeScanner.scan(executor, leftFile, rightFile,
          (double sizeL, double sizeR, String error) -> {
            if (error != null) {
              onFilesCompared(model, false, error);
            } else {
              onFilesCompared(model, sizeL == sizeR, null);
            }
          }
      );
    }
  }

  private void onFilesCompared(
      ItemFolderDiffModel model,
      boolean equals, String message
  ) {
    LoggingJs.trace("Compared files " + model.path);
    if (message != null)
      LoggingJs.debug("onFilesCompared message: " + message);

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
    if (isErrorInts(ints)) {
      String error = ArgsCast.string(result, 1);
      onError(error);
      return;
    }
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
    checkExcludeModelChildren(model);
    onItemCompared();
  }

  private void checkExcludeModelChildren(RemoteFolderDiffModel model) {
    if (exclude == null || model == null) return;
    checkExcludeModel(model);
    if (model.isExcluded()) model.setSendExcluded(true);
    if (model.children == null) return;
    for (int i = 0; i < model.children.length; i++) checkExcludeModelChildren(model.child(i));
  }

  private void checkExcludeModel(RemoteFolderDiffModel model) {
    if (exclude == null) return;
    if (model.parent != null && model.parent.isExcluded()) {
      model.setExcluded(true);
      model.itemCompared();
    } else {
      String fullPath = model.getFullPath("");
      boolean isDir = !model.isFile();
      boolean excluded = exclude.isExcluded(fullPath, isDir);
      model.setExcluded(excluded);
      if (excluded) model.itemCompared();
    }
  }

  private void onItemCompared() {
    if (--inComparing < 0) throw new IllegalStateException("inComparing cannot be negative");
    if (--sentToWorker < 0) throw new IllegalStateException("sentToWorker cannot be negative");
    if (isShutdown) shutdown();
    else if (inComparing == 0) {
      if (!onCompleteSent) {
        onCompleteSent = true;
        onComplete();
      } else sendMessage();
    } else {
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

  private void sendExcludedToCompare(ItemFolderDiffModel model, FrontendTreeNode frontendNode) {
    if (model == null) return;
    if (model.isExcluded() && !model.isSendExcluded()) {
      model.setSendExcluded(true);
      if (model.isBoth()) compare(model);
      else read(model);
    }
    if (model.children == null || frontendNode == null || frontendNode.children == null) return;
    for (int i = 0; i < model.children.length; i++) {
      ItemFolderDiffModel childModel = model.child(i);
      FrontendTreeNode childNode = frontendNode.child(i, childModel.path, childModel.isFile());
      sendExcludedToCompare(childModel, childNode);
    }
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
    if (lastFilters.isEmpty()) fillFilters();
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

  private void fillFilters() {
    lastFilters.clear();
    lastFilters.set(DiffTypes.DEFAULT);
    lastFilters.set(DiffTypes.DELETED);
    lastFilters.set(DiffTypes.INSERTED);
    lastFilters.set(DiffTypes.EDITED);
    lastFiltersLength = 4;
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

  private boolean isErrorInts(int[] ints) {
    return ints.length == 1 && ints[0] == -1;
  }
}
