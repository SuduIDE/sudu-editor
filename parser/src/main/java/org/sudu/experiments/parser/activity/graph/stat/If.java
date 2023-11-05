package org.sudu.experiments.parser.activity.graph.stat;

import org.sudu.experiments.parser.activity.graph.BaseStat;
import org.sudu.experiments.parser.activity.graph.Dag2Part;
import org.sudu.experiments.parser.activity.graph.EdgeFrom;
import org.sudu.experiments.parser.activity.graph.EdgeTo;
import org.sudu.experiments.parser.activity.graph.IExpr;
import org.sudu.experiments.parser.activity.graph.IStat;

import java.util.ArrayList;
import java.util.List;

public class If extends BaseStat {
    public IExpr cond;
    public final List<IStat> ifBlock = new ArrayList<>();
    public final List<IStat> elseBlock = new ArrayList<>();

    @Override
    public void print(StringBuilder acc, int indent) {
        acc.append(" ".repeat(indent));
        acc.append("if (");
        acc.append(cond.toString());
        acc.append(") ");
        IStat.printBlock(acc, indent, ",", ifBlock);

        if (!elseBlock.isEmpty()) {
            acc.append(";\r\n");
            acc.append(" ".repeat(indent));
            acc.append("else ");
            IStat.printBlock(acc, indent, ",", elseBlock);
        }
    }

    @Override
    public String toDag1() {
        StringBuilder acc = new StringBuilder();
        acc.append(getMermaidNodeId()+"{if}\r\n");
        IStat.toDag1Blocks(acc, ifBlock);
        IStat.toDag1Seq(acc, ifBlock);

        IStat.toDag1Blocks(acc, elseBlock);
        IStat.toDag1Seq(acc, elseBlock);

        acc.append(getMermaidNodeId()+"-->|\""+cond.toString()+"\"|"+ifBlock.get(0).getMermaidNodeId()+"\r\n");

        if (!elseBlock.isEmpty())
            acc.append(getMermaidNodeId()+"-->|else|"+elseBlock.get(0).getMermaidNodeId()+"\r\n");

        return acc.toString();
    }

    @Override
    public String drawDagNode() {
        return "{if}";
    }

    @Override
    public Dag2Part toDag2() {
        var clone = new If();
        clone.cond = cond;
        clone.ifBlock.addAll(ifBlock);
        clone.elseBlock.addAll(elseBlock);

        ArrayList<EdgeFrom> output = new ArrayList<EdgeFrom>();


        var ifBranch = IStat.joinDag2(null, clone.ifBlock);
        clone.edges.add(new EdgeTo(ifBranch.input, cond, false));
        output.addAll(ifBranch.output);


        if (elseBlock.isEmpty()) {
            output.add(EdgeFrom.Else(clone, cond));
        } else {
            var elseBranch = IStat.joinDag2(null, clone.elseBlock);
            clone.edges.add(new EdgeTo(elseBranch.input, cond, true));
            output.addAll(elseBranch.output);
        }

        return new Dag2Part(clone, output);
    }

    @Override
    public List<EdgeFrom> getOutputDag1() {
        var res = new ArrayList<EdgeFrom>();
        res.addAll(ifBlock.get(ifBlock.size()-1).getOutputDag1());

        if (elseBlock.isEmpty()) {
            res.add(EdgeFrom.Else(this, cond));
        } else {
            res.addAll(elseBlock.get(elseBlock.size()-1).getOutputDag1());
        }
        return res;
    }

    @Override
    public String name() {
        return "if";
    }
}
