import {newEditor, newTextModel} from "../../src/editor.js";
import {initialTextJava, workerUrl} from "../utils.js";

function test1(editor) {
    let model = newTextModel(initialTextJava, "java", null)
    editor.setModel(model)
    let counter = 0;
    editor.registerDocumentHighlightProvider("java",
        {
            provideDocumentHighlights(model, position, token) {
                if (counter === 0) {
                    counter++;
                    return new Promise((resolve, reject) => {
                        setTimeout(() => resolve([
                            {
                                range: {
                                    startLineNumber: 17,
                                    startColumn: 9,
                                    endLineNumber: 17,
                                    endColumn: 9
                                },
                                kind: 1
                            }
                        ]), 3000)
                    });
                } else {
                    return new Promise((resolve, reject) => {
                        resolve([
                            {
                                range: {
                                    startLineNumber: 1,
                                    startColumn: 1,
                                    endLineNumber: 3,
                                    endColumn: 3
                                },
                                kind: 1
                            }
                        ])
                    });
                }
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
