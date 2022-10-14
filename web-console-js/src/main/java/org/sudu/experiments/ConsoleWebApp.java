package org.sudu.experiments;

import org.sudu.experiments.js.JsHelper;
import org.teavm.jso.ajax.XMLHttpRequest;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLInputElement;

public class ConsoleWebApp {

  public static void main(String[] args) {
    HTMLInputElement inputLine = HTMLDocument.current().getElementById("inputLine").cast();

    inputLine.focus();

    inputLine.addEventListener("change", e -> {
      String value = inputLine.getValue();
      e.stopPropagation();
      e.preventDefault();
      addText("input event :" + value);
      inputLine.setValue("");
      inputLine.scrollIntoView();
      if ("fetch".equals(value)) {
        fetch();
      }
    });
  }

  static void fetch() {
    XMLHttpRequest request = XMLHttpRequest.create();
    request.open("GET", "https://github.com/");
    request.setRequestHeader("Access-Control-Request-Headers", "access-control-allow-origin");
    request.setRequestHeader("Access-Control-Allow-Origin", "*");
    request.setOnReadyStateChange(() -> {
      int readyState = request.getReadyState();
      addText("request.getReadyState() " + readyState);
      String headers = request.getAllResponseHeaders();
      addText(headers);
    });
    request.send();
  }

  private static void addText(String s) {
    JsHelper.addPreText("panelDiv", s);
  }
}
