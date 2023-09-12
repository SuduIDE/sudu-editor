import {newTextModel, newCodeDiff} from "../../src/editor.js";
import {initialTextJava, workerUrl} from "../utils.js";

const editorArgs = {containerId: "editor", workerUrl: "./../" + workerUrl};

function main() {
    newCodeDiff(editorArgs).then(
        diff1 => {
            let model = newTextModel(initialTextJava, "java", "urlNew");
            diff1.setLeftModel(model)
            diff1.setReadonly(true)
        }
    )
}

document.addEventListener("DOMContentLoaded", main)
