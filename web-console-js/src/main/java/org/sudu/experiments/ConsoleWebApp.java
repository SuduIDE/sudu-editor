package org.sudu.experiments;

import org.sudu.experiments.js.JsHelper;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLInputElement;

public class ConsoleWebApp {

  public static void main(String[] args) {
    HTMLInputElement inputLine = HTMLDocument.current().getElementById("inputLine").cast();

    inputLine.focus();

    inputLine.addEventListener("change", e -> {
      addText("input event :" + inputLine.getValue());
      e.stopPropagation();
      e.preventDefault();
      inputLine.setValue("");
      inputLine.scrollIntoView();
    });
  }

  private static void addText(String s) {
    JsHelper.addPreText("panelDiv", s);
  }
}
