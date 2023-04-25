package org.sudu.experiments.demo;

public interface StartFile {

  String START_CODE_JAVA = """
      package sudu.editor;
      
      /*
      * This is multiplatform lightweight code editor
      */
      
      public class Main {
        private static String helloWorld = "Hello, World!";
        private int a;
        
        public static void main(String[] args) {
          System.out.println(helloWorld);
        }
        
        @Deprecated
        private int sum(int a) {
          int b = 10;
          int c = a + b + this.a;
          return c;
        }
        
        public interface A {
          void a();
          
          // Some func
          void b();
          
          void c();
        }
      }
      """;
}
