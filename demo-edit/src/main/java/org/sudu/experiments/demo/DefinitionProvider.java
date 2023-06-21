package org.sudu.experiments.demo;

import java.util.function.Consumer;

public class DefinitionProvider {

  final LanguageSelector[] languageSelectors;
  final Provider f;

  public DefinitionProvider(LanguageSelector[] languageSelectors, Provider f) {
    this.languageSelectors = languageSelectors;
    this.f = f;
  }

  public interface Provider {
    void provideDefinition(Model model, int line, int column, Consumer<Location[]> result, Consumer<String> onError);
  }
}
