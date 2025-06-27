package org.sudu.experiments;

import org.sudu.experiments.editor.worker.FsWorkerJobs;
import org.sudu.experiments.editor.worker.diff.DiffUtils;
import org.sudu.experiments.js.*;
import org.sudu.experiments.js.node.*;
import org.sudu.experiments.update.DiffModelChannelUpdater;
import org.sudu.experiments.diff.folder.ItemFolderDiffModel;
import org.sudu.experiments.update.FileDiffChannelUpdater;
import org.sudu.experiments.update.FileEditChannelUpdater;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSError;
import org.teavm.jso.core.JSString;

import java.util.function.Consumer;

public class DiffEngine implements DiffEngineJs {
  public static final boolean debug = false;

  final NodeWorkersPool pool;

  DiffEngine(JsArray<NodeWorker> worker) {
    pool = new NodeWorkersPool(worker);
  }

  @Override
  public void dispose() {
    JsHelper.consoleInfo("DiffEngine.dispose");
    pool.terminateAll();
    SshPool.terminate();
  }

  @Override
  public JsFolderDiffSession startFolderDiff(
      JsFolderInput leftPath, JsFolderInput rightPath,
      Channel channel,
      JSObject excludeList
  ) {
    LoggingJs.info("Starting folder diff");
    boolean scanFileContent = true;
    DirectoryHandle leftDir = JsFolderInput.directoryHandle(leftPath);
    DirectoryHandle rightDir = JsFolderInput.directoryHandle(rightPath);

    boolean singleExclude = JSString.isInstance(excludeList);

    ExcludeList elLeft, elRight;
    if (singleExclude) {
      JSString excludeString = excludeList.cast();
      elLeft = elRight = new ExcludeList(excludeString.stringValue());
      LoggingJs.info(JsHelper.concat("Exclude list: ", excludeString));
    } else {
      JSString excludeLeft = JsHelper.getString(excludeList,
          JSString.valueOf("left"));
      JSString excludeRight = JsHelper.getString(excludeList,
          JSString.valueOf("right"));

      LoggingJs.info(JsHelper.concat("Exclude left: ", excludeLeft));
      LoggingJs.info(JsHelper.concat("Exclude right: ", excludeRight));
      elLeft = excludeLeft != null ?
          new ExcludeList(excludeLeft.stringValue()) : null;
      elRight = excludeRight != null ?
          new ExcludeList(excludeRight.stringValue()) : null;
    }

    if (leftDir == null)
      throw new IllegalArgumentException(
          "illegal leftPath argument " + JsHelper.jsToString(leftPath));
    if (rightDir == null)
      throw new IllegalArgumentException(
          "illegal rightPath argument " + JsHelper.jsToString(rightPath));

    LoggingJs.info("  DiffEngine Left: ".concat(leftDir.toString()));
    LoggingJs.info("  DiffEngine Right: ".concat(rightDir.toString()));

    ItemFolderDiffModel root = new ItemFolderDiffModel(null, "");
    root.setItems(leftDir, rightDir);

    DiffModelChannelUpdater updater = new DiffModelChannelUpdater(
        root,
        scanFileContent,
        pool, channel,
        elLeft, elRight
    );
    updater.beginCompare();
    return new JsFolderDiffSession0(updater);
  }

  public JsFileDiffSession startFileDiff(
      JsFileInput leftInput, JsFileInput rightInput,
      Channel channel,
      JsFolderDiffSession parent
  ) {
    LoggingJs.info("Starting new file diff ...");

    // string === fileName
    boolean isLeftString = JSString.isInstance(leftInput);
    boolean isLeftText = !isLeftString && JsFileInput.isContent(leftInput);
    boolean isLeftFile = !isLeftText && JsFileInput.isPath(leftInput);

    boolean isRightString = JSString.isInstance(rightInput);
    boolean isRightText = !isRightString && JsFileInput.isContent(rightInput);
    boolean isRightFile = !isRightText && JsFileInput.isPath(rightInput);

    FileHandle leftHandle = isLeftFile ?
        JsFileInput.fileHandle(leftInput, true) : null;
    FileHandle rightHandle = isRightFile ?
        JsFileInput.fileHandle(rightInput, true) : null;

    boolean validatedLeft = leftHandle != null || isLeftText;
    boolean validatedRight = rightHandle != null || isRightText;

    if (!validatedLeft)
      LoggingJs.error(JsHelper.concat(
          "startFileDiff: left input is invalid ", leftInput));

    if (!validatedRight)
      LoggingJs.error(JsHelper.concat(
          "startFileDiff: right input is invalid ", rightInput));

    if (!validatedLeft || !validatedRight)
      return null;

    LoggingJs.info("  parent is FolderDiffSession: " +
        (parent instanceof JsFolderDiffSession0));

    DiffModelChannelUpdater parentUpdater =
        JsHelper.jsIf(parent) ? ((JsFolderDiffSession0) parent).updater : null;

    FileDiffChannelUpdater updater
        = new FileDiffChannelUpdater(channel, parentUpdater, pool);

    if (isLeftFile) {
      LoggingJs.info("  left file: ".concat(leftHandle.toString()));
      updater.compareLeft(leftHandle);
    } else {
      JSString leftStr = JsFileInput.getContent(leftInput);
      JSString leftPath = JsFileInput.getPath(leftInput);
      LoggingJs.info("  left is content, length = " + leftStr.getLength() +
          ", path =" + leftPath.stringValue());
      updater.sendFileRead(true, leftStr, null,
          leftPath);
    }

    if (isRightFile) {
      LoggingJs.info("  right file: ".concat(rightHandle.toString()));
      updater.compareRight(rightHandle);
    } else {
      JSString rightStr = JsFileInput.getContent(rightInput);
      JSString rightPath = JsFileInput.getPath(rightInput);
      LoggingJs.info("  right is content, length = " + rightStr.getLength() +
          ", path =" + rightPath.stringValue());
      updater.sendFileRead(false, rightStr, null, rightPath);
    }
    return new JsFileDiffSession0();
  }

  @Override
  public JsFileDiffSession startFileEdit(
      JsFileInput input, Channel channel,
      JsFolderDiffSession parent
  ) {
    JsHelper.consoleInfo("Starting file edit ...");

    // string === fileName
    boolean isString = JSString.isInstance(input);
    boolean isText = !isString && JsFileInput.isContent(input);
    boolean isFile = !isText && JsFileInput.isPath(input);

    FileHandle fileHandle = isFile ?
        JsFileInput.fileHandle(input, true) : null;

    boolean validate = fileHandle != null || isText;
    if (!validate) {
      LoggingJs.error(JsHelper.concat("startFileEdit bad file: ",
          JsHelper.stringify(input)));
      return null;
    }

    FileEditChannelUpdater updater = new FileEditChannelUpdater(channel, pool);
    if (fileHandle != null) {
      LoggingJs.info("  file: ".concat(fileHandle.toString()));
      updater.setFile(fileHandle);
    } else {
      JSString str = JsFileInput.getContent(input);
      JSString path = JsFileInput.getPath(input);
      LoggingJs.info("  content, length = " + str.getLength());
      updater.sendMessage(str, null, path);
    }
    return new JsFileDiffSession0();
  }

  @JSBody(params = { "name", "isFile" },
      script = "return { name:name, isFile:isFile};")
  static native JSObject newFolderListingEntry(String name, boolean isFile);

  @Override
  public Promise<JsArray<JSObject>> listRemoteDirectory(
      JsSshInput sshInput, boolean withFiles
  ) {

    DirectoryHandle dir = JsFolderInput.sshHandle(sshInput);
    if (dir == null) return Promise.reject("bad sshInput");

    return Promise.create((resolve, reject) ->
        pool.sendToWorker(true,
            r -> publishRemoteDirectory(r, withFiles, resolve, reject),
            DiffUtils.asyncListDirectory, dir)
    );
  }

  static void publishRemoteDirectory(
      Object[] packet, boolean withFiles,
      JsFunctions.Consumer<JsArray<JSObject>> resolve,
      JsFunctions.Consumer<JSError> reject
  ) {
    if (packet.length > 0 && packet[0] instanceof String message) {
      reject.f(JsHelper.newError(message));
    } else {
      var array = JsArray.create();
      for (Object entry : packet) {
        if (entry instanceof FsItem item) {
          boolean isFile = entry instanceof FileHandle;
          if (!isFile || withFiles)
            array.push(newFolderListingEntry(item.getName(), isFile));
        } else {
          System.err.println(
              DiffUtils.asyncListDirectory + ": bad data: " + entry);
        }
      }
      resolve.f(array);
    }
  }

  @JSBody(params = {"isDirectory", "isFile", "isSymbolicLink", "size"},
      script = "return {isDirectory:isDirectory,isFile:isFile" +
          ",isSymbolicLink:isSymbolicLink,size:size};")
  static native JSObject exportStats(
      boolean isDirectory, boolean isFile,
      boolean isSymbolicLink, double size);

  static JSObject exportStats(FileHandle.Stats stats) {
    return exportStats(
        stats.isDirectory, stats.isFile,
        stats.isSymbolicLink, stats.size);
  }

  @Override
  public Promise<JSObject> stat(JsFileInput input) {
    FileHandle file = JsFileInput.fileHandle(input, false);
    return file == null ? Promise.reject("bad input") :
        Promise.create((resolve, reject) ->
            FsWorkerJobs.asyncStats(pool, file,
                stats -> resolve.f(exportStats(stats)),
                postReject(reject)));
  }

  @Override
  public Promise<JSString> readFile(JsFileInput input) {
    FileHandle file = JsFileInput.fileHandle(input, false);

    return file == null ? Promise.reject("bad input") :
        Promise.create((resolve, reject) ->
            FsWorkerJobs.readTextFile(pool, file,
                (text, en) -> resolve.f(TextDecoder.decodeUTF16(text)),
                postReject(reject)));
  }

  static Consumer<String> postReject(JsFunctions.Consumer<JSError> reject) {
    return error -> reject.f(JsHelper.newError(error));
  }

  @Override
  public JsDiffTestApi testApi() {
    return debug ? new DiffTestApi(pool) : null;
  }
}
