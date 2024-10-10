package org.sudu.experiments.esm.dlg;

import org.sudu.experiments.esm.JsDialogProvider;
import org.sudu.experiments.js.JsArray;
import org.sudu.experiments.js.JsHelper;

public class FsDialogs {
  public static void showDlg(
      JsDialogProvider p,
      String from, String to,
      Runnable action
  ) {
    var bOk = JsNative.createButton("OK", true);
    var bCancel = JsNative.createButton("cancel", false);
    var i = JsNative.createInput(
        "confirm file copy operation",
        "from file " + from + " to " + to,
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
