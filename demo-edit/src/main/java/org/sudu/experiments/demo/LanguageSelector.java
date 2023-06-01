package org.sudu.experiments.demo;

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

  private static boolean cmpOptional(String docLanguage, String language) {
    return language == null || docLanguage == null || language.equals(docLanguage);
  }
}
