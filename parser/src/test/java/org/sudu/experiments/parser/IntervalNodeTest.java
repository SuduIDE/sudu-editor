package org.sudu.experiments.parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.sudu.experiments.arrays.ArrayReader;
import org.sudu.experiments.parser.common.tree.IntervalNode;

public class IntervalNodeTest {

  @Test
  public void testReadFromInts() {
    IntervalNode expNode = new IntervalNode(new Interval(0, 10, 0));
    expNode.addChild(new Interval(0, 10, 1));
    expNode.lastChild().addChild(new Interval(0, 4, 2));
    expNode.lastChild().addChild(new Interval(4, 10, 2));
    expNode.lastChild().lastChild().addChild(new Interval(4, 7, 3));
    expNode.lastChild().lastChild().addChild(new Interval(7, 10, 3));

    int[] result = expNode.toInts();
    ArrayReader reader = new ArrayReader(result);

    IntervalNode gotNode = IntervalNode.getNode(reader);

    Assertions.assertEquals(expNode, gotNode);
  }

}
