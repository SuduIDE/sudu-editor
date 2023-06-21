package org.sudu.experiments.demo;

import java.util.Arrays;
import java.util.function.BiConsumer;

@SuppressWarnings("unchecked")
public class EditorRegistrations {

  DefDeclProvider[] definitionProviders = new DefDeclProvider[0];
  DefDeclProvider[] declarationProviders = new DefDeclProvider[0];
  ReferenceProvider[] referenceProviders = new ReferenceProvider[0];
  BiConsumer<Model, Model>[] modelChangeListeners = new BiConsumer[0];

  public void registerDefinitionProvider(DefDeclProvider defProvider) {
    definitionProviders = addItem(definitionProviders, defProvider);
  }

  public void removeDefinitionProvider(DefDeclProvider defProvider) {
    removeItem(definitionProviders, defProvider);
  }

  public void registerDeclarationProvider(DefDeclProvider defProvider) {
    declarationProviders = addItem(declarationProviders, defProvider);
  }

  public void removeDeclarationProvider(DefDeclProvider defProvider) {
    removeItem(declarationProviders, defProvider);
  }

  public void registerReferenceProvider(ReferenceProvider refProvider) {
    referenceProviders = addItem(referenceProviders, refProvider);
  }

  public void removeReferenceProvider(ReferenceProvider refProvider) {
    removeItem(referenceProviders, refProvider);
  }

  public void addModelChangeListener(BiConsumer<Model, Model> listener) {
    modelChangeListeners = addItem(modelChangeListeners, listener);
  }

  public void removeModelChangeListener(BiConsumer<Model, Model> listener) {
    removeItem(modelChangeListeners, listener);
  }

  public DefDeclProvider.Provider findDefinitionProvider(String language, String scheme) {
    return findDdProvider(definitionProviders, language, scheme);
  }

  public DefDeclProvider.Provider findDeclarationProvider(String language, String scheme) {
    return findDdProvider(declarationProviders, language, scheme);
  }

  private DefDeclProvider.Provider findDdProvider(DefDeclProvider[] definitionProviders, String language, String scheme) {
    for (DefDeclProvider provider : definitionProviders) {
      if (provider != null) for (LanguageSelector defSelector : provider.languageSelectors) {
        if (defSelector.match(language, scheme)) {
          return provider.f;
        }
      }
    }
    return null;
  }

  public ReferenceProvider.Provider findReferenceProvider(String language, String scheme) {
    for (ReferenceProvider provider : referenceProviders) {
      if (provider != null) for (LanguageSelector defSelector : provider.languageSelectors) {
        if (defSelector.match(language, scheme)) {
          return provider.f;
        }
      }
    }
    return null;
  }

  public void fireModelChange(Model oldModel, Model newModel) {
    for (BiConsumer<Model, Model> listener : modelChangeListeners) {
      if (listener != null) listener.accept(oldModel, newModel);
    }
  }

  static <T> T[] addItem(T[] array, T item) {
    int length = array.length;
    for (int i = 0; i < length; i++) {
      if (array[i] == null) {
        array[i] = item;
        return array;
      }
    }
    array = Arrays.copyOf(array, length + 8);
    array[length] = item;
    return array;
  }

  static <T> void removeItem(T[] array, T item) {
    for (int i = 0; i < array.length; i++) {
      if (array[i] == item) {
        array[i] = null;
        return;
      }
    }
  }
}
