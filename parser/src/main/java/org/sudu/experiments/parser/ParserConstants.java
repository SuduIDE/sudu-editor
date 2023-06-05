package org.sudu.experiments.parser;

public interface ParserConstants {
  
  interface TokenTypes {
    int DEFAULT = 0;
    int KEYWORD = 1;
    int NULL = 1;
    int BOOLEAN = 1;
    int SEMI = 1;
    int FIELD = 2;
    int STRING = 3;
    int ERROR = 5;
    int NUMERIC = 7;
    int METHOD = 8;
    int COMMENT = 11;
    int ANNOTATION = 12;
    int TYPE = 13;
    int OPERATOR = 14;
    int TYPES_LENGTH = OPERATOR + 1;
  }

  interface TokenStyles {
    int NORMAL = 0;
    int ITALIC = 1;
    int BOLD = 2;
    int ITALIC_BOLD = 3;
  }

  interface IntervalTypes {

    interface Java {
      int UNKNOWN = -1;
      int COMP_UNIT = 0;
      int PACKAGE = 1;
      int IMPORT = 2;
      int TYPE_DECL = 3;
      int CLASS_BODY = 4;
      int COMMENT = 5;
    }

    interface Cpp {
      int UNKNOWN = -1;
      int TRANS_UNIT = 0;
      int DECLARATION = 1;
    }

    interface Js {
      int UNKNOWN = -1;
      int PROGRAM = 0;
      int SRC_ELEM = 1;
    }
  }
}
