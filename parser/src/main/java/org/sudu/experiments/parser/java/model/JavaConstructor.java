package org.sudu.experiments.parser.java.model;

import org.sudu.experiments.parser.common.Pos;

public class JavaConstructor extends JavaMethod {

    public JavaConstructor(String name, Pos position, int numberOfArgs) {
        super(name, position, false, numberOfArgs);
    }
}
