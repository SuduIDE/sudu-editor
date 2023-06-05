package org.sudu.experiments.demo;

import java.nio.charset.StandardCharsets;

public interface StartFile {

  String START_CODE_JAVA = """
      package sudu.editor;
      
      /*
      * This is multiplatform lightweight code editor
      */
      
      public class Main {
      
        public int field;

        public int sumField(int field) {
          return field + this.field;
        }

        public interface A {
          int sumField(int field);
          
          default void foo() {
            sumField(10);
          }
        }
      }
      """;

  static char[] getChars() {
    return START_CODE_JAVA.toCharArray();
  }
}
