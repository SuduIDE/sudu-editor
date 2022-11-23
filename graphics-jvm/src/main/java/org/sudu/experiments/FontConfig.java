package org.sudu.experiments;

import java.util.function.Function;

public class FontConfig {
  public final String[] fonts;
  public final Function<String, byte[]> loader;

  public FontConfig(Function<String, byte[]> loader, String... fonts) {
    this.fonts = fonts;
    this.loader = loader;
  }

  public FontConfig() {
    this(s -> null);
  }
}
