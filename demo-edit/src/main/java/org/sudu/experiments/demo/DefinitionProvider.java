package org.sudu.experiments.demo;

public class DefinitionProvider {

  final LanguageSelector[] languageSelectors;
  private final Provider f;

  public DefinitionProvider(LanguageSelector[] languageSelectors, Provider f) {
    this.languageSelectors = languageSelectors;
    this.f = f;
  }

  public void provideDefinition(Model model, int line, int column) {
    f.provideDefinition(model, line, column);
  }

  public interface Provider {
    void provideDefinition(Model model, int line, int column);
  }
}
