package org.sudu.experiments.esm.dlg;

import org.sudu.experiments.diff.DiffTypes;
import org.sudu.experiments.diff.folder.RemoteFolderDiffModel;
import org.sudu.experiments.esm.JsDialogProvider;
import org.sudu.experiments.js.JsArray;
import org.sudu.experiments.js.JsHelper;

import java.util.function.BiConsumer;

public class FsDialogs {
  public static void showDlg(
      JsDialogProvider p,
      String from, String to,
      RemoteFolderDiffModel remoteModel,
      boolean left,
      // true, _ -> Remove orphan files and folders
      // _, true -> Sync excluded files and folders
      BiConsumer<Boolean, Boolean> action
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
    final JsDialogOption syncOrphanOption;
    if (!isDelete && remoteModel.isDir()) {
      syncOrphanOption = JsNative.createOption("Remove orphan files and folders", false);
      options.push(syncOrphanOption);
    } else syncOrphanOption = null;
    final JsDialogOption syncExcludedOption;
    if (!isDelete && (remoteModel.containExcluded() && !remoteModel.isExcluded())) {
      syncExcludedOption = JsNative.createOption("Sync excluded files and folders", false);
      options.push(syncExcludedOption);
    } else syncExcludedOption = null;

    var i = JsNative.createInput(
        title, text,
        options,
        JsHelper.toJsArray(bOk, bCancel)
    );
    p.showModalDialog(i).then(
        result -> {
          if (result != null && result.getButton() == bOk) {
            var selected = p.getSelectedOptions();
            final boolean syncOrphan = syncOrphanOption == null || selected.has(syncOrphanOption);
            final boolean syncExcluded = syncOrphanOption == null || selected.has(syncExcludedOption);
            action.accept(syncOrphan, syncExcluded);
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
