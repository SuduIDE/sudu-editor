package org.sudu.experiments.editor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LanguageSelectorTest {

  @Test
  public void allExistMatch() {
    LanguageSelector ls = new LanguageSelector("java", "some");
    assertTrue(ls.match("java", "some"));
  }

  @Test
  public void allExistLanguageMismatch() {
    LanguageSelector ls = new LanguageSelector("java", "some");
    assertFalse(ls.match("c", "some"));
  }

  @Test
  public void allExistSchemeMismatch() {
    LanguageSelector ls = new LanguageSelector("java", "some");
    assertFalse(ls.match("java", "another"));
  }

  @Test
  public void languageExistMatch() {
    LanguageSelector ls = new LanguageSelector("java", null);
    assertTrue(ls.match("java", null));
  }

  @Test
  public void languageExistPartialSchemeMatch() {
    LanguageSelector ls = new LanguageSelector("java", null);
    assertTrue(ls.match("java", "some"));
  }

  @Test
  public void schemeExistMatch() {
    LanguageSelector ls = new LanguageSelector(null, "some");
    assertTrue(ls.match(null, "some"));
  }

  @Test
  public void nonExistMatch() {
    LanguageSelector ls = new LanguageSelector(null, null);
    assertTrue(ls.match(null, null));
  }
}
