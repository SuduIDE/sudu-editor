package org.sudu.experiments.demo;

import java.util.function.Consumer;

public class DefDeclProvider {

  final LanguageSelector[] languageSelectors;
  final Provider f;

  public DefDeclProvider(LanguageSelector[] languageSelectors, Provider f) {
    this.languageSelectors = languageSelectors;
    this.f = f;
  }

  public interface Provider {
    void provide(
        Model model, int line, int column,
        Consumer<Location[]> result,
        Consumer<String> onError);
  }
}
