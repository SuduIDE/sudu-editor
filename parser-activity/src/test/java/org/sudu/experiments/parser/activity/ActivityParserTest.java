package org.sudu.experiments.parser.activity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.sudu.experiments.parser.activity.graph.expr.BinaryExpr;
import org.sudu.experiments.parser.activity.graph.stat.*;

public class ActivityParserTest {

  @Test
  void testIdActivity() {
    var activityChars = readFile("id.activity").toCharArray();
    var parser = new ActivityFullParser();
    var result = parser.parseActivity(activityChars);
    var activity = parser.activity;
    var id = activity.block().get(0);

    checkResultListNotEmpty(result);
    Assertions.assertEquals(1, activity.block().size());
    Assertions.assertInstanceOf(Id.class, id);
    Assertions.assertEquals("A1", id.name());
  }

  @Test
  void testSelectActivity() {
    var activityChars = readFile("select.activity").toCharArray();
    var parser = new ActivityFullParser();
    var result = parser.parseActivity(activityChars);
    var activity = parser.activity;

    checkResultListNotEmpty(result);
    Assertions.assertEquals(1, activity.block().size());
    var select = activity.block().get(0);
    Assertions.assertInstanceOf(Select.class, select);
    var block = ((Select) select).block();
    Assertions.assertEquals(3, block.size());
    Assertions.assertInstanceOf(Id.class, block.get(0));
    Assertions.assertEquals("A1", block.get(0).name());
    Assertions.assertInstanceOf(Id.class, block.get(1));
    Assertions.assertEquals("B1", block.get(1).name());
    Assertions.assertInstanceOf(Id.class, block.get(2));
    Assertions.assertEquals("C1", block.get(2).name());
  }

  @Test
  void testSelectWithConditionsActivity() {
    var activityChars = readFile("selectWithConditions.activity").toCharArray();
    var parser = new ActivityFullParser();
    var result = parser.parseActivity(activityChars);
    var activity = parser.activity;

    checkResultListNotEmpty(result);
    Assertions.assertEquals(3, activity.block().size());
    Assertions.assertInstanceOf(Repeat.class, activity.block().get(0));
    Assertions.assertInstanceOf(Select.class, activity.block().get(1));
    Assertions.assertInstanceOf(Select.class, activity.block().get(2));

    var select = (Select) activity.block().get(2);

    Assertions.assertEquals(3, select.block().size());
    Assertions.assertInstanceOf(Id.class, select.block().get(0));
    Assertions.assertEquals("A1", select.block().get(0).name());
    Assertions.assertInstanceOf(Id.class, select.block().get(1));
    Assertions.assertEquals("B1", select.block().get(1).name());
    Assertions.assertInstanceOf(Id.class, select.block().get(2));
    Assertions.assertEquals("C1", select.block().get(2).name());
    Assertions.assertNotNull(select.conditions.get(0));
    Assertions.assertNull(select.conditions.get(1));
    Assertions.assertNotNull(select.conditions.get(2));
  }

  @Test
  void testIfActivity() {
    var activityChars = readFile("if.activity").toCharArray();
    var parser = new ActivityFullParser();
    var result = parser.parseActivity(activityChars);
    var activity = parser.activity;

    checkResultListNotEmpty(result);
    Assertions.assertEquals(4, activity.block().size());
    var iff = activity.block().get(3);
    Assertions.assertInstanceOf(If.class, iff);
    var cond = (((If) iff).cond);
    var ifBlock = (((If) iff).ifBlock);
    var elseBlock = (((If) iff).elseBlock);
    Assertions.assertNotNull(cond);
    Assertions.assertNotNull(ifBlock);
    Assertions.assertEquals(1, ifBlock.size());
    Assertions.assertNotNull(elseBlock);
    Assertions.assertEquals(1, elseBlock.size());
    Assertions.assertInstanceOf(BinaryExpr.class, cond);
    Assertions.assertInstanceOf(Select.class, ifBlock.get(0));
    Assertions.assertInstanceOf(Id.class, elseBlock.get(0));
  }

  @Test
  void testRepeat() {
    var activityChars = readFile("repeat.activity").toCharArray();
    var parser = new ActivityFullParser();
    var result = parser.parseActivity(activityChars);
    var activity = parser.activity;

    checkResultListNotEmpty(result);
    Assertions.assertEquals(1, activity.block().size());
    var repeat = activity.block().get(0);
    Assertions.assertInstanceOf(Repeat.class, repeat);
    Assertions.assertEquals(10, ((Repeat) repeat).count);
    var block = ((Repeat) repeat).block();
    Assertions.assertEquals(3, block.size());
    Assertions.assertInstanceOf(Id.class, block.get(0));
    Assertions.assertEquals("A1", block.get(0).name());
    Assertions.assertInstanceOf(Id.class, block.get(1));
    Assertions.assertEquals("B1", block.get(1).name());
    Assertions.assertInstanceOf(Id.class, block.get(2));
    Assertions.assertEquals("C1", block.get(2).name());
  }

  @Test
  void testSequence() {
    var activityChars = readFile("sequence.activity").toCharArray();
    var parser = new ActivityFullParser();
    var result = parser.parseActivity(activityChars);
    var activity = parser.activity;

    checkResultListNotEmpty(result);
    Assertions.assertEquals(1, activity.block().size());
    var sequence = activity.block().get(0);
    Assertions.assertInstanceOf(Sequence.class, sequence);
    var block = ((Sequence) sequence).block();
    Assertions.assertEquals(3, block.size());
    Assertions.assertInstanceOf(Id.class, block.get(0));
    Assertions.assertEquals("A1", block.get(0).name());
    Assertions.assertInstanceOf(Id.class, block.get(1));
    Assertions.assertEquals("B1", block.get(1).name());
    Assertions.assertInstanceOf(Id.class, block.get(2));
    Assertions.assertEquals("C1", block.get(2).name());
  }

  @Test
  void testSchedule() {
    var activityChars = readFile("schedule.activity").toCharArray();
    var parser = new ActivityFullParser();
    var result = parser.parseActivity(activityChars);
    var activity = parser.activity;

    checkResultListNotEmpty(result);
    Assertions.assertEquals(1, activity.block().size());
    var schedule = activity.block().get(0);
    Assertions.assertInstanceOf(Schedule.class, schedule);
    var block = ((Schedule) schedule).block();
    Assertions.assertEquals(3, block.size());
    Assertions.assertInstanceOf(Id.class, block.get(0));
    Assertions.assertEquals("A1", block.get(0).name());
    Assertions.assertInstanceOf(Id.class, block.get(1));
    Assertions.assertEquals("B1", block.get(1).name());
    Assertions.assertInstanceOf(Id.class, block.get(2));
    Assertions.assertEquals("C1", block.get(2).name());
  }

  @Test
  void testRandom() {
    var activityChars = readFile("random.activity").toCharArray();
    var parser = new ActivityFullParser();
    var result = parser.parseActivity(activityChars);
    var activity = parser.activity;

    checkResultListNotEmpty(result);
    Assertions.assertEquals(1, activity.block().size());
    var random = activity.block().get(0);
    Assertions.assertInstanceOf(Random.class, random);
    var block = ((Random) random).block();
    Assertions.assertEquals(3, ((Random) random).count);
    Assertions.assertEquals(3, block.size());
    Assertions.assertInstanceOf(Id.class, block.get(0));
    Assertions.assertEquals("A1", block.get(0).name());
    Assertions.assertInstanceOf(Id.class, block.get(1));
    Assertions.assertEquals("B1", block.get(1).name());
    Assertions.assertInstanceOf(Id.class, block.get(2));
    Assertions.assertEquals("C1", block.get(2).name());
  }

  @Test
  void testParse() {
    var activityChars = readFile("a.activity").toCharArray();
    var parser = new ActivityFullParser();
    var result = parser.parseActivity(activityChars);
    var activity = parser.activity;
  }

  private void checkResultListNotEmpty(List<Object> result) {
    Assertions.assertFalse(result.isEmpty());
    Assertions.assertFalse(result.size() < 3);
    Assertions.assertInstanceOf(int[].class, result.get(0));
    Assertions.assertInstanceOf(String.class, result.get(1));
    Assertions.assertInstanceOf(String.class, result.get(2));
  }

  private String readFile(String filename) {
    try {
      var url = getClass().getClassLoader().getResource(filename);
      if (url == null) throw new IllegalArgumentException("Illegal resource name: " + filename);
      return Files.readString(Path.of(url.toURI()));
    } catch (URISyntaxException | IOException e) {
      throw new RuntimeException(e);
    }
  }

}
