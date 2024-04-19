package org.sudu.experiments;

import org.sudu.experiments.diff.JsCodeDiff0;
import org.sudu.experiments.diff.JsFolderDiff0;
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
    Promise<JsCodeEditor> create(EditArgs args);

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

  @JSFunctor interface DiffFactory extends JSObject {
    Promise<JsCodeDiff> create(EditArgs args);

    class Setter {
      @JSBody(params = {"f"}, script = "diffFactory = f;")
      public static native void setDiff(DiffFactory f);
    }
  }

  @JSFunctor interface FolderDiffFactory extends JSObject {
    Promise<JsFolderDiff> create(EditArgs args);

    class Setter {
      @JSBody(params = {"f"}, script = "newFolderDiffView = f;")
      public static native void set(FolderDiffFactory f);
    }
  }

  static void main(String[] args) {
    EditorFactory.Setter.setApi(JsCodeEditor0::newEdit);
    TextModelFactory.Setter.setModel(JsTextModel::new);
    DiffFactory.Setter.setDiff(JsCodeDiff0::newDiff);
    FolderDiffFactory.Setter.set(JsFolderDiff0::newDiff);
  }
}
