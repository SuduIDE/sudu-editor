package org.sudu.experiments.demo;

import org.sudu.experiments.SceneApi;
import org.sudu.experiments.js.Fetch;
import org.sudu.experiments.js.JsHelper;
import org.sudu.experiments.js.SplitJsText;
import org.teavm.jso.core.JSString;

import java.util.function.Consumer;

public class DiffDemoJs extends Diff0 {
  public DiffDemoJs(SceneApi api) {
    super(api);
    load(this::setLeftModel, "ClassM.java");
    load(this::setRightModel, "ClassN.java");
  }

  void load(Consumer<Model> editor, String path) {
    Fetch.fetch(path)
        .then(Fetch.Response::text)
        .then(text -> loadText(editor, path, text), JsHelper::onError);
  }

  static void loadText(Consumer<Model> editor, String path, JSString text) {
    String[] split = SplitJsText.split(text, Document.newLine);
    editor.accept(new Model(split, new Uri(path)));
  }
}
