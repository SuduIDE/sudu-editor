import { newEditor, newTextModel } from "../../src/editor.js";
import {initialTextJava, workerUrl} from "../utils.js";

function test1(editor) {
    let model = newTextModel(initialTextJava, "java", "urlNew")
    editor.setModel(model);
    editor.setFontSize(20);
}

function main() {
    newEditor({containerId: "editor", workerUrl: "./../" + workerUrl, readonly: true})
        .then(run, error => console.error(error));
}

function run(editor) {
    test1(editor);
    document.getElementById("true").addEventListener('click', () => editor.setReadonly(true))
    document.getElementById("false").addEventListener('click', () => editor.setReadonly(false))
    console.log("Test successful!");
}

document.addEventListener("DOMContentLoaded", main)
