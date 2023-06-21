package org.sudu.experiments.demo;

public class ReferenceProvider {

  final LanguageSelector[] languageSelectors;
  final Provider f;

  public ReferenceProvider(LanguageSelector[] languageSelectors, Provider f) {
    this.languageSelectors = languageSelectors;
    this.f = f;
  }

  public interface Provider {
    void provideReferences(EditorComponent editor, int line, int column, boolean includeDeclaration);
  }
}
