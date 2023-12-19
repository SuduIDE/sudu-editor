package org.sudu.experiments.editor;

public class Model {

  public final Uri uri;
  public final Document document;
  public final Selection selection = new Selection();

  public Object platformObject;
  private String docLanguage;
  private String languageFromFile;
  final Selection selection = new Selection();

  public Model(String text, String language, Uri uri) {
    this(SplitText.split(text), language, uri);
  }

  public Model(String[] text, Uri uri) {
    this(text, null, uri);
  }

  public Model(String[] text, String language, Uri uri) {
    this.uri = uri;
    docLanguage = language;
    languageFromFile = uri != null ? Languages.languageFromFilename(uri.path) : null;
    document = new Document(text);
  }

  public Model() {
    this.document = new Document();
    this.docLanguage = Languages.TEXT;
    this.uri = null;
  }

  public void setLanguage(String language) {
    docLanguage = language;
  }

  public String language() {
    return docLanguage != null ? docLanguage : languageFromFile;
  }

  public String docLanguage() { return docLanguage; }

  public String uriScheme() {
    return uri != null ? uri.scheme : null;
  }
}
