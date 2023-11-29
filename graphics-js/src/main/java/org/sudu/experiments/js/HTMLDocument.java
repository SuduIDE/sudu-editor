package org.sudu.experiments.js;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.dom.events.EventTarget;
import org.teavm.jso.dom.html.HTMLBodyElement;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.dom.html.HTMLHeadElement;
import org.teavm.jso.dom.xml.Document;

public abstract class HTMLDocument implements JSObject, Document, EventTarget {

  @Override
  public abstract HTMLElement createElement(String tagName);

  @Override
  public abstract HTMLElement getElementById(String elementId);

  @JSProperty
  public abstract HTMLBodyElement getBody();

  @JSProperty
  public abstract HTMLHeadElement getHead();

  @JSProperty
  public abstract HTMLElement getActiveElement();

  @JSProperty
  public abstract void setTitle(String title);

  public static HTMLDocument current() {
    return JsWindow.current().getDocument();
  }
}
