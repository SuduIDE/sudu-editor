package org.sudu.experiments.esm.dlg;

import org.sudu.experiments.BooleanConsumer;
import org.sudu.experiments.diff.DiffTypes;
import org.sudu.experiments.diff.folder.RemoteFolderDiffModel;
import org.sudu.experiments.esm.JsDialogProvider;
import org.sudu.experiments.js.JsArray;
import org.sudu.experiments.js.JsHelper;

import java.util.Arrays;

import static org.sudu.experiments.diff.RemoteFileDiffWindow.sSuffix;

public class FsDialogs {
  public static void showDlg(
      JsDialogProvider p,
      String from, String to,
      RemoteFolderDiffModel remoteModel,
      boolean left,
      BooleanConsumer action  // true -> Remove orphan files and folders
  ) {
    var bOk = JsNative.createButton("Confirm", true);
    var bCancel = JsNative.createButton("Cancel", false);

    int diffType = remoteModel.getDiffType();
    boolean isDelete = left ? diffType == DiffTypes.INSERTED
        : diffType == DiffTypes.DELETED;

    String modelType = remoteModel.isFile() ? "File " : "Folder ";
    String title = (isDelete ? "Delete " : "Copy ") + modelType;

    String text = isDelete
        ? modelType + to
        : "From\n" + from + "\n\nTo\n" + to;

    JsArray<JsDialogOption> options = JsArray.create();
    final JsDialogOption syncOption;
    if (!isDelete && remoteModel.isDir()) {
      syncOption = JsNative.createOption("Remove orphan files and folders", true);
      options.set(0, syncOption);
    } else syncOption = null;

    var i = JsNative.createInput(
        title, text,
        options,
        JsHelper.toJsArray(bOk, bCancel)
    );
    p.showModalDialog(i).then(
        result -> {
          if (result != null && result.getButton() == bOk) {
            var selected = p.getSelectedOptions();
            action.accept(syncOption == null || selected.has(syncOption));
          }
        }, e -> {}
    );
  }

  public static void showDlg(JsDialogProvider p, String msg) {
    var bOk = JsNative.createButton("OK", true);

    var i = JsNative.createInput(
        "File sync completed", msg,
        JsArray.create(),
        JsHelper.toJsArray(bOk)
    );
    p.showModalDialog(i).then(_1 -> {}, _1 -> {});
  }
}
