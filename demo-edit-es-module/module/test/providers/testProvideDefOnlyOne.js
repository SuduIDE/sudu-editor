import {newEditor, newTextModel} from "../../src/editor.js";
import {initialTextJava, workerUrl} from "../utils.js";

function test1(editor) {
    let model = newTextModel(initialTextJava, "java", null)
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

function main() {
    newEditor({containerId: "editor", workerUrl: "./../" + workerUrl})
        .then(run, error => console.error(error));
}

function run(editor) {
    test1(editor);
    console.log("Test successful!");
}

document.addEventListener("DOMContentLoaded", main)
