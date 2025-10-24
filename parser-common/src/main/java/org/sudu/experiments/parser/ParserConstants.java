package org.sudu.experiments.parser;

import java.util.HashMap;
import java.util.Map;

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

    Map<String, Integer> semanticTokenTypeMap = fillMap();

    private static Map<String, Integer> fillMap() {
      Map<String, Integer> map = new HashMap<>();
      int counter = TYPES_LENGTH;
      map.put("namespace", counter++);
      map.put("class", counter++);
      map.put("enum", counter++);
      map.put("interface", counter++);
      map.put("struct", counter++);
      map.put("typeParameter", counter++);
      map.put("type", counter++);
      map.put("parameter", counter++);
      map.put("variable", counter++);
      map.put("property", counter++);
      map.put("enumMember", counter++);
      map.put("decorator", counter++);
      map.put("event", counter++);
      map.put("function", counter++);
      map.put("method", counter++);
      map.put("macro", counter++);
      map.put("label", counter++);
      map.put("comment", counter++);
      map.put("string", counter++);
      map.put("keyword", counter++);
      map.put("number", counter++);
      map.put("regexp", counter++);
      map.put("operator", counter);

      map.put("unknown", -1);
      return map;
    }

    static boolean isSemanticToken(int tokenType) {
      return tokenType >= TYPES_LENGTH;
    }

    static int getSemanticType(String tokenType) {
      var type = semanticTokenTypeMap.get(tokenType);
      return type == null ? ERROR : type == -1 ? DEFAULT : type;
    }
  }

  interface TokenStyles {
    int NORMAL = 0;       // 0000
    int ITALIC = 1;       // 0001
    int BOLD = 2;         // 0010
    int ITALIC_BOLD = 3;  // 0011
    int ERROR = 4;        // 0100
    int STYLES_LENGTH = ERROR + 1;

    Map<String, Integer> semanticModifiersMap = fillMap();

    private static Map<String, Integer> fillMap() {
      Map<String, Integer> map = new HashMap<>();
      map.put("declaration", NORMAL);
      map.put("definition", NORMAL);
      map.put("readonly", NORMAL);
      map.put("static", ITALIC);
      map.put("deprecated", ERROR);
      map.put("abstract", NORMAL);
      map.put("async", NORMAL);
      map.put("modification", NORMAL);
      map.put("documentation", NORMAL);
      map.put("defaultLibrary", NORMAL);
      return map;
    }

    static int getSemanticStyle(String mod) {
      var type = semanticModifiersMap.get(mod);
      return type == null ? NORMAL : type;
    }

    static int error(int style) {
      return style | ERROR;
    }
  }

  interface IntervalTypes {

    int UNKNOWN = -1;
    int ERROR_ROOT = -2;

    interface Java {
      int COMP_UNIT = 0;
      int PACKAGE = 1;
      int IMPORT = 2;
      int TYPE_DECL = 3;
      int CLASS_BODY = 4;
      int COMMENT = 5;
    }

    interface Cpp {
      int TRANS_UNIT = 0;
      int DECLARATION = 1;
      int TYPE = 2;
      int MEMBER = 3;
    }

    interface Js {
      int PROGRAM = 0;
      int SRC_ELEM = 1;
    }
  }
}
