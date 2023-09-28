package org.sudu.experiments.editor;

public class DocumentHighlight {
  public final Range range = new Range();
  public Integer kind;

  public static final int KindText = 0;
  public static final int KindRead = 1;
  public static final int KindWrite = 2;
}
