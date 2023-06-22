package org.sudu.experiments.demo;

public class Model {

  public final Uri uri;
  public String language;
  public Document document;
  public Object platformObject;

  public Model(String[] text, String language, Uri uri, Object platformObject) {
    this.language = language;
    this.uri = uri;
    this.platformObject = platformObject;

    CodeLine[] cl = CodeLine.makeLines(text);
    this.document = new Document(cl);
  }

  public Model(Document document) {
    this.document = document;
    this.language = Languages.TEXT;
    this.uri = null;
  }

  public String uriScheme() {
    return uri != null ? uri.scheme : null;
  }
}
