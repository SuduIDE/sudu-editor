package org.sudu.experiments.demo;

import org.junit.jupiter.api.Assertions;
import org.sudu.experiments.math.ArrayOp;

import java.util.ArrayList;

class DocumentTest {

  public static void main(String[] args) {
    concatTest();
    concatTest2();
    concat3Test();
    newLineTest();
    newLineTest2();
    deleteAtTest();
    insertAtTest();
    deleteCharTest();
  }

  static void deleteCharTest() {
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

    CodeLine cl = new CodeLine(new CodeElement("A", null));
    cl.deleteAt(0);
    Assertions.assertEquals(cl.elements.length, 0);
  }

  private static void concatTest() {
    Document d = doc4();
    d.concatLines(0);
    Assertions.assertEquals(d.document.length, 3);
    Assertions.assertEquals(d.document[0].elements.length, 2);
    Assertions.assertEquals(d.document[0].elements[0].s, "AB");
    Assertions.assertEquals(d.document[0].elements[1].s, "CD");
    Assertions.assertEquals(d.document[1].elements.length, 1);
    Assertions.assertEquals(d.document[1].elements[0].s, "EF");
    Assertions.assertEquals(d.document[2].elements.length, 1);
    Assertions.assertEquals(d.document[2].elements[0].s, "GH");

    d.concatLines(0);

    Assertions.assertEquals(d.document.length, 2);
    Assertions.assertEquals(d.document[0].elements.length, 3);
    Assertions.assertEquals(d.document[0].elements[0].s, "AB");
    Assertions.assertEquals(d.document[0].elements[1].s, "CD");
    Assertions.assertEquals(d.document[0].elements[2].s, "EF");
    Assertions.assertEquals(d.document[1].elements.length, 1);
    Assertions.assertEquals(d.document[1].elements[0].s, "GH");

    d.concatLines(0);

    Assertions.assertEquals(d.document.length, 1);
    Assertions.assertEquals(d.document[0].elements.length, 4);
    Assertions.assertEquals(d.document[0].elements[0].s, "AB");
    Assertions.assertEquals(d.document[0].elements[1].s, "CD");
    Assertions.assertEquals(d.document[0].elements[2].s, "EF");
    Assertions.assertEquals(d.document[0].elements[3].s, "GH");
  }

  private static void concatTest2() {
    Document d = doc4();
    d.concatLines(1);
    d.concatLines(1);

    Assertions.assertEquals(d.document.length, 2);
    Assertions.assertEquals(d.document[1].elements.length, 3);
    Assertions.assertEquals(d.document[0].elements[0].s, "AB");
    Assertions.assertEquals(d.document[1].elements[0].s, "CD");
    Assertions.assertEquals(d.document[1].elements[1].s, "EF");
    Assertions.assertEquals(d.document[1].elements[2].s, "GH");
  }

  private static void concat3Test() {
    Document d = doc4();
    d.concatLines(2);

    Assertions.assertEquals(d.document.length, 3);
    Assertions.assertEquals(d.document[0].elements.length, 1);
    Assertions.assertEquals(d.document[1].elements.length, 1);
    Assertions.assertEquals(d.document[2].elements.length, 2);
    Assertions.assertEquals(d.document[0].elements[0].s, "AB");
    Assertions.assertEquals(d.document[1].elements[0].s, "CD");
    Assertions.assertEquals(d.document[2].elements[0].s, "EF");
    Assertions.assertEquals(d.document[2].elements[1].s, "GH");
  }

  private static Document doc4() {
    return new Document(ab("AB"), ab("CD"), ab("EF"), ab("GH"));
  }

  static void newLineTest() {
    Document a = new Document(3);
    a.newLineOp(0,0);

    Assertions.assertEquals(a.document.length,4);
    Assertions.assertEquals(a.document[0].elements.length,0);

    Document b = new Document(3);

    b.newLineOp(0,b.strLength(0));

    Assertions.assertEquals(b.document.length,4);
    Assertions.assertEquals(b.document[1].elements.length,0);

    Document c = new Document(3);
    c.newLineOp(c.length() - 1, 0);

    Assertions.assertEquals(c.document.length,4);
    Assertions.assertEquals(c.document[2].elements.length,0);

    Document d = new Document(3);
    d.newLineOp(d.length() - 1, d.strLength(d.length() - 1));

    Assertions.assertEquals(d.document.length,4);
    Assertions.assertEquals(d.document[3].elements.length,0);
  }

  static void newLineTest2() {
    ArrayList<Document> list = new ArrayList<>();
    {
      Document a = new Document(line());
      a.newLineOp(0, 1);
      Assertions.assertEquals(a.document.length, 2);
      Assertions.assertEquals(a.document[0].elements.length, 1);
      Assertions.assertEquals(a.document[0].elements[0].s, "A");
      Assertions.assertEquals(a.document[1].elements.length, 1);
      Assertions.assertEquals(a.document[1].elements[0].s, "B");
      list.add(a);
    }
    {
      Document b = new Document(new CodeLine(abElement("AB"), abElement("CD")));
      b.newLineOp(0, 1);

      Assertions.assertEquals(b.document.length, 2);
      Assertions.assertEquals(b.document[0].elements.length, 1);
      Assertions.assertEquals(b.document[0].elements[0].s, "A");
      Assertions.assertEquals(b.document[1].elements.length, 2);
      Assertions.assertEquals(b.document[1].elements[0].s, "B");
      Assertions.assertEquals(b.document[1].elements[1].s, "CD");
      list.add(b);
    }
    {
      Document c = new Document(
          new CodeLine(abElement("AB"), abElement("CD")));
      c.newLineOp(0, 2);
      list.add(c);

      Assertions.assertEquals(c.document.length, 2);
      Assertions.assertEquals(c.document[0].elements.length, 1);
      Assertions.assertEquals(c.document[0].elements[0].s, "AB");
      Assertions.assertEquals(c.document[1].elements.length, 1);
      Assertions.assertEquals(c.document[1].elements[0].s, "CD");
    }
    {
      Document d = new Document(
          new CodeLine(abElement("AB"), abElement("CD")));
      d.newLineOp(0, 3);
      list.add(d);

      Assertions.assertEquals(d.document.length, 2);
      Assertions.assertEquals(d.document[0].elements.length, 2);
      Assertions.assertEquals(d.document[0].elements[0].s, "AB");
      Assertions.assertEquals(d.document[0].elements[1].s, "C");
      Assertions.assertEquals(d.document[1].elements.length, 1);
      Assertions.assertEquals(d.document[1].elements[0].s, "D");
    }
    {
      Document e = new Document(
          new CodeLine(abElement("AB"), abElement("CD"), abElement("EF")));
      e.newLineOp(0, 3);
      list.add(e);
      Assertions.assertEquals(e.document.length, 2);
      Assertions.assertEquals(e.document[0].elements.length, 2);
      Assertions.assertEquals(e.document[0].elements[0].s, "AB");
      Assertions.assertEquals(e.document[0].elements[1].s, "C");
      Assertions.assertEquals(e.document[1].elements.length, 2);
      Assertions.assertEquals(e.document[1].elements[0].s, "D");
      Assertions.assertEquals(e.document[1].elements[1].s, "EF");
    }
//    System.out.println("r = " + list);
  }

  static void deleteAtTest() {
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

  static void insertAtTest() {
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

  static CodeLine line() {
    return ab("AB");
  }

  static CodeLine ab(String t) {
    return new CodeLine(abElement(t));
  }

  private static CodeElement abElement(String t) {
    return new CodeElement(t, Colors.defaultText);
  }
}