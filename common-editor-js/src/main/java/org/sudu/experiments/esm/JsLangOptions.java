package org.sudu.experiments.esm;

import org.sudu.experiments.editor.worker.parser.LangOptions;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

public class JsLangOptions implements JsILangOptions {

  @JSFunctor
  public interface Api extends JSObject {
    JsILangOptions create();
    class Setter {
      @JSBody(params = {"f"}, script = "newLangOptions = f;")
      static native void set(Api f);
    }
    static void install() {
      Setter.set(JsLangOptions::new);
    }
  }

  public final LangOptions javaLangOptions;

  public JsLangOptions() {
    javaLangOptions = new LangOptions();
  }

  public boolean isEnabled(JSString lang) {
    return javaLangOptions.isEnabled(lang.stringValue());
  }

  public void enable(JSString lang) {
    javaLangOptions.enable(lang.stringValue());
  }

  public void disable(JSString lang) {
    javaLangOptions.disable(lang.stringValue());
  }

  public void enableAll() {
    javaLangOptions.enableAll();
  }

  public void disableAll() {
    javaLangOptions.disableAll();
  }
}
