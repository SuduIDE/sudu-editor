package org.sudu.experiments.demo;

public class DefinitionProvider {

  final LanguageSelector[] languageSelectors;
  final Provider f;

  public DefinitionProvider(LanguageSelector[] languageSelectors, Provider f) {
    this.languageSelectors = languageSelectors;
    this.f = f;
  }

  public interface Provider {
    void provideDefinition(EditorComponent editor, int line, int column);
  }
}
