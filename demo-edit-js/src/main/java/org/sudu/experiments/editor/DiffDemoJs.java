package org.sudu.experiments.editor;

import org.sudu.experiments.SceneApi;
import org.sudu.experiments.SplitInfo;
import org.sudu.experiments.fonts.Fonts;
import org.sudu.experiments.js.Fetch;
import org.sudu.experiments.js.JsHelper;
import org.sudu.experiments.js.SplitJsText;
import org.teavm.jso.core.JSString;

import java.util.function.Consumer;

public class DiffDemoJs extends Diff0 {
  public DiffDemoJs(SceneApi api) {
    super(api);
    setReadonly(true);
    load(this::setLeftModel, "ClassL.java");
    load(this::setRightModel, "ClassR.java");
  }

  @Override
  protected String[] menuFonts() {
    return Fonts.editorFonts(true);
  }

  void load(Consumer<Model> editor, String path) {
    Fetch.fetch(path)
        .then(Fetch.Response::text)
        .then(text -> loadText(editor, path, text), JsHelper::onError);
  }

  static void loadText(Consumer<Model> editor, String path, JSString text) {
    SplitInfo splitInfo = SplitJsText.split(text);
    String[] lines = splitInfo.lines;
    editor.accept(new Model(lines, new Uri(path)));
  }
}
