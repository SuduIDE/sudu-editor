package org.sudu.experiments;

import org.sudu.experiments.diff.*;
import org.sudu.experiments.esm.*;
import org.sudu.experiments.js.Promise;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;

// see ES6moduleExport.template.js.1
// see editor.d.ts

public interface Editor_d_ts {

  @JSFunctor interface EditorFactory extends JSObject {
    Promise<JsIEditorView> create(EditArgs args);

    class Setter {
      @JSBody(params = {"f"}, script = "newEditor = f;")
      public static native void setApi(EditorFactory f);
    }
  }

  @JSFunctor interface FileDiffFactory extends JSObject {
    Promise<JsFileDiffView> create(EditArgs args);

    class Setter {
      @JSBody(params = {"f"}, script = "newFileDiff = f;")
      public static native void setDiff(FileDiffFactory f);
    }
  }

  @JSFunctor interface FolderDiffFactory extends JSObject {
    Promise<JsIFolderDiffView> create(EditArgs args);

    class Setter {
      @JSBody(params = {"f"}, script = "newFolderDiff = f;")
      public static native void set(FolderDiffFactory f);
    }
  }

  @JSFunctor interface RemoteFolderDiffFactory extends JSObject {
    Promise<JsRemoteFolderDiffView> create(EditArgs args, Channel channel);

    class Setter {
      @JSBody(params = {"f"}, script = "newRemoteFolderDiff = f;")
      public static native void set(RemoteFolderDiffFactory f);
    }
  }

  @JSFunctor interface RemoteFileDiffFactory extends JSObject {
    Promise<JsRemoteFileDiffView> create(EditArgs args, Channel channel);

    class Setter {
      @JSBody(params = {"f"}, script = "newRemoteFileDiff = f;")
      public static native void set(RemoteFileDiffFactory f);
    }
  }

  @JSFunctor interface RemoteBinaryDiffFactory extends JSObject {
    Promise<JsRemoteBinaryDiff> create(EditArgs args, Channel channel);

    class Setter {
      @JSBody(params = {"f"}, script = "newRemoteBinaryDiff = f;")
      public static native void set(RemoteBinaryDiffFactory f);
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
    JsTextModel.Api.install();
    EditorFactory.Setter.setApi(JsCodeEditor::newEdit);
    FileDiffFactory.Setter.setDiff(JsCodeDiff::newDiff);
    RemoteFileDiffFactory.Setter.set(JsRemoteCodeDiff::create);
    RemoteBinaryDiffFactory.Setter.set(JsRemoteBinaryDiff::create);
    FolderDiffFactory.Setter.set(JsFolderDiff::newDiff);
    RemoteFolderDiffFactory.Setter.set(JsRemoteFolderDiff::newDiff);
    RemoteEditorFactory.Setter.set(JsRemoteEditor::create);

    ChannelTest.publishChannelTest();
  }
}
