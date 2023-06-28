import {newEditor, newTextModel} from "../../src/editor.js";
import {initialTextJava, workerUrl} from "../utils.js";

const uriBase = {path: "base"};
const otherUri = {path: "other"};

function test1(editor) {
    let model = newTextModel(initialTextJava, "java", uriBase);
    let otherModel = newTextModel(newText, "java", otherUri);
    editor.setModel(model);
    editor.registerDefinitionProvider("java",
        {
            provideDefinition(model, position, token) {
                return [{
                    uri: uriBase,
                    range: {
                        startLineNumber: position.lineNumber + 1,
                        startColumn: position.column + 5,
                        endLineNumber: position.lineNumber + 1,
                        endColumn: position.column + 10
                    }
                }, {
                    uri: otherUri,
                    range: {
                        startLineNumber: position.lineNumber - 2,
                        startColumn: position.column,
                        endLineNumber: position.lineNumber -  2,
                        endColumn: position.column + 3
                    }
                }
                ]
            }
        }
    );

    editor.registerEditorOpener({
        openCodeEditor(source, resource, selectionOrPosition) {
            if (resource === otherUri) {
                source.setModel(otherModel);
            }
        }
    });
}

const newText = `
package org.sudu.experiments.demo;

public class Range {
  public int endColumn;
  public int endLineNumber;
  public int startColumn;
  public int startLineNumber;

  public Range() {}

  public Range(int endColumn, int endLineNumber, int startColumn, int startLineNumber) {
    this.endColumn = endColumn;
    this.endLineNumber = endLineNumber;
    this.startColumn = startColumn;
    this.startLineNumber = startLineNumber;
  }

  public Selection toSelection() {
    Selection sel = new Selection();
    sel.getLeftPos().set(startLineNumber, startColumn);
    sel.getRightPos().set(endLineNumber, endColumn);
    return sel;
  }

  @Override
  public String toString() {
    return "Range{ from (line=" + startLineNumber + ", col=" + startColumn + ") "
        + "to (line=" + endLineNumber + ", col=" + endColumn + ")}";
  }
}
`

function main() {
    newEditor({containerId: "editor", workerUrl: "./../" + workerUrl})
        .then(run, error => console.error(error));
}

function run(editor) {
    test1(editor);
    console.log("Test successful!");
}

document.addEventListener("DOMContentLoaded", main)
