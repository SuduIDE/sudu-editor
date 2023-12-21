package org.sudu.experiments.parser.common.graph.node;

public interface NodeTypes {

  interface DeclTypes {
    int LOCAL_VAR = 1;
    int ARGUMENT = 2;
    int FIELD = 3;
    int CALLABLE = 4;
    int TYPE_DECL = 5;
  }
  interface RefTypes {
    int BASE = 1;
    int CALL = 2;
    int LITERAL = 3;
    int THIS = 4;
    int SUPER = 5;
    int TYPE_USAGE = 6;
    int BASE_EXPRESSION = 7;
    int ARRAY_INDEX = 8;
    int QUALIFIED = 9;
  }

  interface MethodTypes {
    int METHOD = 1;
    int CREATOR = 2;
    int ARRAY_CREATOR = 3;
    int THIS = 4;
    int SUPER = 5;
    int THIS_CALL = 6;
    int SUPER_CALL = 7;
  }

}
