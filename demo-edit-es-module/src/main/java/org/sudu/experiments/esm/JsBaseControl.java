package org.sudu.experiments.esm;

import org.teavm.jso.core.JSString;

public interface JsBaseControl extends JsThemeTarget  {
  void dispose();

  void focus();

  void setReadonly(boolean flag);

  void disconnectFromDom();
  void reconnectToDom(JSString containedId);
}
