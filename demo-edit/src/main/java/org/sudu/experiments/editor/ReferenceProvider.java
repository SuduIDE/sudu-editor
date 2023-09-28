package org.sudu.experiments.editor;

import java.util.function.Consumer;

public class ReferenceProvider extends LanguageSelectors {

  final Provider f;

  public ReferenceProvider(LanguageSelector[] selectors, Provider f) {
    super(selectors);
    this.f = f;
  }

  public interface Provider {
    void provideReferences(
        Model model,
        int line,
        int column,
        boolean includeDeclaration,
        Consumer<Location[]> onResult,
        Consumer<String> onError
    );
  }
}
