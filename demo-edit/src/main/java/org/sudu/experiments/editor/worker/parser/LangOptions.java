package org.sudu.experiments.editor.worker.parser;

import org.sudu.experiments.editor.Languages;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class LangOptions {

  private final Set<String> langSet;
  private boolean warmPrinted = false;

  public LangOptions() {
    this.langSet = new HashSet<>();
  }

  public boolean structureParsingEnabled(String lang) {
    return Objects.equals(Languages.JAVA, lang) && isEnabled(Languages.JAVA);
  }

  public String getCppVersion(String lang) {
    boolean isAscendC = Objects.equals(lang, Languages.ASCEND_C);
    boolean enabledAscendC = langSet.contains(Languages.ASCEND_C);
    boolean enabledCpp = langSet.contains(Languages.CPP);
    if (!enabledAscendC && !enabledCpp) return Languages.TEXT;
    if (enabledAscendC && isAscendC) return Languages.ASCEND_C;
    if (enabledAscendC && enabledCpp) {
      if (!warmPrinted) {
        System.err.println("[warn] C++ and Ascend C are both enabled in the same time");
        warmPrinted = true;
      }
      return Languages.CPP;
    }
    return enabledCpp ? Languages.CPP : Languages.ASCEND_C;
  }

  public boolean isEnabled(String lang) {
    return Objects.equals(Languages.TEXT, lang) || langSet.contains(lang);
  }

  public void enable(String lang) {
    langSet.add(lang);
  }

  public void enableAll() {
    langSet.addAll(Arrays.asList(Languages.getAllLanguages()));
  }

  public void disable(String lang) {
    langSet.remove(lang);
  }

  public void disableAll() {
    langSet.clear();
  }
}
