package org.sudu.experiments;

import org.sudu.experiments.diff.folder.RemoteFolderDiffModel;
import org.sudu.experiments.js.*;
import org.sudu.experiments.js.node.Fs;
import org.sudu.experiments.js.node.NodeDirectoryHandle;
import org.sudu.experiments.update.DiffModelChannelUpdater;
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
  public AsyncShutdown startFolderDiff(JSString leftPath, JSString rightPath, Channel channel) {
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

    RemoteFolderDiffModel root = new RemoteFolderDiffModel(null, "");

    DiffModelChannelUpdater updater = new DiffModelChannelUpdater(
        root,
        leftHandle, rightHandle,
        scanFileContent,
        pool, channel
    );
    updater.beginCompare();
    return () -> Promise.create((ok, fail) ->
        updater.shutdown(() -> ok.f(null))
    );
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
