package org.sudu.experiments.demo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LanguageSelectorTest {

  @Test
  public void AllExistMatch() {
    LanguageSelector ls = new LanguageSelector("java", "some");
    assertTrue(ls.match("java", "some"));
  }

  @Test
  public void AllExistLanguageMismatch() {
    LanguageSelector ls = new LanguageSelector("java", "some");
    assertFalse(ls.match("c", "some"));
  }

  @Test
  public void AllExistSchemeMismatch() {
    LanguageSelector ls = new LanguageSelector("java", "some");
    assertFalse(ls.match("java", "another"));
  }

  @Test
  public void LanguageExistMatch() {
    LanguageSelector ls = new LanguageSelector("java", null);
    assertTrue(ls.match("java", null));
  }

  @Test
  public void LanguageExistPartialSchemeMatch() {
    LanguageSelector ls = new LanguageSelector("java", null);
    assertTrue(ls.match("java", "some"));
  }

  @Test
  public void SchemeExistMatch() {
    LanguageSelector ls = new LanguageSelector(null, "some");
    assertTrue(ls.match(null, "some"));
  }

  @Test
  public void NonExistMatch() {
    LanguageSelector ls = new LanguageSelector(null, null);
    assertTrue(ls.match(null, null));
  }
}
