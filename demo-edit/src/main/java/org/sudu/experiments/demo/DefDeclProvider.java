package org.sudu.experiments.demo;

import java.util.function.Consumer;

public class DefDeclProvider extends LanguageSelectors {

  final Provider f;

  public DefDeclProvider(LanguageSelector[] selectors, Provider f) {
    super(selectors);
    this.f = f;
  }

  public interface Provider {
    void provide(
        Model model, int line, int column,
        Consumer<Location[]> result,
        Consumer<String> onError);
  }
}
