package org.sudu.experiments.fonts;

import java.util.function.Function;

public class FontLoaderJvm {
  public final String[] fonts;
  public final Function<String, byte[]> loader;

  public FontLoaderJvm(Function<String, byte[]> loader, String... fonts) {
    this.fonts = fonts;
    this.loader = loader;
  }

}
