import {initialTextJava, workerUrl} from "../utils.js";

// Emulate jQuery define.amd not null
function define() {
}

define.amd = 5
window.define = define

const editModule = await import ("../../src/editor.js");

const editor = await editModule.newEditor({containerId: "editor", workerUrl: "./../" + workerUrl});
let model = editModule.newTextModel(initialTextJava, "java")
editor.setModel(model)
console.log("Test successful!");

