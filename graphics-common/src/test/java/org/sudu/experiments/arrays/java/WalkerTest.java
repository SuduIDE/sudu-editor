package org.sudu.experiments.parser.java;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.sudu.experiments.parser.common.Pos;
import org.sudu.experiments.parser.java.model.JavaClass;
import org.sudu.experiments.parser.java.model.JavaField;
import org.sudu.experiments.parser.java.model.JavaMethod;
import org.sudu.experiments.parser.java.parser.JavaFullParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class WalkerTest {

  @Test
  public void testFieldResolve() {
    String source = readFile("java/TestFieldResolve.java");
    JavaFullParser parser = new JavaFullParser();
    parser.parse(source);
    JavaClass javaClass = parser.getJavaClass().nestedClasses.get(0);

    var usageToDefinition = parser.usageToDefinition;

    JavaField field1 = javaClass.getField("field1");
    JavaField field2 = javaClass.getField("field2");
    JavaField field3 = javaClass.getField("field3");
    JavaField field4 = javaClass.getField("field4");
    JavaField field5 = javaClass.getField("field5");

    ArrayList<String> list = new ArrayList<>();
    list.add(null);

    JavaMethod foo1 = javaClass.getMethod("foo", List.of("int"));
    JavaMethod foo2 = javaClass.getMethod("foo", List.of("String"));
    JavaMethod foo3 = javaClass.getMethod("foo", List.of());
    JavaMethod foo4 = javaClass.getMethod("foo", List.of("int", "int"));
    JavaMethod foo5 = javaClass.getMethod("foo", list);

    var field1Usages = new HashSet<>(Set.of(
        new Pos(10, 9),
        new Pos(11, 18),
        new Pos(13, 11),
        new Pos(14, 11),
        new Pos(15, 19),
        new Pos(16, 20),
        new Pos(16, 37),
        new Pos(18, 15)
    ));

    var field2Usages = new HashSet<>(Set.of(
        new Pos(14, 24),
        new Pos(15, 6),
        new Pos(16, 6),
        new Pos(19, 10)
    ));

    var field3Usages = new HashSet<>(Set.of(
        new Pos(20, 4),
        new Pos(21, 9)
    ));

    Assertions.assertEquals(javaClass.name, "TestFieldResolve");
    Assertions.assertTrue(javaClass.nestedClasses.isEmpty());

    Assertions.assertNotNull(field1);
    Assertions.assertNotNull(field2);
    Assertions.assertNotNull(field3);
    Assertions.assertNotNull(field4);
    Assertions.assertNull(field5);

    Assertions.assertEquals("int", field1.type);
    Assertions.assertEquals("int", field2.type);
    Assertions.assertEquals("String", field3.type);
    Assertions.assertEquals("Number", field4.type);

    Assertions.assertEquals(new Pos(5, 6), field1.position);
    Assertions.assertEquals(new Pos(5, 14), field2.position);
    Assertions.assertEquals(new Pos(6, 9), field3.position);
    Assertions.assertEquals(new Pos(7, 9), field4.position);

    Assertions.assertNotNull(foo1);
    Assertions.assertNull(foo2);
    Assertions.assertNull(foo3);
    Assertions.assertNull(foo4);
    Assertions.assertSame(foo1, foo5);

    Assertions.assertEquals("void", foo1.type);
    Assertions.assertEquals(List.of("int"), foo1.argsTypes);

    Assertions.assertTrue(usageToDefinition.containsValue(field1.position));
    Assertions.assertTrue(usageToDefinition.containsValue(field2.position));

    Assertions.assertTrue(checkUsages(usageToDefinition, field1Usages, field1.position));
    Assertions.assertTrue(checkUsages(usageToDefinition, field2Usages, field2.position));
    Assertions.assertTrue(checkUsages(usageToDefinition, field3Usages, field3.position));
  }

  @Test
  public void testMethodResolve() {
    String source = readFile("java/TestMethodResolve.java");
    JavaFullParser parser = new JavaFullParser();
    parser.parse(source);
    JavaClass javaClass = parser.getJavaClass().nestedClasses.get(0);

    var usageToDefinition = parser.usageToDefinition;

    JavaMethod a = javaClass.getMethod("a", List.of());
    JavaMethod b = javaClass.getMethod("b", List.of("int"));
    JavaMethod c = javaClass.getMethod("c", List.of());
    JavaMethod rec = javaClass.getMethod("rec", List.of("int"));
    JavaMethod foo0 = javaClass.getMethod("foo", List.of());
    JavaMethod foo1 = javaClass.getMethod("foo", List.of("int"));
    JavaMethod foo2 = javaClass.getMethod("foo", List.of("int", "int"));

    var bUsages = new HashSet<>(Set.of(
        new Pos(6, 4),
        new Pos(7, 4),
        new Pos(8, 4)
    ));

    var cUsages = new HashSet<>(Set.of(
        new Pos(8, 6),
        new Pos(8, 17)
    ));

    var recUsages = new HashSet<>(Set.of(
        new Pos(24, 11),
        new Pos(24, 24)
    ));

    Assertions.assertNotNull(a);
    Assertions.assertNotNull(b);
    Assertions.assertNotNull(c);
    Assertions.assertNotNull(rec);
    Assertions.assertNotNull(foo0);
    Assertions.assertNotNull(foo1);
    Assertions.assertNotNull(foo2);

    Assertions.assertEquals("void", a.type);
    Assertions.assertEquals("int", b.type);
    Assertions.assertEquals("int", c.type);
    Assertions.assertEquals("int", rec.type);
    Assertions.assertEquals("void", foo0.type);
    Assertions.assertEquals("void", foo1.type);
    Assertions.assertEquals("void", foo2.type);

    Assertions.assertEquals(new Pos(5, 7), a.position);
    Assertions.assertEquals(new Pos(14, 6), b.position);
    Assertions.assertEquals(new Pos(18, 6), c.position);
    Assertions.assertEquals(new Pos(22, 6), rec.position);
    Assertions.assertEquals(new Pos(27, 7), foo0.position);
    Assertions.assertEquals(new Pos(29, 7), foo1.position);
    Assertions.assertEquals(new Pos(31, 7), foo2.position);

    Assertions.assertTrue(checkUsages(usageToDefinition, new HashSet<>(), a.position));
    Assertions.assertTrue(checkUsages(usageToDefinition, bUsages, b.position));
    Assertions.assertTrue(checkUsages(usageToDefinition, cUsages, c.position));
    Assertions.assertTrue(checkUsages(usageToDefinition, recUsages, rec.position));
    Assertions.assertTrue(checkUsage(usageToDefinition, new Pos(9, 4), foo0.position));
    Assertions.assertTrue(checkUsage(usageToDefinition, new Pos(10, 4), foo1.position));
    Assertions.assertTrue(checkUsage(usageToDefinition, new Pos(11, 4), foo2.position));
  }

  @Test
  public void testMethodTypeResolve() {
    String source = readFile("java/TestMethodTypeResolve.java");
    JavaFullParser parser = new JavaFullParser();
    parser.parse(source);
    JavaClass javaClass = parser.getJavaClass().nestedClasses.get(0);

    var usageToDefinition = parser.usageToDefinition;

    JavaMethod intResolve = javaClass.getMethod("intResolve", List.of());
    JavaMethod booleanResolve = javaClass.getMethod("booleanResolve", List.of());
    JavaMethod stringResolve = javaClass.getMethod("stringResolve", List.of());
    JavaMethod classResolve = javaClass.getMethod("classResolve", List.of());
    JavaMethod intFun = javaClass.getMethod("fun", List.of("int"));
    JavaMethod booleanFun = javaClass.getMethod("fun", List.of("boolean"));
    JavaMethod stringFun = javaClass.getMethod("fun", List.of("String"));
    JavaMethod classFun = javaClass.getMethod("fun", List.of("RandomClass"));

    var intFunUsages = new HashSet<>(Set.of(
        new Pos(11, 4),
        new Pos(12, 4),
        new Pos(13, 4),
        new Pos(14, 4),
        new Pos(15, 4),
        new Pos(16, 4),
        new Pos(17, 4)
    ));

    var booleanFunUsages = new HashSet<>(Set.of(
        new Pos(29, 4),
        new Pos(30, 4),
        new Pos(31, 4),
        new Pos(32, 4),
        new Pos(33, 4),
        new Pos(34, 4),
        new Pos(35, 4),
        new Pos(36, 4)
    ));

    var stringFunUsages = new HashSet<>(Set.of(
        new Pos(46, 4),
        new Pos(47, 4),
        new Pos(48, 4),
        new Pos(49, 4),
        new Pos(50, 4),
        new Pos(51, 4)
    ));

    var classFunUsages = new HashSet<>(Set.of(
        new Pos(62, 4),
        new Pos(63, 4),
        new Pos(64, 4),
        new Pos(65, 4),
        new Pos(66, 4),
        new Pos(67, 4),
        new Pos(68, 4),
        new Pos(69, 4)
    ));

    Assertions.assertNotNull(intResolve);
    Assertions.assertNotNull(booleanResolve);
    Assertions.assertNotNull(stringResolve);
    Assertions.assertNotNull(classResolve);
    Assertions.assertNotNull(intFun);
    Assertions.assertNotNull(booleanFun);
    Assertions.assertNotNull(stringFun);
    Assertions.assertNotNull(classFun);

    Assertions.assertEquals("void", intResolve.type);
    Assertions.assertEquals("void", booleanResolve.type);
    Assertions.assertEquals("void", stringResolve.type);
    Assertions.assertEquals("void", classResolve.type);
    Assertions.assertEquals("void", intFun.type);
    Assertions.assertEquals("void", booleanFun.type);
    Assertions.assertEquals("void", stringFun.type);
    Assertions.assertEquals("void", classFun.type);

    Assertions.assertTrue(checkUsages(usageToDefinition, intFunUsages, intFun.position));
    Assertions.assertTrue(checkUsages(usageToDefinition, booleanFunUsages, booleanFun.position));
    Assertions.assertTrue(checkUsages(usageToDefinition, stringFunUsages, stringFun.position));
    Assertions.assertTrue(checkUsages(usageToDefinition, classFunUsages, classFun.position));
  }

  private boolean checkUsages(
      Map<Pos, Pos> usageToDef,
      HashSet<Pos> expUsages,
      Pos definition
  ) {
    Set<Pos> gotUsages = new HashSet<>();
    for (var entry: usageToDef.entrySet()) {
      if (entry.getValue().equals(definition)) gotUsages.add(entry.getKey());
    }
    return expUsages.equals(gotUsages);
  }

  private boolean checkUsage(Map<Pos, Pos> usageToDef, Pos key, Pos value) {
    var usage = usageToDef.get(key);
    return usage != null && usage.equals(value);
  }

  private String readFile(String filename) {
    try {
      return Files.readString(Path.of("src", "test", "resources", filename))
          .replace("\r", "");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
