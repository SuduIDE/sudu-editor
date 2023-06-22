package org.sudu.experiments.demo;

public class LanguageSelectors {
  final LanguageSelector[] selectors;

  public LanguageSelectors(LanguageSelector[] selectors) {
    this.selectors = selectors;
  }

  public boolean match(String language, String scheme) {
    for (LanguageSelector defSelector : selectors) {
      if (defSelector.match(language, scheme)) {
        return true;
      }
    }
    return false;
  }
}
