import {newEditor, newTextModel} from "../../src/editor.js";

const workerUrl = "./../../../../../../../module/src/worker.js"

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

const newText = "1\n2\n3\n4\nselected\n6"

function test1(editor) {

    let model = newTextModel(initialText, "java", null)
    // Uri's of model and anotherModel must differ (obviously)
    let anotherModel = newTextModel(newText, "java", {path: "somePath"})
    editor.setModel(model)
    editor.registerDefinitionProvider("java",
        {
            provideDefinition(model, position, token) {
                // Setting new model will cause editor to provide def for the prev model,
                // but performing goto with a new model --- error!
                editor.setModel(anotherModel)
                return [{
                    uri: model.uri,
                    range: {
                        startLineNumber: 4,
                        startColumn: 0,
                        endLineNumber: 4,
                        endColumn: 8
                    }
                }]
            }
        }
    )
}

document.addEventListener("DOMContentLoaded", main)
