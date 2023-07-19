import {newEditor, newTextModel} from "../../src/editor.js";
import {initialTextJava, workerUrl} from "../utils.js";

function test1(editor) {
    let model = newTextModel(initialTextJava, "java")
    editor.setModel(model)
    editor.registerDocumentHighlightProvider("java",
        {
            provideDocumentHighlights(model, position, token) {
                return [
                    {
                        range: {
                            startLineNumber: 17,
                            startColumn: 11,
                            endLineNumber: 17,
                            endColumn: 12
                        },
                        kind: 1
                    },
                    {
                        range: {
                            startLineNumber: 17,
                            startColumn: 15,
                            endLineNumber: 17,
                            endColumn: 16
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
