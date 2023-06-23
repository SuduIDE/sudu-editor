export function equalModels(a, b) {
    if (a.language !== b.language) throw `Assertion error on model language: ${a.language} != ${b.language}`;
    equalUri(a.uri, b.uri);
}

export function equalUri(a, b) {
    if (a.scheme !== b.scheme) throw `Assertion error on uri scheme: ${a.scheme} != ${b.scheme}`;
    if (a.path !== b.path) throw `Assertion error on uri path: ${a.path} != ${b.path}`;
    if (a.authority !== b.authority) throw `Assertion error on uri authority: ${a.authority} != ${b.authority}`;
}

export function throws(f, message) {
    try {
        f();
    } catch (e) {
        if (e.message !== message)
            throw `Assertion error on catching exception: ${e.message} != ${message}`;
    }
}

export const initialTextJava = `
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
      System.out.println(helloWorld + n);
      sum(a + a);
    }
        
    @Deprecated
    private static void sum() {
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

    public interface A {
      int sumField(int field);
      
      default void foo() {
        sumField(10);
      }
    }
  }
`

export const workerUrl = "./../../src/worker.js"
