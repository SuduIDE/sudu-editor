import {newEditor, newTextModel} from "../../src/editor.js";
import {initialTextJava, workerUrl} from "../utils.js";

function registerForButton(id, listener) {
    document.getElementById(id).addEventListener("click", listener)
}

function test1(editor) {
    let model = newTextModel(initialTextJava, "java", null)
    editor.setModel(model)
    let disposableRef = editor.registerReferenceProvider("java", {
        provideReferences(model, position, context, token) {
            return [{
                    uri: model.uri,
                    range: {
                        startLineNumber: position.lineNumber + 1,
                        startColumn: position.column + 5,
                        endLineNumber: position.lineNumber + 1,
                        endColumn: position.column + 10
                        }
                },
                {
                    uri: model.uri,
                    range: {
                        startLineNumber: position.lineNumber - 2,
                        startColumn: position.column + 1,
                        endLineNumber: position.lineNumber - 1,
                        endColumn: position.column + 2
                    }
                }]
        }
    });
    let disposableDecl = editor.registerDeclarationProvider("java",
        {
            provideDeclaration(model, position, token) {
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
    );
    let disposableDef = editor.registerDefinitionProvider("java",
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
                }, {
                    uri: model.uri,
                    range: {
                        startLineNumber: position.lineNumber - 2,
                        startColumn: position.column,
                        endLineNumber: position.lineNumber -  2,
                        endColumn: position.column
                    }
                }
                ]
            }
        }
    );
    registerForButton("disposeDef", () => disposableDef.dispose());
    registerForButton("disposeDecl",
        () => disposableDecl.dispose())
    registerForButton("disposeRef",
        () => disposableRef.dispose())
    registerForButton("registerEmptyDefinitions",
        () => {
            disposableDef.dispose()
            disposableDef = editor.registerDefinitionProvider("java", {
                provideDefinition(model, position, token) {
                    return [];
                }
            });
        })

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
