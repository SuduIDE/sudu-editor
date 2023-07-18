import {newEditor, newTextModel} from "../../src/editor.js";
import {initialTextJava, workerUrl} from "../utils.js";

function test1(editor) {
    let model = newTextModel(initialTextJava, "java", null)
    editor.setModel(model)
    editor.registerDocumentHighlightProvider("java",
        {
            provideDocumentHighlights(model, position, token) {
                return [
                    {
                        range: {
                            startLineNumber: 17,
                            startColumn: 9,
                            endLineNumber: 17,
                            endColumn: 9
                        },
                        kind: 1
                    },
                    {
                        range: {
                            startLineNumber: 17,
                            startColumn: 13,
                            endLineNumber: 17,
                            endColumn: 13
                        },
                        kind: 1
                    }, {
                        range: {
                            startLineNumber: 16,
                            startColumn: 27,
                            endLineNumber: 16,
                            endColumn: 36
                        },
                        kind: 1
                    }
                ]
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
