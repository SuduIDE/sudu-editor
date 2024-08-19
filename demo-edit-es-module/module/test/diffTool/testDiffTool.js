import {newTextModel, newCodeDiff} from "../../src/editor.js";
import {initialTextJava, workerUrl} from "../utils.js";
const codiconUrl = "../../../../codicon/src/main/resources/fonts/codicon.ttf";

const editorArgs = {
    containerId: "editor", workerUrl: "./../" + workerUrl,
    codiconUrl : codiconUrl
};

function main() {
    newCodeDiff(editorArgs).then(
        diff1 => {
            let model1 = newTextModel(initialTextJava, "java", "url1")
            diff1.setLeftModel(model1)
            let model2 = newTextModel(secondTextJava, "java", "url2")
            diff1.setRightModel(model2)
            // diff1.setReadonly(true)
        }
    )
}

const secondTextJava =
`package sudu.editor;
  
  public class Main {
  
    private static String helloWorld = "Hello Second World";
    private static char n = '\\n';
    private static int a;
    private static String s = "";

    public int field;

    public static void main(String[] args) {
      System.out.println(helloWorld + n);
      sum(a + a);
    }
        
    @Deprecated
    private static int sum(int a) {
      int b = 10;
      return b + 10;
    }
        
    public int summm(int field) {
      return field + this.field;
    }

    public interface A {
      int summm(int field);
      
      default void foo() {
        summm(10);
      }
    }
  }
`

document.addEventListener("DOMContentLoaded", main)
