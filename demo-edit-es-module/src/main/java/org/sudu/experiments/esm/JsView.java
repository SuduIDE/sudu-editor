package org.sudu.experiments.esm;

import org.teavm.jso.core.JSString;


// View Focusable Disposable HasTheme
public interface JsView extends JsHasTheme {
  void dispose();
  void focus();
  void disconnectFromDom();
  void reconnectToDom(JSString containedId);
}
