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
    int JAVADOC = STRING; // todo fix later
    int TYPES_LENGTH = OPERATOR + 1;
  }

  interface TokenStyles {
    int NORMAL = 0;       // 0000
    int ITALIC = 1;       // 0001
    int BOLD = 2;         // 0010
    int ITALIC_BOLD = 3;  // 0011
    int ERROR = 4;        // 0100
    static int error(int style) {
      return style | ERROR;
    }
  }

  interface IntervalTypes {

    int UNKNOWN = -1;

    interface Java {
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
      int TYPE = 2;
      int MEMBER = 3;
    }

    interface Js {
      int UNKNOWN = -1;
      int PROGRAM = 0;
      int SRC_ELEM = 1;
    }
  }
}
