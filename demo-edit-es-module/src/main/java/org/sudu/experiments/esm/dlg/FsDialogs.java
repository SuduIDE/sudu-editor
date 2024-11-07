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
    var bOk = JsNative.createButton("Confirm", true);
    var bCancel = JsNative.createButton("Cancel", false);

    int diffType = remoteModel.getDiffType();
    boolean isDelete = (left && diffType == DiffTypes.INSERTED) || (!left && diffType == DiffTypes.DELETED);

    String modelType = remoteModel.isFile() ? "File " : "Folder ";
    String title =
      (isDelete ? "Delete " : "Copy ") +
      modelType;

    String text = isDelete
        ? modelType + from
        : "From\n" + from + "\n\nTo\n" + to;

    var i = JsNative.createInput(
        title, text,
        JsArray.create(),
        JsHelper.toJsArray(bOk, bCancel)
    );
    p.showModalDialog(i).then(
        result -> {
          if (result != null && result.getButton() == bOk)
            action.run();
        }, e -> {}
    );
  }
}
