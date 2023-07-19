package org.sudu.experiments.demo;

import org.sudu.experiments.Subscribers;

import java.util.function.BiConsumer;

@SuppressWarnings("unchecked")
public class EditorRegistrations {

  public final Subscribers<DefDeclProvider> definitionProviders =
      new Subscribers<>(new DefDeclProvider[0]);

  public final Subscribers<DefDeclProvider> declarationProviders =
      new Subscribers<>(new DefDeclProvider[0]);

  public final Subscribers<ReferenceProvider> referenceProviders =
      new Subscribers<>(new ReferenceProvider[0]);

  public final Subscribers<DocumentHighlightProvider> documentHighlightProviders =
      new Subscribers<>(new DocumentHighlightProvider[0]);

  public final Subscribers<BiConsumer<Model, Model>> modelChangeListeners =
      new Subscribers<BiConsumer<Model, Model>>(new BiConsumer[0]);

  public final Subscribers<EditorOpener> openers = new Subscribers<>(new EditorOpener[0]);

  public DefDeclProvider.Provider findDefinitionProvider(String language, String scheme) {
    return findDdProvider(definitionProviders.array(), language, scheme);
  }

  public DefDeclProvider.Provider findDeclarationProvider(String language, String scheme) {
    return findDdProvider(declarationProviders.array(), language, scheme);
  }

  public DocumentHighlightProvider.Provider findDocumentHighlightProvider(String language, String scheme) {
    for (DocumentHighlightProvider provider : documentHighlightProviders.array()) {
      if (provider.match(language, scheme)) {
        return provider.f;
      }
    }
    return null;
  }

  public EditorOpener findOpener() {
    for (EditorOpener opener : openers.array()) {
      if (opener != null) return opener;
    }
    return null;
  }

  private DefDeclProvider.Provider findDdProvider(DefDeclProvider[] definitionProviders, String language, String scheme) {
    for (DefDeclProvider provider : definitionProviders) {
      if (provider.match(language, scheme)) {
        return provider.f;
      }
    }
    return null;
  }

  public ReferenceProvider.Provider findReferenceProvider(String language, String scheme) {
    for (ReferenceProvider provider : referenceProviders.array()) {
      if (provider.match(language, scheme)) {
        return provider.f;
      }
    }
    return null;
  }

  public void fireModelChange(Model oldModel, Model newModel) {
    for (BiConsumer<Model, Model> listener : modelChangeListeners.array()) {
      listener.accept(oldModel, newModel);
    }
  }
}
