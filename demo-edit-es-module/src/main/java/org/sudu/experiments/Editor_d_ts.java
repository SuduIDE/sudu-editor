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

public interface Editor_d_ts {

  @JSFunctor interface EditorFactory extends JSObject {
    Promise<JsIEditorView> create(EditArgs args);

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

  @JSFunctor interface CodeDiffFactory extends JSObject {
    Promise<JsFileDiffView> create(EditArgs args);

    class Setter {
      @JSBody(params = {"f"}, script = "diffFactory = f;")
      public static native void setDiff(CodeDiffFactory f);
    }
  }

  @JSFunctor interface FolderDiffFactory extends JSObject {
    Promise<JsIFolderDiffView> create(EditArgs args);

    class Setter {
      @JSBody(params = {"f"}, script = "newFolderDiffView = f;")
      public static native void set(FolderDiffFactory f);
    }
  }

  @JSFunctor interface RemoteFolderDiffFactory extends JSObject {
    Promise<JsRemoteFolderDiffView> create(EditArgs args, Channel channel);

    class Setter {
      @JSBody(params = {"f"}, script = "newRemoteFolderDiffView = f;")
      public static native void set(RemoteFolderDiffFactory f);
    }
  }

  @JSFunctor interface RemoteCodeDiffFactory extends JSObject {
    Promise<JsRemoteFileDiffView> create(EditArgs args, Channel channel);

    class Setter {
      @JSBody(params = {"f"}, script = "newRemoteCodeDiff = f;")
      public static native void set(RemoteCodeDiffFactory f);
    }
  }

  @JSFunctor interface RemoteEditorFactory extends JSObject {
    Promise<JsRemoteEditorView> create(EditArgs args, Channel channel);

    class Setter {
      @JSBody(params = {"f"}, script = "newRemoteEditor = f;")
      public static native void set(RemoteEditorFactory f);
    }
  }

  static void main(String[] args) {
    LoggingJs.Setter.set();
    EditorFactory.Setter.setApi(JsCodeEditor::newEdit);
    TextModelFactory.Setter.setModel(JsTextModel::new);
    CodeDiffFactory.Setter.setDiff(JsCodeDiff::newDiff);
    RemoteCodeDiffFactory.Setter.set(JsRemoteCodeDiff::create);
    FolderDiffFactory.Setter.set(JsFolderDiff::newDiff);
    RemoteFolderDiffFactory.Setter.set(JsRemoteFolderDiff::newDiff);
    RemoteEditorFactory.Setter.set(JsRemoteEditor::create);

    ChannelTest.publishChannelTest();
  }
}
