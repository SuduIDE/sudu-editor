package org.sudu.experiments.parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.sudu.experiments.parser.common.tree.IntervalNode;
import org.sudu.experiments.parser.common.tree.IntervalTree;
import org.sudu.experiments.parser.common.graph.node.FakeNode;

public class IntervalTreeTest {

/*

          (O, 10, 0)
              |
              |
          (0, 10, 1)
          /        \
         /          \
    (0, 4, 2)    (4, 10, 2)
                 /        \
                /          \
           (4, 7, 3)    (7, 10, 3)

*/

  private IntervalNode getRoot() {
    var rootScope = new FakeNode(null);
    IntervalNode root = new IntervalNode(new Interval(0, 10, 0), rootScope);

    root.addChild(new Interval(0, 10, 1));
    root.lastChild().addChild(new Interval(0, 4, 2), new FakeNode(rootScope));
    var lastChildScope = new FakeNode(rootScope);
    root.lastChild().addChild(new Interval(4, 10, 2), lastChildScope);

    root.lastChild().lastChild().addChild(new Interval(4, 7, 3), new FakeNode(lastChildScope));
    root.lastChild().lastChild().addChild(new Interval(7, 10, 3), new FakeNode(lastChildScope));
    return root;
  }

  @Test
  public void testInsert1() {
    IntervalNode root = getRoot();
    IntervalNode insertNode = root.getChild(0).getChild(0);
    IntervalTree tree = new IntervalTree(root);

    tree.makeInsertDiff(2, 2);

    Assertions.assertEquals(0, root.getStart());
    Assertions.assertEquals(12, root.getStop());
    Assertions.assertFalse(root.needReparse);

    Assertions.assertEquals(0, root.lastChild().getStart());
    Assertions.assertEquals(12, root.lastChild().getStop());
    Assertions.assertFalse(root.lastChild().needReparse);

    Assertions.assertEquals(0, insertNode.getStart());
    Assertions.assertEquals(6, insertNode.getStop());
    Assertions.assertTrue(insertNode.needReparse);
  }

  @Test
  public void testDelete1() {
    IntervalNode root = getRoot();
    IntervalNode deleteNode = root.getChild(0).getChild(0);
    IntervalTree tree = new IntervalTree(root);

    tree.makeDeleteDiff(2, 2);

    Assertions.assertEquals(0, root.getStart());
    Assertions.assertEquals(8, root.getStop());
    Assertions.assertFalse(root.needReparse);

    Assertions.assertEquals(0, root.lastChild().getStart());
    Assertions.assertEquals(8, root.lastChild().getStop());
    Assertions.assertFalse(root.lastChild().needReparse);

    Assertions.assertEquals(0, deleteNode.getStart());
    Assertions.assertEquals(2, deleteNode.getStop());
    Assertions.assertTrue(deleteNode.needReparse);
  }

  @Test
  public void testDelete2() {
    IntervalNode root = getRoot();
    IntervalTree tree = new IntervalTree(root);

    tree.makeDeleteDiff(4, 6);

    Assertions.assertEquals(0, root.getStart());
    Assertions.assertEquals(4, root.getStop());
    Assertions.assertFalse(root.needReparse);

    Assertions.assertEquals(0, root.lastChild().getStart());
    Assertions.assertEquals(4, root.lastChild().getStop());
    Assertions.assertEquals(1, root.lastChild().children.size());
    Assertions.assertFalse(root.lastChild().needReparse);

    Assertions.assertEquals(0, root.lastChild().lastChild().getStart());
    Assertions.assertEquals(4, root.lastChild().lastChild().getStop());
    Assertions.assertTrue(root.lastChild().lastChild().children.isEmpty());
    Assertions.assertFalse(root.lastChild().lastChild().needReparse);
  }

  @Test
  public void testDelete3() {
    IntervalNode root = getRoot();
    IntervalTree tree = new IntervalTree(root);

    tree.makeDeleteDiff(0, 10);

    Assertions.assertEquals(0, root.getStart());
    Assertions.assertEquals(0, root.getStop());
    Assertions.assertTrue(root.needReparse);
  }

}
