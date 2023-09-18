package org.sudu.experiments.parser.common.graph;

public interface ScopeGraphConstants {

  interface Nodes {
    int FAKE_NODE = -1;
    int BASE_NODE = 0;
    int MEMBER_NODE = 1;
  }

  interface Decls {
    int ARG_DECL_NODE = 1;
    int CREATOR_DECL_NODE = 2;
    int BASE_DECL_NODE = 3;
    int FIELD_DECL_NODE = 4;
    int METHOD_DECL_NODE = 5;
    int VAR_DECL_NODE = 6;
  }

}
