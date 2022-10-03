package org.sudu.experiments.js;

public class FireFoxWarning {
  // todo: improve the page about FireFox configuration
  //   add a button to "copy" property name
  //   add a clickable link to "about:config"
  public static void display(String panelDiv) {
    JsHelper.addPreText(
        panelDiv, "This Browser does not support FontMetrics API. \n" +
            "Please enable the API in FireFox settings:\n" +
            "  open about:config and enable dom.textMetrics.fontBoundingBox.enabled");
  }
}
