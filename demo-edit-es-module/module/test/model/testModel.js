import { newEditor, newTextModel } from "../../src/editor.js";
import {initialTextJava, workerUrl, throws} from "../utils.js";

function test1(editor) {
    // Check that there is a default model
    const basicModel = editor.getModel();
    if (!basicModel) {
        throw `Assertion error on model: ${basicModel} == null}`
    }

    // Simple set and get check (with added someField to show that actual js object is preserved)
    let model = newTextModel(initialTextJava, "java", "urlNew")
    editor.setModel(model);
    let currentModel = editor.getModel();
    if (currentModel !== model)
        throw `Assertion error on model: ${currentModel} != ${model}`

    // Setting null model throws exception
    throws(() => editor.setModel(null), "bad model");

    // Checking that previous null model set did not change actual model
    currentModel = editor.getModel();
    if (currentModel !== model)
        throw `Assertion error on model: ${currentModel} != ${model}`
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
