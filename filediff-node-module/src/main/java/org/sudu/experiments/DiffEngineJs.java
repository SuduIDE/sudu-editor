package org.sudu.experiments;

import org.teavm.jso.JSObject;

public interface DiffEngineJs extends JSObject {
  void dispose();

  JsFolderDiffSession startFolderDiff(
      JsFolderInput leftPath, JsFolderInput rightPath,
      Channel channel);

  //     startFileDiff(
  //        leftPath: string, rightPath: string,
  //        channel: Channel,
  //        folderDiff?: FolderDiffSession
  //    ): FileDiffSession;

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

  JsDiffTestApi testApi();
}
