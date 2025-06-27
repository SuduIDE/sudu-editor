package org.sudu.experiments;

import org.sudu.experiments.js.JsArray;
import org.sudu.experiments.js.Promise;
import org.sudu.experiments.js.node.JsSshInput;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

public interface DiffEngineJs extends JSObject {
  void dispose();

  JsFolderDiffSession startFolderDiff(
      JsFolderInput leftPath, JsFolderInput rightPath,
      Channel channel, JSObject excludeList);

  //  startFolderDiff(
  //    leftPath: FolderInput,
  //    rightPath: FolderInput,
  //    channel: Channel,
  //    excludeList: ExcludeList
  //  ): FolderDiffSession;

  JsFileDiffSession startFileDiff(
      JsFileInput leftInput, JsFileInput rightInput,
      Channel channel,
      JsFolderDiffSession parent
  );

//  startFileEdit(
//      path: string,
//      channel: Channel,
//      folderDiff?: FolderDiffSession
//  ): FileDiffSession;

  JsFileDiffSession startFileEdit(
      JsFileInput path, Channel channel,
      JsFolderDiffSession parent
  );

  Promise<JsArray<JSObject>> listRemoteDirectory(
      JsSshInput sshInput, boolean withFiles);

  Promise<JSObject> stat(JsFileInput sshInput);

  Promise<JSString> readFile(JsFileInput file);

  JsDiffTestApi testApi();
}
