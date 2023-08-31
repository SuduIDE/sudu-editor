package org.sudu.experiments.demo;

public class Model {

  public final Uri uri;
  public Document document;
  public Object platformObject;
  private String docLanguage;
  private String languageFromFile;

  public Model(String text, String language, Uri uri) {
    this(SplitText.split(text), language, uri);
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
