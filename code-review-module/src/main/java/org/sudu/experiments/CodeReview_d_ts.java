package org.sudu.experiments;

import org.sudu.experiments.diff.*;
import org.sudu.experiments.esm.*;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;

// see ES6moduleExport.template.js.1
// see codereview.d.ts

public interface CodeReview_d_ts {

  @JSFunctor interface EditorFactory extends JSObject {
    JsIEditorView create(EditArgs args);

    class Setter {
      @JSBody(params = {"f"}, script = "newEditor = f;")
      public static native void setApi(EditorFactory f);
    }
  }

  @JSFunctor interface CodeReviewFactory extends JSObject {
    JsCodeReviewView create(EditArgs args);

    class Setter {
      @JSBody(params = {"f"}, script = "newCodeReview = f;")
      public static native void setDiff(CodeReviewFactory f);
    }
  }

  static void main(String[] args) {
//    LoggingJs.Setter.set();
    JsTextModel.Api.install();
    JsLoadFonts.install();
    JsWorkerPool.install();
    EditorFactory.Setter.setApi(JsCodeEditor::newEdit);
    CodeReviewFactory.Setter.setDiff(JsCodeReview::newCodeReview);
  }
}
