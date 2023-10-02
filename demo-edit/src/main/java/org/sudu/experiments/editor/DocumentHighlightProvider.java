package org.sudu.experiments.editor;

import java.util.function.Consumer;

public class DocumentHighlightProvider extends LanguageSelectors {
  final Provider f;

  public DocumentHighlightProvider(LanguageSelector[] selectors, Provider f) {
    super(selectors);
    this.f = f;
  }

  public interface Provider {
    void provide(
        Model model,
        int line,
        int column,
        Consumer<DocumentHighlight[]> onResult,
        Consumer<String> onError
    );
  }
}
