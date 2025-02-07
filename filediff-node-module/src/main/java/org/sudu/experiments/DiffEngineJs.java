package org.sudu.experiments;

import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

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
//      channel: Channel
//  ): FileDiffSession;

  JsFileDiffSession startFileEdit(JSString path, Channel channel);

  JsDiffTestApi testApi();
}
