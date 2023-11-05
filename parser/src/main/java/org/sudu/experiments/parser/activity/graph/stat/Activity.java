package org.sudu.experiments.parser.activity.graph.stat;


import org.sudu.experiments.parser.activity.graph.Dag2Part;
import org.sudu.experiments.parser.activity.graph.IStat;
import org.sudu.experiments.parser.activity.graph.TerminalNode;

public class Activity extends ComplexStat {

    public static final String INITIAL = "Initial";
    public static final String FINAL = "Final";

    public void print(StringBuilder acc, int indent) {
        acc.append(" ".repeat(indent));
        acc.append("activity ");
        IStat.printBlock(acc, indent, ";", block);
        acc.append("\r\n");
    }

    @Override
    public String name() {
        return super.name();
    }

    @Override
    public String toDag1() {
        StringBuilder acc = new StringBuilder();
        acc.append("flowchart TB\r\n");
        acc.append(INITIAL+"((("+INITIAL+")))\r\n");
        acc.append(FINAL+"((("+FINAL+")))\r\n");


        IStat.toDag1Blocks(acc, block);
        IStat.toDag1Seq(acc, block);

        if (block.isEmpty()) {
            acc.append(INITIAL+"-->"+FINAL+"\r\n");
        } else {
            acc.append(INITIAL+"-->"+block.get(0).getMermaidNodeId()+"\r\n");
            var lastBlock = block.get(block.size()-1);
            IStat.toDag1Seq(acc, lastBlock, FINAL);
        }

        return acc.toString();
    }

    @Override
    public Dag2Part toDag2() {
        var start = new TerminalNode(INITIAL);
        var res = IStat.joinDag2(start, block);
        res = IStat.joinDag2(res, new Dag2Part(new TerminalNode(FINAL)));
        return res;
    }


}


