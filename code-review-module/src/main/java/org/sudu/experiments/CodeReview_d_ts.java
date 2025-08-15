package org.sudu.experiments;

import org.sudu.experiments.diff.*;
import org.sudu.experiments.esm.*;
import org.sudu.experiments.js.Promise;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

// see ES6moduleExport.template.js.1
// see editor.d.ts

public interface CodeReview_d_ts {

  @JSFunctor interface EditorFactory extends JSObject {
    Promise<JsIEditorView> create(EditArgs args);

    class Setter {
      @JSBody(params = {"f"}, script = "newEditor = f;")
      public static native void setApi(EditorFactory f);
    }
  }

  @JSFunctor interface TextModelFactory extends JSObject {
    JsITextModel create(JSString value, JSString language, JsUri uri);

    class Setter {
      @JSBody(params = {"f"}, script = "newTextModel = f;")
      public static native void setModel(TextModelFactory f);
    }
  }

  @JSFunctor interface CodeReviewFactory extends JSObject {
    Promise<JsCodeReviewView> create(EditArgs args);

    class Setter {
      @JSBody(params = {"f"}, script = "newCodeReview = f;")
      public static native void setDiff(CodeReviewFactory f);
    }
  }


  static void main(String[] args) {
//    LoggingJs.Setter.set();
    EditorFactory.Setter.setApi(JsCodeEditor::newEdit);
    TextModelFactory.Setter.setModel(JsTextModel::new);
    CodeReviewFactory.Setter.setDiff(JsCodeReview::newDiff);
  }
}
