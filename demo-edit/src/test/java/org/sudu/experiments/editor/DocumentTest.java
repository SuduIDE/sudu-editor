package org.sudu.experiments.editor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.parser.common.Pos;
import org.sudu.experiments.text.SplitText;

import java.util.ArrayList;
import java.util.Arrays;

class DocumentTest {
  @Test void deleteCharTest() {
    String[] from = new String[] { "A", "B", "C" };
    String[] r1 = ArrayOp.remove(from, 0, new String[2]);
    String[] r2 = ArrayOp.remove(from, 1, new String[2]);
    String[] r3 = ArrayOp.remove(from, 2, new String[2]);
    Assertions.assertEquals(r1[0], "B");
    Assertions.assertEquals(r1[1], "C");
    Assertions.assertEquals(r2[0], "A");
    Assertions.assertEquals(r2[1], "C");
    Assertions.assertEquals(r3[0], "A");
    Assertions.assertEquals(r3[1], "B");

    CodeLine cl = new CodeLine(new CodeElement("A"));
    cl.deleteAt(0);
    Assertions.assertEquals(cl.elements.length, 0);
  }

  @Test void testLineMeasure() {
    CodeLine cl = new CodeLine(new CodeElement("A"));
    cl.iMeasure = new int[1];
    Assertions.assertEquals(cl.lineMeasure(), 0);
    cl.deleteAt(0);
    Assertions.assertEquals(cl.lineMeasure(), 0);
  }

  @Test void concatTest() {
    Document d = doc4();
    d.concatLines(0);
    Assertions.assertEquals(d.lines.length, 3);
    Assertions.assertEquals(d.lines[0].elements.length, 2);
    Assertions.assertEquals(d.lines[0].elements[0].s, "AB");
    Assertions.assertEquals(d.lines[0].elements[1].s, "CD");
    Assertions.assertEquals(d.lines[1].elements.length, 1);
    Assertions.assertEquals(d.lines[1].elements[0].s, "EF");
    Assertions.assertEquals(d.lines[2].elements.length, 1);
    Assertions.assertEquals(d.lines[2].elements[0].s, "GH");

    d.concatLines(0);

    Assertions.assertEquals(d.lines.length, 2);
    Assertions.assertEquals(d.lines[0].elements.length, 3);
    Assertions.assertEquals(d.lines[0].elements[0].s, "AB");
    Assertions.assertEquals(d.lines[0].elements[1].s, "CD");
    Assertions.assertEquals(d.lines[0].elements[2].s, "EF");
    Assertions.assertEquals(d.lines[1].elements.length, 1);
    Assertions.assertEquals(d.lines[1].elements[0].s, "GH");

    d.concatLines(0);

    Assertions.assertEquals(d.lines.length, 1);
    Assertions.assertEquals(d.lines[0].elements.length, 4);
    Assertions.assertEquals(d.lines[0].elements[0].s, "AB");
    Assertions.assertEquals(d.lines[0].elements[1].s, "CD");
    Assertions.assertEquals(d.lines[0].elements[2].s, "EF");
    Assertions.assertEquals(d.lines[0].elements[3].s, "GH");
  }

  @Test void concatTest2() {
    Document d = doc4();
    d.concatLines(1);
    d.concatLines(1);

    Assertions.assertEquals(d.lines.length, 2);
    Assertions.assertEquals(d.lines[1].elements.length, 3);
    Assertions.assertEquals(d.lines[0].elements[0].s, "AB");
    Assertions.assertEquals(d.lines[1].elements[0].s, "CD");
    Assertions.assertEquals(d.lines[1].elements[1].s, "EF");
    Assertions.assertEquals(d.lines[1].elements[2].s, "GH");
  }

  @Test void concat3Test() {
    Document d = doc4();
    d.concatLines(2);

    Assertions.assertEquals(d.lines.length, 3);
    Assertions.assertEquals(d.lines[0].elements.length, 1);
    Assertions.assertEquals(d.lines[1].elements.length, 1);
    Assertions.assertEquals(d.lines[2].elements.length, 2);
    Assertions.assertEquals(d.lines[0].elements[0].s, "AB");
    Assertions.assertEquals(d.lines[1].elements[0].s, "CD");
    Assertions.assertEquals(d.lines[2].elements[0].s, "EF");
    Assertions.assertEquals(d.lines[2].elements[1].s, "GH");
  }

  static Document newDocument(int n) {
    return new Document(TestText.document(n, false));
  }

  @Test void newLineTest() {
    Document a = newDocument(3);
    a.newLineOp(0,0);

    Assertions.assertEquals(a.lines.length,4);
    Assertions.assertEquals(a.lines[0].elements.length,0);

    Document b = newDocument(3);

    b.newLineOp(0,b.strLength(0));

    Assertions.assertEquals(b.lines.length,4);
    Assertions.assertEquals(b.lines[1].elements.length,0);

    Document c = newDocument(3);
    c.newLineOp(c.length() - 1, 0);

    Assertions.assertEquals(c.lines.length,4);
    Assertions.assertEquals(c.lines[2].elements.length,0);

    Document d = newDocument(3);
    d.newLineOp(d.length() - 1, d.strLength(d.length() - 1));

    Assertions.assertEquals(d.lines.length,4);
    Assertions.assertEquals(d.lines[3].elements.length,0);
  }

  @Test void newLineTest2() {
    ArrayList<Document> list = new ArrayList<>();
    {
      Document a = new Document(line());
      a.newLineOp(0, 1);
      Assertions.assertEquals(a.lines.length, 2);
      Assertions.assertEquals(a.lines[0].elements.length, 1);
      Assertions.assertEquals(a.lines[0].elements[0].s, "A");
      Assertions.assertEquals(a.lines[1].elements.length, 1);
      Assertions.assertEquals(a.lines[1].elements[0].s, "B");
      list.add(a);
    }
    {
      Document b = new Document(new CodeLine(abElement("AB"), abElement("CD")));
      b.newLineOp(0, 1);

      Assertions.assertEquals(b.lines.length, 2);
      Assertions.assertEquals(b.lines[0].elements.length, 1);
      Assertions.assertEquals(b.lines[0].elements[0].s, "A");
      Assertions.assertEquals(b.lines[1].elements.length, 2);
      Assertions.assertEquals(b.lines[1].elements[0].s, "B");
      Assertions.assertEquals(b.lines[1].elements[1].s, "CD");
      list.add(b);
    }
    {
      Document c = new Document(
          new CodeLine(abElement("AB"), abElement("CD")));
      c.newLineOp(0, 2);
      list.add(c);

      Assertions.assertEquals(c.lines.length, 2);
      Assertions.assertEquals(c.lines[0].elements.length, 1);
      Assertions.assertEquals(c.lines[0].elements[0].s, "AB");
      Assertions.assertEquals(c.lines[1].elements.length, 1);
      Assertions.assertEquals(c.lines[1].elements[0].s, "CD");
    }
    {
      Document d = new Document(
          new CodeLine(abElement("AB"), abElement("CD")));
      d.newLineOp(0, 3);
      list.add(d);

      Assertions.assertEquals(d.lines.length, 2);
      Assertions.assertEquals(d.lines[0].elements.length, 2);
      Assertions.assertEquals(d.lines[0].elements[0].s, "AB");
      Assertions.assertEquals(d.lines[0].elements[1].s, "C");
      Assertions.assertEquals(d.lines[1].elements.length, 1);
      Assertions.assertEquals(d.lines[1].elements[0].s, "D");
    }
    {
      Document e = new Document(
          new CodeLine(abElement("AB"), abElement("CD"), abElement("EF")));
      e.newLineOp(0, 3);
      list.add(e);
      Assertions.assertEquals(e.lines.length, 2);
      Assertions.assertEquals(e.lines[0].elements.length, 2);
      Assertions.assertEquals(e.lines[0].elements[0].s, "AB");
      Assertions.assertEquals(e.lines[0].elements[1].s, "C");
      Assertions.assertEquals(e.lines[1].elements.length, 2);
      Assertions.assertEquals(e.lines[1].elements[0].s, "D");
      Assertions.assertEquals(e.lines[1].elements[1].s, "EF");
    }
//    System.out.println("r = " + list);
  }

  @Test void deleteAtTest() {
    {
      CodeElement a = abElement("ABC");
      CodeElement da = a.deleteAt(0);
      Assertions.assertEquals(da.s, "BC");
    }
    {
      CodeElement b = abElement("ABC");
      CodeElement db = b.deleteAt(1);
      Assertions.assertEquals(db.s, "AC");
    }
    {
      CodeElement c = abElement("ABC");
      CodeElement dc = c.deleteAt(2);
      Assertions.assertEquals(dc.s, "AB");
    }
  }

  @Test void insertAtTest() {
    {
      CodeElement a = abElement("ABC");
      CodeElement d = a.insertAt(0, "Ins");
      Assertions.assertEquals(d.s, "InsABC");
    }
    {
      CodeElement a = abElement("ABC");
      CodeElement d = a.insertAt(1, "In");
      Assertions.assertEquals(d.s, "AInBC");
    }
    {
      CodeElement a = abElement("ABC");
      CodeElement d = a.insertAt(2, "In");
      Assertions.assertEquals(d.s, "ABInC");
    }
    {
      CodeElement a = abElement("ABC");
      CodeElement d = a.insertAt(3, "In");
      Assertions.assertEquals(d.s, "ABCIn");
    }

  }

  @Test void deleteLineTest() {
    Document doc4 = doc4();
    Document doc0 = new Document();
    doc0.deleteLine(0);
    doc0.deleteLine(0);
    doc0.deleteLine(0);
    Assertions.assertEquals(doc0.lines.length, 1);

    CodeLine[] copy = Arrays.copyOf(doc4.lines, doc4.lines.length);
    doc4.deleteLine(3);
    Assertions.assertEquals(doc4.lines.length, 3);
    Assertions.assertSame(doc4.lines[0], copy[0]);
    Assertions.assertSame(doc4.lines[1], copy[1]);
    Assertions.assertSame(doc4.lines[2], copy[2]);

    doc4.deleteLine(0);
    Assertions.assertEquals(doc4.lines.length, 2);
    Assertions.assertSame(doc4.lines[0], copy[1]);
    Assertions.assertSame(doc4.lines[1], copy[2]);

    doc4.deleteLine(1);
    Assertions.assertEquals(doc4.lines.length, 1);
    Assertions.assertSame(doc4.lines[0], copy[1]);

    Assertions.assertTrue(doc4.lines[0].totalStrLength > 0);
    doc4.deleteLine(0);
    Assertions.assertEquals(doc4.lines.length, 1);
    Assertions.assertEquals(doc4.lines[0].totalStrLength, 0);
  }

  @Test void deleteLinesTest() {
    Document doc5 = doc5();
    CodeLine[] copy = Arrays.copyOf(doc5.lines, doc5.lines.length);

    doc5.deleteLines(1, 3);
    Assertions.assertEquals(doc5.lines.length, 3);
    Assertions.assertEquals(doc5.lines[0], copy[0]);
    Assertions.assertEquals(doc5.lines[1], copy[3]);
    Assertions.assertEquals(doc5.lines[2], copy[4]);

    doc5.deleteLines(0, 1);
    Assertions.assertEquals(doc5.lines.length, 2);
    Assertions.assertEquals(doc5.lines[0], copy[3]);
    Assertions.assertEquals(doc5.lines[1], copy[4]);

    doc5.deleteLines(0, 2);
    Assertions.assertEquals(doc5.lines.length, 0);
  }

  @Test void deleteSelectedTest() {
    Document doc5 = doc5();
    Selection selection = new Selection();
    selection.startPos.set(0, 7);
    selection.endPos.set(3, 4);

    doc5.deleteSelected(selection);

    Assertions.assertEquals(doc5.lines.length, 2);
    Assertions.assertEquals(doc5.lines[0].makeString(), "This is demo is designed to investigate");
    Assertions.assertEquals(doc5.lines[0].elements.length, 3);

    selection.startPos.set(1, 2);
    selection.endPos.set(1, 8);

    doc5.deleteSelected(selection);

    Assertions.assertEquals(doc5.lines.length, 2);
    Assertions.assertEquals(doc5.lines[1].makeString(), "pence limits of this approach");
    Assertions.assertEquals(doc5.lines[1].elements.length, 2);

    selection.startPos.set(1, 2);
    selection.endPos.set(1, 20);

    doc5.deleteSelected(selection);

    Assertions.assertEquals(doc5.lines.length, 2);
    Assertions.assertEquals(doc5.lines[1].makeString(), "pe approach");
    Assertions.assertEquals(doc5.lines[1].elements.length, 2);

    selection.startPos.set(1, 0);
    selection.endPos.set(1, 3);

    doc5.deleteSelected(selection);

    Assertions.assertEquals(doc5.lines.length, 2);
    Assertions.assertEquals(doc5.lines[1].makeString(), "approach");
    Assertions.assertEquals(doc5.lines[1].elements.length, 1);
  }

  @Test void copyTest() {
    Document doc5 = doc5();
    CodeLine[] copy = Arrays.copyOf(doc5.lines, doc5.lines.length);
    Selection selection = new Selection();
    String copied;

    selection.startPos.set(0, 8);
    selection.endPos.set(0, 23);

    copied = doc5.copy(selection, false);
    Assertions.assertEquals(copied, "an experimental");
    Assertions.assertArrayEquals(doc5.lines, copy);

    copied = doc5.copy(selection, true);
    Assertions.assertEquals(copied, "an experimental");
    Assertions.assertEquals(doc5.lines[0].elements[0].s, "This is ");
    Assertions.assertEquals(doc5.lines[0].elements[1].s, " project");
    for (int i = 1; i < 5; i++) Assertions.assertEquals(doc5.lines[i], copy[i]);

    String expected = """
        a portable (Web + Desktop)
        editor in java and kotlin
        This demo is designed to investigate
        performance limits""";

    selection.startPos.set(1, 9);
    selection.endPos.set(4, 18);

    copied = doc5.copy(selection, false);
    Assertions.assertEquals(copied, expected);
  }

  @Test void insertLinesTest() {
    Document doc = new Document(new CodeLine());

    doc.insertLines(0, 0, new String[]{"This is an"});
    Assertions.assertEquals(doc.lines.length, 1);
    Assertions.assertEquals(doc.lines[0].elements.length, 1);
    Assertions.assertEquals(doc.lines[0].makeString(), "This is an");

    doc.insertLines(0, 10, new String[]{" experimental project"});
    Assertions.assertEquals(doc.lines.length, 1);
    Assertions.assertEquals(doc.lines[0].elements.length, 1);
    Assertions.assertEquals(doc.lines[0].makeString(), "This is an experimental project");
  }

  @Test void testCodeLineInsertEmpty() {
    String value = "abc 5";
    CodeLine cl = new CodeLine(new CodeElement(value));

    cl.insertToBegin("");
    Assertions.assertEquals(1, cl.elements.length);
    Assertions.assertEquals(value, cl.elements[0].s);
  }

  @Test
  void insertLinesTest2() {
    Document doc = new Document(new CodeLine());

    doc.insertLines(0, 0, new String[]{"line 1", "line 4", "line 5"});
    Assertions.assertEquals(doc.lines.length, 3);
    Assertions.assertEquals(doc.lines[0].elements[0].s, "line 1");
    Assertions.assertEquals(doc.lines[1].elements[0].s, "line 4");
    Assertions.assertEquals(doc.lines[2].elements[0].s, "line 5");

    doc.insertLines(1, 0, new String[]{"line 2", "line 3", ""});
    Assertions.assertEquals(doc.lines.length, 5);
    Assertions.assertEquals(doc.lines[0].elements[0].s, "line 1");
    Assertions.assertEquals(doc.lines[1].elements[0].s, "line 2");
    Assertions.assertEquals(doc.lines[2].elements[0].s, "line 3");
    Assertions.assertEquals(doc.lines[3].elements[0].s, "line 4");
    Assertions.assertEquals(doc.lines[4].elements[0].s, "line 5");
  }

  @Test
  void documentTextTest() {
    Document d = doc5();

    String text = """
        This is an experimental project
        to write a portable (Web + Desktop)
        editor in java and kotlin
        This demo is designed to investigate
        performance limits of this approach""";

    Assertions.assertEquals(d.makeString(), text);
  }

  @Test
  void documentFromTextTest() {
    String text = """
        This is an experimental project
        to write a portable (Web + Desktop)
        editor in java and kotlin
        This demo is designed to investigate
        performance limits of this approach
        """;

    Document d = new Document(SplitText.split(text));
    Assertions.assertEquals(d.makeString(), text);
  }

  static Document doc5() {
    return new Document(
        ab("This is an ", "experimental project"),
        ab("to write a portable"," (Web + Desktop)"),
        ab("editor in", " java and kotlin"),
        ab("This demo is ", "designed to investigate"),
        ab("performance limits", " of this approach")
    );
  }

  @Test
  void getPositionOffsetTest() {
    Document document = doc5();
    System.out.println(document.makeString());

    int l0 = document.lines[0].makeString().length();
    int l1 = document.lines[1].makeString().length();
    Pos at0 = document.getPositionAt(0);
    Assertions.assertEquals(at0, new Pos(0, 0));
    Assertions.assertEquals(document.getOffsetAt(at0), 0);
    Pos atl0 = document.getPositionAt(l0);
    Assertions.assertEquals(atl0, new Pos(0, l0));
    Assertions.assertEquals(document.getOffsetAt(atl0), l0);

    Pos atl1 = document.getPositionAt(l0 + 1);
    Assertions.assertEquals(atl1, new Pos(1, 0));
    Assertions.assertEquals(document.getOffsetAt(atl1), l0+1);

    Pos atl01 = document.getPositionAt(l0 + 1 + l1);
    Assertions.assertEquals(atl01, new Pos(1, l1));
    Assertions.assertEquals(document.getOffsetAt(atl01), l0 + 1 + l1);

    Pos atl20 = document.getPositionAt(l0 + 1 + l1 + 1);
    Assertions.assertEquals(atl20, new Pos(2, 0));
    Assertions.assertEquals(document.getOffsetAt(atl20), l0 + 1 + l1 + 1);

    Pos atlMax = document.getPositionAt(document.getFullLength() * 2);
    Assertions.assertEquals(atlMax, new Pos(document.length(), 0));
    Assertions.assertEquals(document.getOffsetAt(atlMax), document.getFullLength());
  }

  @Test void setEmptyText() {
    Document d = new Document(ab(""));
    char[] chars = d.getChars();
    Assertions.assertArrayEquals(chars, new char[0]);
  }

  @Test void testLength() {
    Document document = doc5();
    String docString = document.makeString();
    int fullLength = document.getFullLength();
    Assertions.assertEquals(docString.length(), fullLength);
    char[] chars = document.getChars();
    Assertions.assertEquals(new String(chars), docString);
  }

  @Test void testGetLineStartInd() {
    // line starts: 0, 3, 6, 9, 11
    Document document = doc4();
    for (int i = 0; i < 4; i++) {
      int lineStart = document.getLineStartInd(i);
      Assertions.assertEquals(lineStart, i * 3);

    }
    int lineStart = document.getLineStartInd(4);
    Assertions.assertEquals(lineStart, 4 * 3 - 1);

    try {
      int l5 = document.getLineStartInd(5);
      Assertions.fail();
    } catch (ArrayIndexOutOfBoundsException ignored) {
    }
  }

  @Test void testDiffOnFirstLine() {
    Document document = doc5();
    document.applyChange(0, 2, new CodeLine[] {});
    Diff lastDiff = document.lastDiff()[0];
    String change = lastDiff.change;

    Assertions.assertFalse(change.startsWith("\n"));
    Assertions.assertTrue(change.endsWith("\n"));
  }

  @Test void testApplyChangeOnLastLine() {
    Document document = doc5();
    document.applyChange(2, document.length(), new CodeLine[] {});
    Diff lastDiff = document.lastDiff()[0];
    String change = lastDiff.change;

    Assertions.assertTrue(change.startsWith("\n"));
    Assertions.assertFalse(change.endsWith("\n"));
  }

  @Test void testApplyChangeOnFirstLineToLast() {
    Document document = doc5();
    document.applyChange(0, document.length(), new CodeLine[] {});
    Diff lastDiff = document.lastDiff()[0];
    String change = lastDiff.change;

    Assertions.assertFalse(change.startsWith("\n"));
    Assertions.assertFalse(change.endsWith("\n"));
  }

  @Test void testApplyChangeInMiddle() {
    Document document = doc5();
    document.applyChange(2, 4, new CodeLine[] {});
    Diff lastDiff = document.lastDiff()[0];
    String change = lastDiff.change;

    Assertions.assertTrue(change.startsWith("\n"));
    Assertions.assertFalse(change.endsWith("\n"));
  }

  @Test void testThrows() {

  }

  static Document doc4() {
    return new Document(ab("AB"), ab("CD"), ab("EF"), ab("GH"));
  }

  static CodeLine line() {
    return ab("AB");
  }

  static CodeLine ab(String t) {
    return new CodeLine(abElement(t));
  }

  static CodeLine ab(String a, String b) {
    return new CodeLine(abElement(a), abElement(b));
  }

  static CodeElement abElement(String t) {
    return new CodeElement(t);
  }
}
