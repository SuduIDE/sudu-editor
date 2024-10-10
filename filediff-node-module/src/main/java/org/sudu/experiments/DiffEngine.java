package org.sudu.experiments;

import org.sudu.experiments.js.*;
import org.sudu.experiments.js.node.Fs;
import org.sudu.experiments.js.node.NodeDirectoryHandle;
import org.sudu.experiments.js.node.NodeFileHandle;
import org.sudu.experiments.update.DiffModelChannelUpdater;
import org.sudu.experiments.diff.folder.ItemFolderDiffModel;
import org.sudu.experiments.update.FileDiffChannelUpdater;
import org.sudu.experiments.update.FileEditChannelUpdater;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

public class DiffEngine implements DiffEngineJs {
  final NodeWorkersPool pool;

  DiffEngine(JsArray<NodeWorker> worker) {
    pool = new NodeWorkersPool(worker);
  }

  @Override
  public void dispose() {
    pool.terminateAll();
  }

  @Override
  public JsFolderDiffSession startFolderDiff(JSString leftPath, JSString rightPath, Channel channel) {
    JsHelper.consoleInfo("Starting folder diff ");
    boolean scanFileContent = true;

    if (notDir(leftPath))
      throw new IllegalArgumentException("Left path " + leftPath.stringValue() + " should be directory");
    if (notDir(rightPath))
      throw new IllegalArgumentException("Right path " + rightPath.stringValue() + " should be directory");

    JsHelper.consoleInfo("DiffEngine LeftPath: ", leftPath);
    JsHelper.consoleInfo("DiffEngine RightPath: ", rightPath);

    DirectoryHandle leftHandle = new NodeDirectoryHandle(leftPath);
    DirectoryHandle rightHandle = new NodeDirectoryHandle(rightPath);

    ItemFolderDiffModel root = new ItemFolderDiffModel(null, "");
    root.setItems(leftHandle, rightHandle);

    DiffModelChannelUpdater updater = new DiffModelChannelUpdater(
        root,
        scanFileContent,
        pool, channel
    );
    updater.beginCompare();
    return new JsFolderDiffSession0(updater);
  }

  public JsFileDiffSession startFileDiff(
      JSObject leftInput, JSObject rightInput,
      Channel channel,
      JsFolderDiffSession parent
  ) {
    LoggingJs.info("Starting new file diff ...");

    boolean isLeftFile = JsFileInputFile.isInstance(leftInput);
    boolean isRightFile = JsFileInputFile.isInstance(rightInput);
    boolean isLeftText = JsFileInputContent.isInstance(leftInput);
    boolean isRightText = JsFileInputContent.isInstance(rightInput);
    boolean validatedLeft = isLeftFile ^ isLeftText;
    boolean validatedRight = isRightFile ^ isRightText;

    if (!validatedLeft)
      LoggingJs.error(JsHelper.concat(
          "startFileDiff: left input is invalid ", leftInput));

    if (!validatedRight)
      LoggingJs.error(JsHelper.concat(
          "startFileDiff: right input is invalid ", rightInput));

    if (!validatedLeft || !validatedRight)
      return null;

    JSString leftStr = isLeftFile
        ? JsFileInputFile.getPath(leftInput)
        : JsFileInputContent.getContent(leftInput);

    JSString rightStr = isRightFile
        ? JsFileInputFile.getPath(rightInput)
        : JsFileInputContent.getContent(rightInput);

    LoggingJs.info(JsHelper.concat("  left: ", leftStr));
    LoggingJs.info(JsHelper.concat("  right: ",rightStr));

    LoggingJs.info("  parent instanceof JsFolderDiffSession0: " +
        (parent instanceof JsFolderDiffSession0));

    DiffModelChannelUpdater parentUpdater =
        JsHelper.jsIf(parent) ? ((JsFolderDiffSession0) parent).updater : null;
    FileDiffChannelUpdater updater
        = new FileDiffChannelUpdater(channel, parentUpdater, pool);
    if (isLeftFile && isRightFile) {
      FileHandle leftHandle = new NodeFileHandle(leftStr);
      FileHandle rightHandle = new NodeFileHandle(rightStr);
      updater.beginCompare(leftHandle, rightHandle);
    } else {
      updater.sendFileRead(true, leftStr);
      updater.sendFileRead(false, rightStr);
    }
    return new JsFileDiffSession0();
  }

  @Override
  public JsFileDiffSession startFileEdit(JSString input, Channel channel) {
    JsHelper.consoleInfo("Starting file edit ...");

    boolean isFile = JsFileInputFile.isInstance(input);

    JSString str = isFile
        ? JsFileInputFile.getPath(input)
        : JsFileInputContent.getContent(input);

    JsHelper.consoleInfo("  input: ", str);

    FileEditChannelUpdater updater = new FileEditChannelUpdater(channel);
    if (isFile) {
      FileHandle handle = new NodeFileHandle(str);
      updater.beginCompare(handle);
    } else {
      updater.sendMessage(str);
    }
    return new JsFileDiffSession0();
  }

  static boolean notDir(JSString path) {
    if (!isDir(path)) {
      JsHelper.consoleError("path is not a directory ", path);
      return true;
    }
    return false;
  }

  static boolean isDir(JSString path) {
    return Fs.isDirectory(path);
  }

  static boolean notFile(JSString path) {
    return !Fs.isFile(path);
  }

  @Override
  public JsDiffTestApi testApi() {
    return new DiffTestApi(pool);
  }
}
