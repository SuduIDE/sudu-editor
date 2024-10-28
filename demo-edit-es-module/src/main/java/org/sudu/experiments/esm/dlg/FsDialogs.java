package org.sudu.experiments.esm.dlg;

import org.sudu.experiments.diff.DiffTypes;
import org.sudu.experiments.diff.folder.RemoteFolderDiffModel;
import org.sudu.experiments.esm.JsDialogProvider;
import org.sudu.experiments.js.JsArray;
import org.sudu.experiments.js.JsHelper;

public class FsDialogs {
  public static void showDlg(
      JsDialogProvider p,
      String from, String to,
      RemoteFolderDiffModel remoteModel,
      boolean left,
      Runnable action
  ) {
    var bOk = JsNative.createButton("OK", true);
    var bCancel = JsNative.createButton("cancel", false);

    int diffType = remoteModel.getDiffType();
    boolean isDelete = (left && diffType == DiffTypes.INSERTED) || (!left && diffType == DiffTypes.DELETED);

    String modelType = (remoteModel.isFile() ? "file " : "folder ");
    String title = "confirm " +
        modelType +
        (isDelete ? "delete " : "copy ") +
        "operation";
    String text = isDelete
        ? modelType + from
        : "from " + modelType + from + " to " + to;

    var i = JsNative.createInput(
        title, text,
        JsArray.create(),
        JsHelper.toJsArray(bOk, bCancel)
    );
    p.showModalDialog(i).then(
        result -> {
          if (result.getButton() == bOk)
            action.run();
        }, e -> {}
    );
  }
}
