package org.sudu.experiments.demo;

import org.sudu.experiments.math.ArrayOp;

import java.util.function.BiConsumer;

@SuppressWarnings("unchecked")
public class EditorRegistrations {

  DefinitionProvider[] definitionProviders = new DefinitionProvider[0];
  ReferenceProvider[] referenceProviders = new ReferenceProvider[0];
  BiConsumer<Model, Model>[] modelChangeListeners = new BiConsumer[0];

  public void registerDefinitionProvider(DefinitionProvider defProvider) {
    definitionProviders = ArrayOp.add(definitionProviders, defProvider);
  }

  public void registerReferenceProvider(ReferenceProvider refProvider) {
    referenceProviders = ArrayOp.add(referenceProviders, refProvider);
  }

  public void addModelChangeListener(BiConsumer<Model, Model> listener) {
    modelChangeListeners = ArrayOp.add(modelChangeListeners, listener);
  }

  public DefinitionProvider findDefinitionProvider(String language, String scheme) {
    for (DefinitionProvider definitionProvider : definitionProviders) {
      for (LanguageSelector defSelector : definitionProvider.languageSelectors) {
        if (defSelector.match(language, scheme)) {
          return definitionProvider;
        }
      }
    }
    return null;
  }

  public ReferenceProvider findReferenceProvider(String language, String scheme) {
    for (ReferenceProvider referenceProvider : referenceProviders) {
      for (LanguageSelector defSelector : referenceProvider.languageSelectors) {
        if (defSelector.match(language, scheme)) {
          return referenceProvider;
        }
      }
    }
    return null;
  }

  public void fireModelChange(Model oldModel, Model newModel) {
    for (BiConsumer<Model, Model> listener : modelChangeListeners) {
      listener.accept(oldModel, newModel);
    }
  }
}
