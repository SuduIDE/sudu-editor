package org.sudu.experiments.editor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.sudu.experiments.text.SplitText;

import static org.sudu.experiments.editor.DocumentTest.*;

public class UndoBufferTest {

  private static final String EMPTY_INITIAL = "";
  private static final String INITIAL = """
      Old line 0
      Old line 1
      Old line 2
      Old line 3
      Old line 4""";
  private static final boolean PRINT_DOC = false;

  @Test
  public void testApplyEditChange1() {
    var undoBuffer = new UndoBuffer();

    Document document = new Document(SplitText.split(INITIAL));
    document.getUndoBuffer = () -> undoBuffer;

    document.applyChange(1, 2, new CodeLine[]{
        ab("New line 1"),
    });
    testUndoBuffer(document, INITIAL);
    Assertions.assertEquals(5, document.length());
    Assertions.assertEquals("Old line 0", document.line(0).makeString());
    Assertions.assertEquals("New line 1", document.line(1).makeString());
    Assertions.assertEquals("Old line 2", document.line(2).makeString());
    Assertions.assertEquals("Old line 3", document.line(3).makeString());
    Assertions.assertEquals("Old line 4", document.line(4).makeString());
  }

  @Test
  public void testApplyEditChange2() {
    var undoBuffer = new UndoBuffer();

    Document document = new Document(SplitText.split(INITIAL));
    document.getUndoBuffer = () -> undoBuffer;

    document.applyChange(0, 5, new CodeLine[]{
        ab("New line 0"),
        ab("New line 1"),
        ab("New line 2"),
        ab("New line 3"),
        ab("New line 4"),
    });
    testUndoBuffer(document, INITIAL);
    Assertions.assertEquals(5, document.length());
    Assertions.assertEquals("New line 0", document.line(0).makeString());
    Assertions.assertEquals("New line 1", document.line(1).makeString());
    Assertions.assertEquals("New line 2", document.line(2).makeString());
    Assertions.assertEquals("New line 3", document.line(3).makeString());
    Assertions.assertEquals("New line 4", document.line(4).makeString());
  }

  @Test
  public void testApplyEditChange3() {
    var undoBuffer = new UndoBuffer();

    Document document = new Document(SplitText.split(INITIAL));
    document.getUndoBuffer = () -> undoBuffer;

    document.applyChange(1, 5, new CodeLine[]{
        ab("New line 1"),
        ab("New line 2"),
        ab("New line 3"),
        ab("New line 4"),
    });
    testUndoBuffer(document, INITIAL);
    Assertions.assertEquals(5, document.length());
    Assertions.assertEquals("Old line 0", document.line(0).makeString());
    Assertions.assertEquals("New line 1", document.line(1).makeString());
    Assertions.assertEquals("New line 2", document.line(2).makeString());
    Assertions.assertEquals("New line 3", document.line(3).makeString());
    Assertions.assertEquals("New line 4", document.line(4).makeString());
  }

  @Test
  public void testApplyEditChange4() {
    var undoBuffer = new UndoBuffer();

    Document document = new Document(SplitText.split(INITIAL));
    document.getUndoBuffer = () -> undoBuffer;

    document.applyChange(0, 4, new CodeLine[]{
        ab("New line 0"),
        ab("New line 1"),
        ab("New line 2"),
        ab("New line 3"),
    });
    testUndoBuffer(document, INITIAL);
    Assertions.assertEquals(5, document.length());
    Assertions.assertEquals("New line 0", document.line(0).makeString());
    Assertions.assertEquals("New line 1", document.line(1).makeString());
    Assertions.assertEquals("New line 2", document.line(2).makeString());
    Assertions.assertEquals("New line 3", document.line(3).makeString());
    Assertions.assertEquals("Old line 4", document.line(4).makeString());
  }

  @Test
  public void testApplyDeleteChange1() {
    var undoBuffer = new UndoBuffer();

    Document document = new Document(SplitText.split(INITIAL));
    document.getUndoBuffer = () -> undoBuffer;

    document.applyChange(1, 2, new CodeLine[]{});
    testUndoBuffer(document, INITIAL);
    Assertions.assertEquals(4, document.length());
    Assertions.assertEquals("Old line 0", document.line(0).makeString());
    Assertions.assertEquals("Old line 2", document.line(1).makeString());
    Assertions.assertEquals("Old line 3", document.line(2).makeString());
    Assertions.assertEquals("Old line 4", document.line(3).makeString());
  }

  @Test
  public void testApplyDeleteChange2() {
    var undoBuffer = new UndoBuffer();

    Document document = new Document(SplitText.split(INITIAL));
    document.getUndoBuffer = () -> undoBuffer;

    document.applyChange(0, 5, new CodeLine[]{});
    testUndoBuffer(document, INITIAL);
    Assertions.assertEquals(1, document.length());
    Assertions.assertEquals("", document.line(0).makeString());
  }

  @Test
  public void testApplyDeleteChange3() {
    var undoBuffer = new UndoBuffer();

    Document document = new Document(SplitText.split(INITIAL));
    document.getUndoBuffer = () -> undoBuffer;

    document.applyChange(1, 5, new CodeLine[]{});
    testUndoBuffer(document, INITIAL);
    Assertions.assertEquals(1, document.length());
    Assertions.assertEquals("Old line 0", document.line(0).makeString());
  }

  @Test
  public void testApplyDeleteChange4() {
    var undoBuffer = new UndoBuffer();

    Document document = new Document(SplitText.split(INITIAL));
    document.getUndoBuffer = () -> undoBuffer;

    document.applyChange(0, 4, new CodeLine[]{});
    testUndoBuffer(document, INITIAL);
    Assertions.assertEquals(1, document.length());
    Assertions.assertEquals("Old line 4", document.line(0).makeString());
  }

  @Test
  public void testApplyInsertChange1() {
    var undoBuffer = new UndoBuffer();

    Document document = new Document(SplitText.split(INITIAL));
    document.getUndoBuffer = () -> undoBuffer;

    document.applyChange(1, 1, new CodeLine[]{
        ab("Inserted line 1"),
        ab("Inserted line 2"),
        ab("Inserted line 3"),
    });
    testUndoBuffer(document, INITIAL);
    Assertions.assertEquals(8, document.length());
    Assertions.assertEquals("Old line 0", document.line(0).makeString());
    Assertions.assertEquals("Inserted line 1", document.line(1).makeString());
    Assertions.assertEquals("Inserted line 2", document.line(2).makeString());
    Assertions.assertEquals("Inserted line 3", document.line(3).makeString());
    Assertions.assertEquals("Old line 1", document.line(4).makeString());
    Assertions.assertEquals("Old line 2", document.line(5).makeString());
    Assertions.assertEquals("Old line 3", document.line(6).makeString());
    Assertions.assertEquals("Old line 4", document.line(7).makeString());
  }

  @Test
  public void testApplyInsertChange2() {
    var undoBuffer = new UndoBuffer();

    Document document = new Document(SplitText.split(INITIAL));
    document.getUndoBuffer = () -> undoBuffer;

    document.applyChange(0, 0, new CodeLine[]{
        ab("Inserted line 1"),
        ab("Inserted line 2"),
        ab("Inserted line 3"),
    });
    testUndoBuffer(document, INITIAL);
    Assertions.assertEquals(8, document.length());
    Assertions.assertEquals("Inserted line 1", document.line(0).makeString());
    Assertions.assertEquals("Inserted line 2", document.line(1).makeString());
    Assertions.assertEquals("Inserted line 3", document.line(2).makeString());
    Assertions.assertEquals("Old line 0", document.line(3).makeString());
    Assertions.assertEquals("Old line 1", document.line(4).makeString());
    Assertions.assertEquals("Old line 2", document.line(5).makeString());
    Assertions.assertEquals("Old line 3", document.line(6).makeString());
    Assertions.assertEquals("Old line 4", document.line(7).makeString());
  }

  @Test
  public void testApplyInsertChange3() {
    var undoBuffer = new UndoBuffer();

    Document document = new Document(SplitText.split(INITIAL));
    document.getUndoBuffer = () -> undoBuffer;

    document.applyChange(5, 5, new CodeLine[]{
        ab("Inserted line 1"),
        ab("Inserted line 2"),
        ab("Inserted line 3"),
    });
    testUndoBuffer(document, INITIAL);
    Assertions.assertEquals(8, document.length());
    Assertions.assertEquals("Old line 0", document.line(0).makeString());
    Assertions.assertEquals("Old line 1", document.line(1).makeString());
    Assertions.assertEquals("Old line 2", document.line(2).makeString());
    Assertions.assertEquals("Old line 3", document.line(3).makeString());
    Assertions.assertEquals("Old line 4", document.line(4).makeString());
    Assertions.assertEquals("Inserted line 1", document.line(5).makeString());
    Assertions.assertEquals("Inserted line 2", document.line(6).makeString());
    Assertions.assertEquals("Inserted line 3", document.line(7).makeString());
  }

  @Test
  public void testApplyInsertChange4() {
    var undoBuffer = new UndoBuffer();

    Document document = new Document(SplitText.split(EMPTY_INITIAL));
    document.getUndoBuffer = () -> undoBuffer;

    document.applyChange(0, 0, new CodeLine[]{
        ab("Inserted line 1"),
        ab("Inserted line 2"),
        ab("Inserted line 3"),
    });
    testUndoBuffer(document, EMPTY_INITIAL);
    Assertions.assertEquals(4, document.length());
    Assertions.assertEquals("Inserted line 1", document.line(0).makeString());
    Assertions.assertEquals("Inserted line 2", document.line(1).makeString());
    Assertions.assertEquals("Inserted line 3", document.line(2).makeString());
    Assertions.assertEquals("", document.line(3).makeString());
  }

  private void testUndoBuffer(Document document, String initial) {
    String doc1 = document.makeString();
    if (PRINT_DOC) {
      System.out.println(doc1);
      System.out.println("—".repeat(10));
    }

    document.undoLastDiff(false);
    String doc2 = document.makeString();
    if (PRINT_DOC) {
      System.out.println(doc2);
      System.out.println("—".repeat(10));
    }

    document.undoLastDiff(true);
    String doc3 = document.makeString();
    if (PRINT_DOC) System.out.println(doc3);

    Assertions.assertEquals(initial, doc2);
    Assertions.assertEquals(doc1, doc3);

    Assertions.assertNotEquals(initial, doc1);
    Assertions.assertNotEquals(doc1, doc2);
    Assertions.assertNotEquals(doc2, doc3);
  }
}
