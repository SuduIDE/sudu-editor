package org.sudu.experiments.parser.common.graph;

public interface ScopeGraphConstants {

  interface Nodes {
    int FAKE_NODE = -1;
    int BASE_NODE = 0;
    int MEMBER_NODE = 1;
  }

  interface Decls {
    int NULL = -1;
    int ARG_DECL_NODE = 1;
    int CREATOR_DECL_NODE = 2;
    int BASE_DECL_NODE = 3;
    int FIELD_DECL_NODE = 4;
    int METHOD_DECL_NODE = 5;
    int VAR_DECL_NODE = 6;
  }

  interface Refs {
    int NULL = -1;
    int CREATOR_CALL_NODE = 1;
    int FIELD_REF_NODE = 2;
    int METHOD_CALL_NODE = 3;
    int QUALIFIED_CALL_NODE = 4;
    int BASE_REF_NODE = 5;
    int SUPER_NODE = 6;
    int THIS_NODE = 7;
    int TYPE_NODE = 8;
  }

}
