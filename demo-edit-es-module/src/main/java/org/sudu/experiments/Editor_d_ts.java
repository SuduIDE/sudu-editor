package org.sudu.experiments;

import org.sudu.experiments.esm.*;
import org.sudu.experiments.js.Promise;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

// see ES6moduleExport.template.js.1
// see editor.d.ts

public interface Editor_d_ts {

  @JSFunctor interface EditorFactory extends JSObject {
    Promise<JsCodeEditor> create(JsCodeEditor.EditArguments args);

    class Setter {
      @JSBody(params = {"f"}, script = "editorFactory = f;")
      public static native void setApi(EditorFactory f);
    }
  }

  @JSFunctor interface TextModelFactory extends JSObject {
    JsITextModel create(JSString value, JSString language, JsUri uri);

    class Setter {
      @JSBody(params = {"f"}, script = "modelFactory = f;")
      public static native void setModel(TextModelFactory f);
    }
  }

  static void main(String[] args) {
    EditorFactory.Setter.setApi(JsCodeEditor0::newEdit);
    TextModelFactory.Setter.setModel(JsTextModel::new);
  }
}
