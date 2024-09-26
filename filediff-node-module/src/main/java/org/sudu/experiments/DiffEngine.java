package org.sudu.experiments;

import org.sudu.experiments.js.*;
import org.sudu.experiments.js.node.Fs;
import org.sudu.experiments.js.node.NodeDirectoryHandle;
import org.sudu.experiments.update.DiffModelChannelUpdater;
import org.sudu.experiments.diff.folder.ItemFolderDiffModel;
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
    root.items = new FsItem[]{leftHandle, rightHandle};

    DiffModelChannelUpdater updater = new DiffModelChannelUpdater(
        root,
        scanFileContent,
        pool, channel
    );
    updater.beginCompare();
    return new JsFolderDiffSession0(updater);
  }

  @Override
  public JsFileDiffSession startFileDiff(
      JSString leftPath, JSString rightPath,
      Channel channel,
      JsFolderDiffSession parent
  ) {
    JsHelper.consoleInfo("Starting new file diff ...");
    JsHelper.consoleInfo("  LeftPath: ", leftPath);
    JsHelper.consoleInfo("  RightPath: ", rightPath);
    return new JsFileDiffSession0();
  }

  @Override
  public JsFileDiffSession startFileEdit(JSString path, Channel channel) {
    JsHelper.consoleInfo("Starting file edit ...");
    JsHelper.consoleInfo("  path: ", path);
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

  @Override
  public JsDiffTestApi testApi() {
    return new DiffTestApi(pool);
  }
}
