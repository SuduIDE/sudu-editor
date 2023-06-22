import {newEditor, newTextModel} from "../../src/editor.js";

const workerUrl = "./../../src/worker.js"

function main() {
    newEditor({containerId: "editor", workerUrl: workerUrl})
        .then(run, error => console.error(error));
}

const initialText = `
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

function run(editor) {
    test1(editor);
    console.log("Test successful!");
}

function test1(editor) {

    let model = newTextModel(initialText, "java", null)
    editor.setModel(model)
    // Only the first provider will fire, since we use first matching provider
    editor.registerDefinitionProvider("java",
        {
            provideDefinition(model, position, token) {
                return [{
                    uri: model.uri,
                    range: {
                        startLineNumber: position.lineNumber + 1,
                        startColumn: position.column + 5,
                        endLineNumber: position.lineNumber + 1,
                        endColumn: position.column + 10
                    }
                }]
            }
        }
    )
    editor.registerDefinitionProvider("java",
        {
            provideDefinition(model, position, token) {
                throw "Unreachable code"
            }
        }
    )

}

document.addEventListener("DOMContentLoaded", main)
