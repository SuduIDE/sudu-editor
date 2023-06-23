import { newEditor, newTextModel } from "../../src/editor.js";

const workerUrl = "./../../src/worker.js"

function main() {
    newEditor({containerId: "editor", workerUrl: workerUrl})
        .then(run, error => console.error(error));
}

const initialText = "0\n1\n2\n3\n4\n5\n6\n7\n8\n9\n0\n1\n2\n3\n4\n5\n6\n7\n8\n9\n0\n1\n2\n3\n4\n5\n6\n7\n8\n9\n0\n1\n2\n3\n4\n5\n6\n7\n8\n9\n"

function run(editor) {
    test1(editor);

    console.log("Test successful!");
}

function test1(editor) {
    let model = newTextModel(initialText, "language", "urlNew")
    editor.setModel(model);
    document.getElementById("reveal1").addEventListener('click', () => editor.revealLine(1))
    document.getElementById("reveal10").addEventListener('click', () => editor.revealLine(10))
    document.getElementById("reveal30").addEventListener('click', () => editor.revealLine(30))
    document.getElementById("reveal41").addEventListener('click', () => editor.revealLine(41))
    document.getElementById("revealCenter20")
        .addEventListener('click', () => editor.revealLineInCenter(20))
    document.getElementById("revealCenter40")
        .addEventListener('click', () => editor.revealLineInCenter(40))
}

document.addEventListener("DOMContentLoaded", main)
