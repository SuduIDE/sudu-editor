package org.sudu.experiments.editor;

public class LanguageSelector {

  final String language, scheme;

  public LanguageSelector(String language) {
    this(language, null);
  }

  public LanguageSelector(String language, String scheme) {
    this.language = language;
    this.scheme = scheme;
  }

  public boolean match(String docLanguage, String docScheme) {
    return cmpOptional(docLanguage, language)
        && cmpOptional(docScheme, scheme);
  }

  private static boolean cmpOptional(String valueA, String valueB) {
    return valueB == null || valueA == null || valueB.equals(valueA);
  }
}
