package org.sudu.experiments.demo;

public class Model {

  public final String language;
  public final Uri uri;
  public Document document;

  public Model(String[] text, String language, Uri uri) {
    this.language = language;
    this.uri = uri;

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

  public Model(Document document, String language) {
    this.document = document;
    this.language = language;
    this.uri = null;
  }

  public String[] getCodeLines() {
    String[] res = new String[document.length()];
    for (int i = 0; i < document.length(); i++) {
      res[i] = document.line(i).makeString();
    }
    return res;
  }
}
