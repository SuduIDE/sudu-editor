package org.sudu.experiments.update;

import org.sudu.experiments.*;
import org.sudu.experiments.arrays.ArrayWriter;
import org.sudu.experiments.diff.DiffTypes;
import org.sudu.experiments.diff.SizeScanner;
import org.sudu.experiments.diff.folder.ItemFolderDiffModel;
import org.sudu.experiments.editor.worker.ArgsCast;
import org.sudu.experiments.editor.worker.diff.DiffUtils;
import org.sudu.experiments.js.JsArray;
import org.sudu.experiments.math.Numbers;
import org.sudu.experiments.protocol.BackendMessage;
import org.sudu.experiments.protocol.FrontendMessage;
import org.teavm.jso.JSObject;
import org.teavm.jso.browser.Performance;
import org.teavm.jso.core.JSString;

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
  private int lastFoldersCompared = 0, lastFilesCompared = 0;

  private boolean firstMessageSent = false;
  private boolean lastMessageSent = false;
  private boolean isShutdown = false;

  private FrontendMessage lastFrontendMessage = FrontendMessage.EMPTY;
  private double lastMessageSentTime;
  private final double startTime;

  private final Deque<Runnable> sendToWorkerQueue;

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
    String beginMsg = "Begin comparing" + leftHandle().getName() + " ↔ " + rightHandle().getName();
    LoggingJs.Static.logger.log(LoggingJs.INFO, JSString.valueOf(beginMsg));
    compare(root);
  }

  public void onMessageGot(FrontendMessage message) {
    double time = Performance.now() - startTime;
    LoggingJs.Static.logger.log(LoggingJs.TRACE, JSString.valueOf("RemoteCollector got frontend message in " + time + "ms"));
    lastFrontendMessage = message;
    if (sendResult != null) sendMessage();
  }

  public void applyDiff(int[] path, boolean left) {
    var model = (ItemFolderDiffModel) root.findNode(path);
    int diffType = model.getDiffType();
    if (diffType == DiffTypes.DEFAULT) return;

    boolean isDeleteDiff = (left && diffType == DiffTypes.INSERTED) || (!left && diffType == DiffTypes.DELETED);
    boolean isInsertDiff = (left && diffType == DiffTypes.DELETED) || (!left && diffType == DiffTypes.INSERTED);

    ArrayWriter pathWriter = new ArrayWriter();
    lastFrontendMessage.collectPath(path, pathWriter, root, left);

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
    if (model.isFile()) {
      if (!isDeleteDiff) copyFile(model, left);
      else removeFile(model);
    } else {
      if (!isDeleteDiff) copyFolder(model, left);
      else removeFolder(model);
    }
  }

  private void copyFile(ItemFolderDiffModel model, boolean left) {
    if (!(model.item() instanceof FileHandle fileItem)) return;
    String to = model.getFullPath(!left ? leftHandle().getFullPath() : rightHandle().getFullPath());
    fileItem.copyTo(to, this::sendApplied, this::onError);
  }

  private void removeFile(ItemFolderDiffModel model) {
    if (!(model.item() instanceof FileHandle fileItem)) return;
    fileItem.remove(this::sendApplied, this::onError);
  }

  private void copyFolder(ItemFolderDiffModel model, boolean left) {
    if (!(model.item() instanceof DirectoryHandle dirItem)) return;
    String to = model.getFullPath(!left ? leftHandle().getFullPath() : rightHandle().getFullPath());
    dirItem.copyTo(to, this::sendApplied, this::onError);
  }

  private void removeFolder(ItemFolderDiffModel model) {
    if (!(model.item() instanceof DirectoryHandle dirItem)) return;
    dirItem.remove(this::sendApplied, this::onError);
  }

  private void onError(String error) {
    LoggingJs.Static.logger.log(LoggingJs.ERROR, JSString.valueOf(error));
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
    model.children = new ItemFolderDiffModel[len];
    model.childrenComparedCnt = 0;

    int lP = 0, rP = 0;
    int mP = 0;
    boolean edited = false;

    while (mP < len) {
      int kind = kinds[mP];
      if (diffs[mP] == DiffTypes.DELETED) {
        edited = true;
        model.children[mP] = new ItemFolderDiffModel(model, leftItem[lP].getName());
        model.child(mP).posInParent = mP;
        model.child(mP).setItemKind(kind);
        model.child(mP).setDiffType(DiffTypes.DELETED);
        model.child(mP).items = new FsItem[]{leftItem[lP]};
        read(model.child(mP));
        mP++;
        lP++;
      } else if (diffs[mP] == DiffTypes.INSERTED) {
        edited = true;
        model.children[mP] = new ItemFolderDiffModel(model, rightItem[rP].getName());
        model.child(mP).posInParent = mP;
        model.child(mP).setItemKind(kind);
        model.child(mP).setDiffType(DiffTypes.INSERTED);
        model.child(mP).items = new FsItem[]{rightItem[rP]};
        read(model.child(mP));
        mP++;
        rP++;
      } else {
        model.children[mP] = new ItemFolderDiffModel(model, leftItem[lP].getName());
        model.child(mP).posInParent = mP;
        model.child(mP).setItemKind(kind);
        model.child(mP).items = new FsItem[]{leftItem[lP], rightItem[rP]};
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
    boolean equals = ArgsCast.intArray(result, 0)[0] == 1;
    onFilesCompared(model, equals);
  }

  private void onFilesCompared(
      ItemFolderDiffModel model,
      boolean equals
  ) {
    filesCompared++;
    if (!equals) model.markUp(DiffTypes.EDITED);
    model.itemCompared();
    onItemCompared();
  }

  private void read(ItemFolderDiffModel model) {
    if (model.item() instanceof DirectoryHandle dirHandle) readFolder(model, dirHandle);
    else model.itemCompared();
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
    int[] sizes = ArgsCast.intArray(result, 1);
    String[] paths = new String[sizes[0]];
    FsItem[] fsItems = new FsItem[sizes[1]];
    for (int i = 0; i < paths.length; i++) paths[i] = (String) result[i + 2];
    for (int i = 0; i < fsItems.length; i++) fsItems[i] = (FsItem) result[sizes[0] + i + 2];
    var updModel = ItemFolderDiffModel.fromInts(ints, paths, fsItems);
    model.update(updModel);
    onItemCompared();
  }

  private void onItemCompared() {
    if (--inComparing < 0) throw new IllegalStateException("inComparing cannot be negative");
    if (--sentToWorker < 0) throw new IllegalStateException("inComparing cannot be negative");
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
    send(sendResult, lastFrontendMessage);

    this.lastMessageSentTime = Performance.now();
    if (lastFilesCompared == filesCompared && lastFoldersCompared == foldersCompared) return;
    String progressMsg = "Sent message in " + Numbers.iRnd(lastMessageSentTime - startTime) + "ms, " +
        "foldersCompared: " + foldersCompared + ", filesCompared: " + filesCompared;
    LoggingJs.Static.logger.log(LoggingJs.TRACE, JSString.valueOf(progressMsg));
    lastFilesCompared = filesCompared;
    lastFoldersCompared = foldersCompared;
  }

  private void onComplete() {
    if (onComplete == null) return;
    lastMessageSent = true;
    send(onComplete, null);

    this.lastMessageSentTime = Performance.now();
    String completeMsg = leftHandle().getName() + " ↔ " + rightHandle().getName() +
        " - finished in " + Numbers.iRnd(lastMessageSentTime - startTime) + "ms, " +
        "foldersCompared: " + foldersCompared + ", filesCompared: " + filesCompared;
    LoggingJs.Static.logger.log(LoggingJs.INFO, JSString.valueOf(completeMsg));
  }

  private void send(Consumer<JsArray<JSObject>> send, FrontendMessage message) {
    LoggingJs.Static.logger.log(LoggingJs.TRACE, JSString.valueOf("inComparing: " + inComparing));
    String leftRootName = leftHandle().getFullPath();
    String rightRootName = rightHandle().getFullPath();
    var jsArray = BackendMessage.serialize(root, message, leftRootName, rightRootName);
    jsArray.push(DiffModelChannelUpdater.FRONTEND_MESSAGE_ARRAY);
    send.accept(jsArray);
  }

  private void sendApplied() {
    LoggingJs.Static.logger.log(LoggingJs.DEBUG, JSString.valueOf("Send applied model"));
    if (sendResult == null) return;
    String leftRootName = leftHandle().getFullPath();
    String rightRootName = rightHandle().getFullPath();
    var backendMessage = lastMessageSent ? null : lastFrontendMessage;
    var jsArray = BackendMessage.serialize(root, backendMessage, leftRootName, rightRootName);
    jsArray.push(DiffModelChannelUpdater.APPLY_DIFF_ARRAY);
    sendResult.accept(jsArray);
  }

  private double getTimeDelta() {
    return Performance.now() - lastMessageSentTime;
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
}
