package org.sudu.experiments.esm.dlg;

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
      Runnable action
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

  public static void showDlg(JsDialogProvider p, int[] ints) {
    var bOk = JsNative.createButton("OK", true);
    StringBuilder textSB = new StringBuilder();
    if (ints[0] != 0) textSB.append(sSuffix(ints[0], "dir")).append(" copied\n");
    if (ints[1] != 0) textSB.append(sSuffix(ints[1], "file")).append(" copied\n");
    if (ints[2] != 0) textSB.append(sSuffix(ints[2], "dir")).append(" deleted\n");
    if (ints[3] != 0) textSB.append(sSuffix(ints[3], "file")).append(" deleted\n");
    if (Arrays.equals(ints, new int[] {0, 0, 0, 0})) textSB.append("No differences\n");

    var i = JsNative.createInput(
        "File sync completed", textSB.toString(),
        JsArray.create(),
        JsHelper.toJsArray(bOk)
    );
    p.showModalDialog(i).then(_1 -> {}, _1 -> {});
  }
}
