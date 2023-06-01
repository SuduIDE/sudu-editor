package org.sudu.experiments.demo;

public class ReferenceProvider {

  final LanguageSelector[] languageSelectors;
  private final Provider f;

  public ReferenceProvider(LanguageSelector[] languageSelectors, Provider f) {
    this.languageSelectors = languageSelectors;
    this.f = f;
  }

  public void provideReferences(Model model, int line, int column, boolean includeDeclaration) {
    f.provideDefinition(model, line, column, includeDeclaration);
  }

  public interface Provider {
    void provideDefinition(Model model, int line, int column, boolean includeDeclaration);
  }
}
