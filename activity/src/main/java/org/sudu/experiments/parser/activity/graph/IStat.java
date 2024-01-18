package org.sudu.experiments.parser.activity.graph;

import org.sudu.experiments.parser.activity.graph.stat.Id;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public interface IStat {
  void print(StringBuilder acc, int indent);

  String getMermaidNodeId();

  static void printBlock(StringBuilder acc, int indent, String delim, List<IStat> stat) {
    acc.append("{");

    boolean oneline = stat.stream().allMatch((s) -> s instanceof Id) && stat.size() <= 3;

    // multi-line
    if (!oneline) {
      acc.append("\r\n");
      for (var i = 0; i < stat.size(); i++) {
        stat.get(i).print(acc, indent + 2);
        if (i != stat.size() - 1)
          acc.append(delim);
        acc.append("\r\n");
      }

      //one-line
    } else {
      for (var i = 0; i < stat.size(); i++) {
        stat.get(i).print(acc, 0); //no need to go inside ID here
        if (i != stat.size() - 1) {
          acc.append(delim);
          acc.append(' ');
        }
      }
    }

    if (!oneline)
      acc.append(" ".repeat(indent));

    acc.append("}");
  }

  static void toDag1Blocks(StringBuilder acc, List<IStat> stat) {
    for (var b: stat) {
      acc.append(b.toDag1());
      acc.append("\r\n");
    }
  }

  static void toDag1Seq(StringBuilder acc, IStat from, String to) {
    for (var outputEdges: from.getOutputDag1()) {
      acc.append(outputEdges.getFrom().getMermaidNodeId());
      acc.append("-->");
      if (!outputEdges.getLabel().isEmpty())
        acc.append("|" + outputEdges.getLabel() + "|");
      acc.append(to);
      acc.append("\r\n");
    }
  }

  static void toDag1Seq(StringBuilder acc, List<IStat> stat) {
    if (stat.size() < 2)
      return;

    for (int i = 0; i < stat.size() - 1; i++) {
      toDag1Seq(acc, stat.get(i), stat.get(i + 1).getMermaidNodeId());
    }
  }

  default List<EdgeFrom> getOutputDag1() {
    return Collections.singletonList(new EdgeFrom((Node) this, null));
  }

  String name();

  String toDag1();

  static Dag2Part joinDag2(Dag2Part part1, Dag2Part part2) {
    if (part1 == null)
      return part2;

    //to simplify separate node creation like Id, terminal nodes
    if (part1.output.isEmpty()) {
      System.out.println("Wrong part1 without output");
    }

    for (var first: part1.output) {
      var secondList = new ArrayList<Node>();

      secondList.add(part2.input);

//            if (part2.input instanceof EmptyNode) {
//                secondList.addAll(part2.input.edges.stream().map(EdgeTo::getTo).toList());
//            } else {
//                secondList.add(part2.input);
//            }

      for (var second: secondList) {
        first.getFrom().edges.add(new EdgeTo(second, first.expr));
      }
    }

    return new Dag2Part(part1.input, part2.output);
  }

  static Dag2Part joinDag2(Dag2Part head, List<Dag2Part> tail) {
    var res = head;
    for (Dag2Part part: tail) {
      res = joinDag2(res, part);
    }
    return res;
  }

  static Dag2Part joinDag2(Node node, List<IStat> tail) {
    var res = Dag2Part.singleExit(node);
    for (IStat part: tail) {
      res = joinDag2(res, part.toDag2Part());
    }
    return res;
  }


  Dag2Part toDag2Part();
}
