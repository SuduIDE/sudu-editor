package org.sudu.experiments.demo;

public class Model {

  public final String language;
  public final Uri uri;
  public Document document;
  public Object platformObject;

  public Model(String[] text, String language, Uri uri, Object platformObject) {
    this.language = language;
    this.uri = uri;
    this.platformObject = platformObject;

    CodeLine[] cl = new CodeLine[text.length];
    for (int i = 0; i < text.length; i++) {
      cl[i] = new CodeLine(new CodeElement(text[i]));
    }
    this.document = new Document(cl);
  }

  public Model(Document document) {
    this.document = document;
    this.language = "java";
    this.uri = null;
  }
}
