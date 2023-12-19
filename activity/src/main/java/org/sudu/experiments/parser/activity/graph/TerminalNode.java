package org.sudu.experiments.parser.activity.graph;

public class TerminalNode extends Node {
    private final String name;

    public TerminalNode(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }
    public String drawDagNode() {
        return "((("+name+")))";
    }
}
