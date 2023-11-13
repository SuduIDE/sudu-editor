package org.sudu.experiments.editor;

public interface StartFile {

  String START_CODE_FILE = "example.java";

  String START_CODE_JAVA = """
      package sudu.editor;
      
      /*
      * This is multiplatform lightweight code editor
      */
      
      public class Main {
      
        private static String helloWorld = "Hello,\\tWorld\\u3000";
        private static char n = '\\n';
        private static int a;
            
        public int field;

        public static void main(String[] args) {
          sum(a + a);
          var g = g(a + a);
          g.a;
          var array = new int[field];
          for (var a: array) {
            sum(a);
          }
        }
            
        @Deprecated
        private static void sum() {
          G g = new G(12);
          g.a;
        }
            
        @Deprecated
        private static int sum(int a) {
          int b = 10;
          int c = a + b;
          return c;
        }
            
        public int sumField(int field) {
          return field + this.field;
        }
        
        public G g(int a) {
          return new G(a);
        }
        
        public class G {
          int a;
          public G(int a) {
            this.a = a;
          }
        }

        public interface I {
          int sumField(int field);

          default void foo(int a) {
            sumField(a);
          }
        }

        public class C {
          int field;
        }

        public class A extends C implements I {
          public void a() {
            foo(field);
          }

          @Override
          public int sumField(int field) {
            return 0;
          }
        }
      }
      """;

  static char[] getChars() {
    return START_CODE_JAVA.toCharArray();
  }
}
