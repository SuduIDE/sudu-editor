package org.sudu.experiments.parser.activity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.sudu.experiments.parser.activity.graph.Node;
import org.sudu.experiments.parser.activity.graph.expr.BinaryExpr;
import org.sudu.experiments.parser.activity.graph.expr.ExprKind;
import org.sudu.experiments.parser.activity.graph.stat.*;

public class ActivityToDag2Test {

  private static final String ARROW = "-->";
  private static final String ID = "id";
  private static final String INITIAL = "((Initial))";
  private static final String FINAL = "((Final))";

  @Test
  void testIdsDag() {
    var activity = new Activity();
    activity.block().add(new Id("A1"));
    activity.block().add(new Id("A2"));
    activity.block().add(new Id("A3"));

    var initial = activity.dag2(false);
    var mermaid = initial.printRecDag2(null);

    Assertions.assertEquals(1, initial.edges.size());
    testNode(mermaid, initial.getId(), INITIAL);

    var a1 = initial.edges.get(0).to;
    Assertions.assertEquals("A1", a1.name());
    Assertions.assertEquals(1, a1.edges.size());
    testNode(mermaid, a1);
    testPath(mermaid, initial, a1);

    var a2 = a1.edges.get(0).to;
    Assertions.assertEquals("A2", a2.name());
    Assertions.assertEquals(1, a2.edges.size());
    testNode(mermaid, a2);
    testPath(mermaid, a1, a2);

    var a3 = a2.edges.get(0).to;
    Assertions.assertEquals("A3", a3.name());
    Assertions.assertEquals(1, a3.edges.size());
    testNode(mermaid, a3);
    testPath(mermaid, a2, a3);

    var finale = a3.edges.get(0).to;
    testPath(mermaid, a3, finale);
    Assertions.assertEquals(0, finale.edges.size());
    testNode(mermaid, finale.getId(), FINAL);
  }

  @Test
  void testSelectDag() {
    var activity = new Activity();
    var select = new Select();
    select.block().add(new Id("A1"));
    select.conditions.add(null);
    select.block().add(new Id("A2"));
    select.conditions.add(null);
    select.block().add(new Id("A3"));
    select.conditions.add(null);
    activity.block().add(select);

    var initial = activity.dag2(false);
    var mermaid = initial.printRecDag2(null);

    Assertions.assertEquals(3, initial.edges.size());
    testNode(mermaid, initial.getId(), INITIAL);

    var a1 = initial.edges.get(0).to;
    var a2 = initial.edges.get(1).to;
    var a3 = initial.edges.get(2).to;

    testNode(mermaid, a1);
    testNode(mermaid, a2);
    testNode(mermaid, a3);
    testPath(mermaid, initial, a1);
    testPath(mermaid, initial, a2);
    testPath(mermaid, initial, a3);
    Assertions.assertEquals(1, a1.edges.size());
    Assertions.assertEquals(1, a2.edges.size());
    Assertions.assertEquals(1, a3.edges.size());
    Assertions.assertSame(a1.edges.get(0).to, a2.edges.get(0).to);
    Assertions.assertSame(a2.edges.get(0).to, a3.edges.get(0).to);
    Assertions.assertSame(a3.edges.get(0).to, a1.edges.get(0).to);

    var finale = a1.edges.get(0).to;
    Assertions.assertEquals(0, finale.edges.size());
    testNode(mermaid, finale.getId(), FINAL);
    testPath(mermaid, a1, finale);
    testPath(mermaid, a2, finale);
    testPath(mermaid, a3, finale);
  }

  @Test
  void testIfDag() {
    var activity = new Activity();
    activity.block().add(new Id("A1"));
    activity.block().add(new Id("B1"));
    activity.block().add(new Id("C1"));

    var select = new Select();
    select.block().add(new Id("T1"));
    select.conditions.add(null);

    var iff = new If();
    var andCond = new BinaryExpr(ExprKind.And);
    var orCond = new BinaryExpr(ExprKind.Or);
    andCond.list().add(new Id("A1"));
    andCond.list().add(new Id("B1"));
    orCond.list().add(andCond);
    orCond.list().add(new Id("C1"));
    iff.cond = orCond;
    iff.ifBlock.add(select);
    iff.elseBlock.add(new Id("F1"));
    activity.block().add(iff);

    var initial = activity.dag2(false);
    var mermaid = initial.printRecDag2(null);

    Assertions.assertEquals(1, initial.edges.size());
    testNode(mermaid, initial.getId(), INITIAL);

    var a1 = initial.edges.get(0).to;
    Assertions.assertEquals("A1", a1.name());
    Assertions.assertEquals(1, a1.edges.size());
    testNode(mermaid, a1);
    testPath(mermaid, initial, a1);

    var b1 = a1.edges.get(0).to;
    Assertions.assertEquals("B1", b1.name());
    Assertions.assertEquals(1, b1.edges.size());
    testNode(mermaid, b1);
    testPath(mermaid, a1, b1);

    var c1 = b1.edges.get(0).to;
    Assertions.assertEquals("C1", c1.name());
    Assertions.assertEquals(1, c1.edges.size());
    testNode(mermaid, c1);
    testPath(mermaid, b1, c1);

    var ifNode = (If) c1.edges.get(0).to;
    Assertions.assertEquals(2, ifNode.edges.size());
    testIfNode(mermaid, ifNode);
    testPath(mermaid, c1, ifNode);

    var trueNode = ifNode.edges.get(0).to;
    Assertions.assertEquals(1, trueNode.edges.size());
    testPath(mermaid, ifNode, trueNode, ifNode.cond.toString());
    testNode(mermaid, trueNode);

    var elseBlock = ifNode.edges.get(1).to;
    Assertions.assertEquals(1, elseBlock.edges.size());
    testPath(mermaid, ifNode, elseBlock, "else");
    testNode(mermaid, elseBlock);

    Assertions.assertSame(trueNode.edges.get(0).to, elseBlock.edges.get(0).to);
    var finale = trueNode.edges.get(0).to;
    Assertions.assertEquals(0, finale.edges.size());
    testNode(mermaid, finale.getId(), FINAL);
    testPath(mermaid, trueNode, finale);
    testPath(mermaid, elseBlock, finale);
  }

  @Test
  void testRandomDag() {
    Random.setGlobalSeedAndInitiateRandom(42);

    var activity = new Activity();
    var select = new Random(3);
    select.block().add(new Id("A1"));
    select.block().add(new Id("A2"));
    select.block().add(new Id("A3"));
    activity.block().add(select);

    var initial = activity.dag2(false);
    var mermaid = initial.printRecDag2(null);

    Assertions.assertEquals(3, initial.edges.size());
    testNode(mermaid, initial.getId(), INITIAL);

    var r11 = initial.edges.get(0).to;
    var r12 = initial.edges.get(1).to;
    var r13 = initial.edges.get(2).to;

    testNode(mermaid, r11);
    testNode(mermaid, r12);
    testNode(mermaid, r13);
    testPath(mermaid, initial, r11);
    testPath(mermaid, initial, r12);
    testPath(mermaid, initial, r13);
    Assertions.assertEquals(1, r11.edges.size());
    Assertions.assertEquals(1, r12.edges.size());
    Assertions.assertEquals(1, r13.edges.size());

    var r21 = r11.edges.get(0).to;
    var r22 = r12.edges.get(0).to;
    var r23 = r13.edges.get(0).to;

    testNode(mermaid, r21);
    testNode(mermaid, r22);
    testNode(mermaid, r23);
    testPath(mermaid, r11, r21);
    testPath(mermaid, r12, r22);
    testPath(mermaid, r13, r23);
    Assertions.assertEquals(1, r21.edges.size());
    Assertions.assertEquals(1, r22.edges.size());
    Assertions.assertEquals(1, r23.edges.size());

    var r31 = r21.edges.get(0).to;
    var r32 = r22.edges.get(0).to;
    var r33 = r23.edges.get(0).to;

    testNode(mermaid, r31);
    testNode(mermaid, r32);
    testNode(mermaid, r33);
    testPath(mermaid, r21, r31);
    testPath(mermaid, r22, r32);
    testPath(mermaid, r23, r33);
    Assertions.assertEquals(1, r31.edges.size());
    Assertions.assertEquals(1, r32.edges.size());
    Assertions.assertEquals(1, r33.edges.size());
    Assertions.assertSame(r31.edges.get(0).to, r32.edges.get(0).to);
    Assertions.assertSame(r32.edges.get(0).to, r33.edges.get(0).to);
    Assertions.assertSame(r33.edges.get(0).to, r31.edges.get(0).to);

    var finale = r31.edges.get(0).to;
    Assertions.assertEquals(0, finale.edges.size());
    testNode(mermaid, finale.getId(), FINAL);
    testPath(mermaid, r31, finale);
    testPath(mermaid, r32, finale);
    testPath(mermaid, r33, finale);
  }

  private void testNode(String mermaid, Node id) {
    testNode(mermaid, id.getId(), id.name());
  }

  private void testPath(String mermaid, Node from, Node to) {
    testPath(mermaid, from.getId(), to.getId(), null);
  }

  private void testPath(String mermaid, Node from, Node to, String label) {
    testPath(mermaid, from.getId(), to.getId(), label);
  }

  private void testPath(String mermaid, long fromId, long toId, String label) {
    testPath(mermaid, ID + fromId, ID + toId, label);
  }

  private void testPath(String mermaid, String from, String to, String label) {
    String path;
    if (label == null) path = from + ARROW + to;
    else path = from + ARROW + "|\"" + label + "\"|" + to;
    Assertions.assertTrue(mermaid.contains(path));
  }

  private void testNode(String mermaid, long nodeId, String nodeName) {
    testNode(mermaid, ID + nodeId, nodeName);
  }

  private void testNode(String mermaid, String node, String nodeName) {
    String path = node + "(" + nodeName + ")";
    Assertions.assertTrue(mermaid.contains(path));
  }

  private void testIfNode(String mermaid, Node node) {
    String path = ID + node.getId() + "{if}";
    Assertions.assertTrue(mermaid.contains(path));
  }

}
