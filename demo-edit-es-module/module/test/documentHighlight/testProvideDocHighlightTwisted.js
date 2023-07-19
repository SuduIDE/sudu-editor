import {newEditor, newTextModel} from "../../src/editor.js";
import {initialTextJava, workerUrl} from "../utils.js";

function test1(editor) {
    let model = newTextModel(initialTextJava, "java", null)
    editor.setModel(model)
    let savedResolve = null
    editor.registerDocumentHighlightProvider("java",
        {
            provideDocumentHighlights(model, position, token) {
                if (savedResolve === null) {
                    return new Promise((resolve, reject) => {
                        savedResolve = resolve
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
                        setTimeout(
                            savedResolve(
                                [{
                                    range: {
                                        startLineNumber: 17,
                                        startColumn: 9,
                                        endLineNumber: 17,
                                        endColumn: 9
                                    },
                                    kind: 1
                                }]
                            ), 1500)
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
