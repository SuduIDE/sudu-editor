package org.sudu.experiments.editor;

import org.sudu.experiments.SceneApi;
import org.sudu.experiments.SplitInfo;
import org.sudu.experiments.diff.FileDiff;
import org.sudu.experiments.fonts.Fonts;
import org.sudu.experiments.js.Fetch;
import org.sudu.experiments.js.JsHelper;
import org.sudu.experiments.js.SplitJsText;
import org.teavm.jso.core.JSString;

import java.util.function.Consumer;

public class DiffDemoJs extends FileDiff {

  public static final String classL = "classL.java";
  public static final String classR = "classR.java";

  public DiffDemoJs(SceneApi api) {
    super(api);
    load(model -> left().setModel(model), classL);
    load(model -> right().setModel(model), classR);
  }

  @Override
  public String[] menuFonts() {
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
