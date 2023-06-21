package org.sudu.experiments.demo;

import java.util.function.Consumer;

public class ReferenceProvider {

  final LanguageSelector[] languageSelectors;
  final Provider f;

  public ReferenceProvider(LanguageSelector[] languageSelectors, Provider f) {
    this.languageSelectors = languageSelectors;
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
