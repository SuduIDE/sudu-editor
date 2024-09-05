package org.sudu.experiments.diff;

import org.sudu.experiments.js.JsFunctions;
import org.teavm.interop.NoSideEffects;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSString;

public interface JsFolderDiffViewSelection extends JSObject {
  @JSProperty JSString getRelativePath();
  @JSProperty boolean getIsLeft();
  @JSProperty boolean getIsFolder();

  class H {
    @JSBody(
        params = {"path", "isLeft", "isFolder"},
        script = "return { relativePath:path, isLeft:isLeft, isFolder:isFolder };"
    )
    @NoSideEffects
    public static native JsFolderDiffViewSelection create(
        JSString path, boolean isLeft, boolean isFolder
    );

    public static JsFolderDiffViewSelection create(
        FolderDiffRootView.Selection s
    ) {
      return s != null
          ? create(JSString.valueOf(s.path), s.isLeft, s.isFolder)
          : null;
    }

    static FolderDiffRootView.SelectionListener toJava(
        JsFunctions.Consumer<JsFolderDiffViewSelection> callback
    ) {
      return s -> callback.f(create(s));
    }
  }
}
