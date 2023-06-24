import {newEditor, newTextModel} from "../../src/editor.js";
import {initialTextJava, workerUrl} from "../utils.js";

const newText = "1\n2\n3\n4\nselected\n6"

function test1(editor) {
    let model = newTextModel(initialTextJava, "java", null)
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

function main() {
    newEditor({containerId: "editor", workerUrl: "./../" + workerUrl})
        .then(run, error => console.error(error));
}

function run(editor) {
    test1(editor);
    console.log("Test successful!");
}

document.addEventListener("DOMContentLoaded", main)
