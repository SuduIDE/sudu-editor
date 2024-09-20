package org.sudu.experiments.diff;

import org.sudu.experiments.js.JsFunctions;
import org.teavm.interop.NoSideEffects;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSString;

public interface JsFolderDiffSelection extends JsDiffSelection {
  @JSProperty
  JSString getRelativePath();

  @JSProperty
  boolean getIsLeft();

  @JSProperty
  boolean getIsFolder();

  @JSProperty
  boolean getIsOrphan();

  class H {
    @JSBody(
        params = {"path", "isLeft", "isFolder", "isOrphan"},
        script = "return { relativePath:path, isLeft:isLeft, isFolder:isFolder, isOrphan:isOrphan };"
    )
    @NoSideEffects
    public static native JsFolderDiffSelection create(
        JSString path, boolean isLeft, boolean isFolder, boolean isOrphan
    );

    public static JsFolderDiffSelection create(
        FolderDiffRootView.Selection s
    ) {
      return s != null
          ? create(JSString.valueOf(s.path), s.isLeft, s.isFolder, s.isOrphan)
          : null;
    }

    static FolderDiffRootView.SelectionListener toJava(
        JsFunctions.Consumer<JsFolderDiffSelection> callback
    ) {
      return s -> callback.f(create(s));
    }
  }
}
