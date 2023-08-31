import {newEditor, newTextModel} from "../../src/editor.js";
import {initialTextJava, workerUrl} from "../utils.js";

function test1(editor) {
    let modelHugeFileName = newTextModel(initialTextJava, "java", {path: "reallyHugeFileNameWWWWWWWWWWWWWWW.java"})
    let modelNormalFileName = newTextModel(initialTextJava, "java", {path: "somePath.java"})
    editor.setModel(modelHugeFileName)

    editor.registerDefinitionProvider("java",
        {
            provideDefinition(model, position, token) {
                return generateDefinitionData(model, position, 200).concat(
                    generateDefinitionData(modelNormalFileName, position, 200)
                );
            }
        }
    );
}

function main() {
    newEditor({containerId: "editor", workerUrl: "./../" + workerUrl})
        .then(run, error => console.error(error));
}

function run(editor) {
    test1(editor);
}

function generateDefinitionData(model, position, n) {
    let definitions = [];
    for (let i = 0; i < n; i++) {
            definitions.push(
                {
                    uri: model.uri,
                    range: {
                        startLineNumber: position.lineNumber - 2 * (i % 2 === 0),
                        startColumn: position.column,
                        endLineNumber: position.lineNumber + 2 * (i % 2),
                        endColumn: position.column
                    }
                }
            )
    }
    return definitions
}

document.addEventListener("DOMContentLoaded", main)
